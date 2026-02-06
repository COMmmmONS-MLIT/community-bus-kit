package maint.service.operation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import maint.constants.CommConstant;
import maint.doc.ImportBusServicesExcelUpload;
import maint.dto.operation.holidayCalendars.BusServiceInfoDto;
import maint.dto.operation.importBusServices.*;
import maint.mapper.*;
import maint.service.BaseService;
import maint.service.planDaiya.DailyBusDaiyasService;
import maint.utils.ExObjectUtils;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 便ダイヤ登録SERVICE
 *
 * @author JEKAL.H
 *
 */
@Service
public class ImportBusServicesService extends BaseService {

  /** テンプレートEXCELファイル */
  private final String TEMP_EXCEL_FILE = "importBusServices";

  /** EXCELファイル名 */
  private final String EXCEL_FILE_NAME = "便情報インポート";

  /** ResourceLoader */
  @Autowired
  private ResourceLoader resourceLoader;

  /** 日毎便ダイヤSERVICE */
  @Autowired
  DailyBusDaiyasService dailyBusDaiyasService;

  /** 事業者MAPPER MAPPER */
  @Autowired
  AgenciesMapper agenciesMapper;

  /** 便MAPPER */
  @Autowired
  BusServicesMapper busServicesMapper;

  /** 便ダイヤ改正MAPPER */
  @Autowired
  DaiyaRevisionsMapper daiyaRevisionsMapper;

  /** 運行カレンダーMAPPER */
  @Autowired
  CalendarsMapper calendarsMapper;

  /** 系統停留所Mapper */
  @Autowired
  LineRouteBusStopsMapper lineRouteBusStopsMapper;

  /** 停留所MAPPER */
  @Autowired
  BusStopMapper busStopMapper;

  /** 便ダイヤMAPPER */
  @Autowired
  BusDaiyasMapper busDaiyasMapper;

  /**
   * 便情報インポート用テンプレートExcelをダウンロード
   *
   * @return Excelバイナリ
   * @throws Exception 生成中の例外は呼出し側へスロー
   */
  public ResponseEntity<?> getImportBusServicesTemplate() throws Exception {
    // テンプレートを読み込み
    Resource resource = resourceLoader.getResource(CommConstant.TEMP_EXCEL_FOLDER_PATH + this.TEMP_EXCEL_FILE + CommConstant.EXCEL_FILE_EXTENSION);

    try {
      // テンプレートファイルをバイト配列として読み込む
      byte[] fileContent = FileCopyUtils.copyToByteArray(resource.getInputStream());

      // ダウンロード日付（現在日付を使用）
      String downloadDate = String.valueOf(LocalDate.now());

      // ダウンロード処理を実行
      return excelDownloadProc(fileContent, downloadDate);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      throw e;
    }
  }

  /**
   * Excelのバイト配列をResponseEntityに組み立て
   *
   * @param documentContent Excel
   * @param targetMonth ファイル名に含める運行開始日
   * @return ダウンロード用ResponseEntity
   */
  private ResponseEntity<?> excelDownloadProc(byte[] documentContent, String targetMonth) {
    HttpHeaders headers = new HttpHeaders();
    // xlsx形式のContent-Typeを設定
    headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    // ファイル名: EXCEL_FILE_NAME_YYYYMMDD.xlsx（ハイフン除去）
    String fileName = EXCEL_FILE_NAME + "_" + targetMonth.replace("-", "") + CommConstant.EXCEL_FILE_EXTENSION;
    headers.set("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    headers.setContentLength(documentContent.length);
    return new ResponseEntity<>(documentContent, headers, HttpStatus.OK);
  }

  @Transactional
  public void importBusServices(ImportBusServicesExcelUpload excel, List<String> msgList,
      MultipartFile file, String uploadFileName) throws IOException {
    List<StopSequenceNodeDto> stopSequenceList = new ArrayList<>();
    // 系統停留所重複チェック用HashMap（キー：改正ID+便番号、値：系統停留所IDのリスト）
    Map<String, List<String>> routeStopListMapForDupCheck = new HashMap<>();
    // 系統停留所の入力漏れチェック用HashMap（キー：改正ID+系統ID+便番号、値：系統停留所IDのリスト）
    Map<String, List<String>> routeStopListMapForRequiredCheck = new HashMap<>();

    ValidatorFactory factory =
        Validation.byProvider(HibernateValidator.class).configure().failFast(false)
            .buildValidatorFactory();
    Validator localValidator = factory.getValidator();

    // 1列ずつ検証する
    ImportBusServicesExcelDto rowDto = null;
    for (int i = 0; i < excel.getDataCount(); i++) {
      rowDto = excel.getData(i);
      java.util.Set<jakarta.validation.ConstraintViolation<ImportBusServicesExcelDto>> violations =
          localValidator.validate(rowDto);

      int rowNo = excel.getDataStartDataRow() + i; // 実際のExcel行番号
      if (!violations.isEmpty()) {
        for (jakarta.validation.ConstraintViolation<ImportBusServicesExcelDto> v : violations) {
          String resolved = v.getMessage();
          if (resolved != null && resolved.startsWith("{") && resolved.endsWith("}")) {
            String key = resolved.substring(1, resolved.length() - 1);
            resolved = messageSource.getMessage(key, null, Locale.JAPAN);
          }
          msgList.add("行" + rowNo + " " + resolved);
        }
      }

      // 到着時刻、出発時刻の整合性チェック（HHMMにマッチする場合のみ比較）
      String arr = rowDto.getBusStopArrivalTime();
      String dep = rowDto.getBusStopDepartureTime();
      if (isHHmm(arr) && isHHmm(dep)) {
        if (toMinutes(arr) > toMinutes(dep)) {
          msgList.add("行" + rowNo + " " + this.getMessage(
              "validation.busServiceImport.arrivalTime.lessThanOrEqualDepartureTime"));
        }
      }

      // 事業者権限チェック（行の事業者ID ≠ セッション事業者ID）
      if (ExObjectUtils.isNotBlank(rowDto.getBusinessCorpId()) && ExObjectUtils.isNotBlank(
          getUserSession().getBusinessCorpId()) && !ExObjectUtils.equals(rowDto.getBusinessCorpId(),
          getUserSession().getBusinessCorpId())) {
        msgList.add(
            "行" + rowNo + " " + this.getMessage("validation.busServiceImport.agency.mismatch"));
      }

      // 事業者存在チェック（削除フラグ=0・利用可否=1・事業者ID=EXCEL）
      String businessCorpId = rowDto.getBusinessCorpId();
      if (ExObjectUtils.isNotBlank(businessCorpId)) {
        boolean exist = this.existsBusinessActive(businessCorpId);
        if (!exist) {
          msgList.add(
              "行" + rowNo + " " + this.getMessage("validation.busServiceImport.agency.notFound"));
        }
      }

      // 便ダイヤ改正存在チェック（削除=0・開始日>システム日付・路線ID・改正ID）
      String daiyaRevisionId = rowDto.getDaiyaRevisionId();
      if (ExObjectUtils.isNotBlank(businessCorpId) && ExObjectUtils.isNotBlank(daiyaRevisionId)) {
        int exist =
            this.existsRevisionActiveAfterDate(businessCorpId, daiyaRevisionId, LocalDate.now());
        if (exist <= 0) {
          msgList.add("行" + rowNo + " " + this.getMessage(
              "validation.busServiceImport.daiyaRevision.notFound"));
        }
      }

      // 便番号重複チェック（便ダイヤ改正ID毎に）
      String busServiceTripId = rowDto.getBusServiceTripId();
      if (ExObjectUtils.isNotBlank(busServiceTripId) && ExObjectUtils.isNotBlank(
          daiyaRevisionId) && this.existsDuplicatedBusServiceTripId(daiyaRevisionId, busServiceTripId)) {
        msgList.add("行" + rowNo + " " + getMessage("validation.busServiceImport.serviceNo.duplicate"));
      }

      // 運行カレンダー存在チェック（削除=0・利用可否=1・事業者ID・カレンダーID）
      String calendarId = rowDto.getCalendarId();
      if (ExObjectUtils.isNotBlank(businessCorpId) && ExObjectUtils.isNotBlank(calendarId)) {
        int exist = this.existsCalendarActive(businessCorpId, calendarId);
        if (exist <= 0) {
          msgList.add("行" + rowNo + " " + this.getMessage(
              "validation.busServiceImport.calendar.notFound"));
        }
      }

      // 系統停留所存在チェック（削除=0・系統ID・系統停留所ID）
      String lineRouteId = null;
      Optional<Integer> stopSeqOpt = Optional.empty();
      if (ExObjectUtils.isNotBlank(daiyaRevisionId) && ExObjectUtils.isNotBlank(businessCorpId)) {
        List<FindLineRouteStopSequenceDto> busStopList =
            this.getLineRouteStopSequence(daiyaRevisionId, businessCorpId);
        if (ExObjectUtils.isEmpty(busStopList)) {
          msgList.add("行" + rowNo + " " + this.getMessage(
              "validation.busServiceImport.daiyaRevisionId.notFound"));
        } else {
          String targetBusStopId = rowDto.getLineRouteBusStopId();

          // 系統停留所IDに一致するレコードを検索して系統IDを取得
          FindLineRouteStopSequenceDto hit = busStopList.stream()
              .filter(dto -> ExObjectUtils.equals(dto.getLineRouteBusStopId(), targetBusStopId))
              .findFirst().orElse(null);

          if (hit == null) {
            msgList.add("行" + rowNo + " " + this.getMessage(
                "validation.busServiceImport.rosenBusStopId.notFound") + ExObjectUtils.toS(
                targetBusStopId, "") + "）");
          } else {
            lineRouteId = hit.getLineRouteId();
          }
        }
      }

      // 運行順序チェック
      String lineRouteBusStopId = rowDto.getLineRouteBusStopId();
      String busStopArrivalTime = rowDto.getBusStopArrivalTime();
      String busStopDepartureTime = rowDto.getBusStopDepartureTime();
      int busStopSeq = this.getBusStopSeqSequence(lineRouteBusStopId);

      // 分変換（HHmmのみ有効）
      Integer arrMinForSeq = isHHmm(busStopArrivalTime) ? toMinutes(busStopArrivalTime) : null;
      Integer depMinForSeq =
          isHHmm(busStopDepartureTime) ? toMinutes(busStopDepartureTime) : null;

      // 必要情報が揃っている場合のみ格納
      if (ExObjectUtils.isNotBlank(daiyaRevisionId) && ExObjectUtils.isNotBlank(
          lineRouteId) && ExObjectUtils.isNotBlank(busServiceTripId) && ExObjectUtils.isNotBlank(
          lineRouteBusStopId) && arrMinForSeq != null && depMinForSeq != null && busStopSeq > 0) {
        stopSequenceList.add(new StopSequenceNodeDto(daiyaRevisionId,         // 便ダイヤ改正ID
            lineRouteId,             // 系統ID
            busServiceTripId,        // 便番号
            lineRouteBusStopId,      // 系統停留所ID
            arrMinForSeq,            // 出発(分)
            depMinForSeq,            // 到着(分)
            busStopSeq,              // 運行順序
            rowNo                    // 行番号
        ));
      }

      // 運行順序チェック: 改正ID → 便番号 → 運行順序 でソートし、同一便内で
      // 「到着>=前行の到着」または「出発>=前行の出発」ならエラー
      if (!stopSequenceList.isEmpty()) {
        stopSequenceList.sort(
            Comparator.comparing(StopSequenceNodeDto::getDaiyaRevisionId, Comparator.nullsFirst(String::compareTo))
                .thenComparing(StopSequenceNodeDto::getBusServiceTripId, Comparator.nullsFirst(String::compareTo))
                .thenComparingInt(StopSequenceNodeDto::getBusStopSeq));

        StopSequenceNodeDto prev = null;
        for (StopSequenceNodeDto cur : stopSequenceList) {
          if (prev != null && ExObjectUtils.equals(prev.getDaiyaRevisionId(),
              cur.getDaiyaRevisionId()) && ExObjectUtils.equals(prev.getBusServiceTripId(),
              cur.getBusServiceTripId())) {

            if (cur.getBusStopArrivalTime() <= prev.getBusStopDepartureTime()) {
              msgList.add("行" + cur.getRowNo() + " " + this.getMessage(
                  "validation.busServiceImport.operationOrder.invalidInService"));
            }
          }
          prev = cur;
        }
      }

      // 系統停留所の重複チェック
      // キー作成（便ダイヤ改正ID＋便番号）
      String mapKeyForDupCheck =
          ExObjectUtils.toS(daiyaRevisionId, "") + "|" + ExObjectUtils.toS(busServiceTripId, "");
      // 値（系統停留所IDのリスト）を取得
      List<String> stopListForDupCheck = routeStopListMapForDupCheck.get(mapKeyForDupCheck);
      // リストが未作成なら作成して現在の系統停留所IDを追加
      if (ExObjectUtils.isNotBlank(lineRouteBusStopId)) {
        if (stopListForDupCheck == null) {
          stopListForDupCheck = new ArrayList<>();
          stopListForDupCheck.add(lineRouteBusStopId);
          routeStopListMapForDupCheck.put(mapKeyForDupCheck, stopListForDupCheck);
        } else {
          // 既存リストに含まれていなければ追加、含まれていればエラー
          if (stopListForDupCheck.contains(lineRouteBusStopId)) {
            msgList.add("行" + rowNo + " " + this.getMessage(
                "validation.busServiceImport.rosenBusStopId.duplicateInService") + lineRouteBusStopId + "）");
          } else {
            stopListForDupCheck.add(lineRouteBusStopId);
          }
        }
      }

      // 系統停留所の入力漏れチェック用HashMap作成
      // キー作成（便ダイヤ改正ID＋系統ID＋便番号）
      String mapKeyForRequiredCheck =
          ExObjectUtils.toS(daiyaRevisionId, "") + "|" + ExObjectUtils.toS(lineRouteId,
              "") + "|" + ExObjectUtils.toS(busServiceTripId, "");
      // 値（系統停留所IDのリスト）を取得
      List<String> stopListForRequiredCheck = routeStopListMapForRequiredCheck.get(mapKeyForRequiredCheck);
      // リストが未作成なら作成して現在の系統停留所IDを追加
      if (ExObjectUtils.isNotBlank(lineRouteBusStopId)) {
        if (stopListForRequiredCheck == null) {
          stopListForRequiredCheck = new ArrayList<>();
          stopListForRequiredCheck.add(lineRouteBusStopId);
          routeStopListMapForRequiredCheck.put(mapKeyForRequiredCheck, stopListForRequiredCheck);
        } else {
          if (!stopListForRequiredCheck.contains(lineRouteBusStopId)) {
            stopListForRequiredCheck.add(lineRouteBusStopId);
          }
        }
      }
    }

    // アップロードEXCELの最終行後: 系統停留所の入力漏れチェック
    if (!routeStopListMapForRequiredCheck.isEmpty()) {
      for (Map.Entry<String, List<String>> e : routeStopListMapForRequiredCheck.entrySet()) {
        String key = e.getKey();                 // 形式: revId|lineRouteId|tripId
        List<String> list = e.getValue();        // 当該便に入力された系統停留所IDのリスト
        String[] parts = key.split("\\|", -1);
        String kRev = parts.length > 0 ? parts[0] : "";
        String kLineRouteId = parts.length > 1 ? parts[1] : "";
        String kTrip = parts.length > 2 ? parts[2] : "";

        if (ExObjectUtils.isNotBlank(kLineRouteId)) {
          int dbCount = this.countActiveLineRouteBusStops(kLineRouteId);
          int excelCount = (list != null) ? list.size() : 0;
          if (dbCount != excelCount) {
            msgList.add(
                "改正ID=" + kRev + " 系統ID=" + kLineRouteId + " 便番号=" + kTrip + " の" + this.getMessage(
                    "validation.busServiceImport.rosenBusStopCount.inconsistent") + "（DB=" + dbCount + "件 / EXCEL=" + excelCount + "件）。" + this.getMessage(
                    "validation.busServiceImport.input.missingOrExtra"));
          }
        }
      }
    }

    // 2) データ登録

    // 2-1) 便データ登録（運行カレンダーの件数分）
    if (msgList.isEmpty()) {
      // Excel から便単位にユニーク化: (改正ID × カレンダーID × 便の一意ID)
      class TripKey {
        final String revId, calId, excelTripId;

        TripKey(String r, String c, String e) {
          this.revId = r;
          this.calId = c;
          this.excelTripId = e;
        }

        /**
         * TripKey の等価性判定。
         * <p>
         * 同一インスタンスは true。<br> 異種オブジェクトは false。<br> revId / calId / excelTripId
         * の全てが等しければ true。
         */
        @Override
        public boolean equals(Object o) {
          if (this == o)
            return true;
          if (!(o instanceof TripKey k))
            return false;
          return java.util.Objects.equals(revId, k.revId) && java.util.Objects.equals(calId,
              k.calId) && java.util.Objects.equals(excelTripId, k.excelTripId);
        }

        /**
         * equals と整合するハッシュコードを返します。
         * <p>
         * revId / calId / excelTripId をキーとして生成します。
         */
        @Override
        public int hashCode() {
          return java.util.Objects.hash(revId, calId, excelTripId);
        }
      }

      Map<TripKey, ImportBusServicesInsertDto> insertMap = new LinkedHashMap<>();
      try (ImportBusServicesExcelUpload insertExcel = new ImportBusServicesExcelUpload(file,
          uploadFileName)) {
        for (int i = 0; i < insertExcel.getDataCount(); i++) {
          ImportBusServicesExcelDto row = insertExcel.getData(i);

          String revId = row.getDaiyaRevisionId();
          String calId = row.getCalendarId();
          String excelTripId = ExObjectUtils.toS(row.getBusServiceTripId(), null); // Excel上の便ID

          if (ExObjectUtils.isBlank(revId) || ExObjectUtils.isBlank(
              calId) || ExObjectUtils.isBlank(excelTripId)) {
            // 必須不足はスキップ（検証段階で弾かれている想定）
            continue;
          }
          TripKey key = new TripKey(revId, calId, excelTripId);
          if (insertMap.containsKey(key)) {
            continue; // 同一便(同一カレンダー)は1回のみ登録
          }

          String daiyaRevisionId = row.getDaiyaRevisionId();
          String busServiceTripId = row.getBusServiceTripId();
          String busServiceName = row.getBusServiceName();
          String calendarId = row.getCalendarId(); // Excel上の便ID
          String lineRouteId = routeStopListMapForRequiredCheck.entrySet().stream()
              .filter((e) -> e.getValue().contains(row.getLineRouteBusStopId()))
              .map(e -> e.getKey().split("\\|")[1])
              .findFirst().orElse(null);

          ImportBusServicesInsertDto p = new ImportBusServicesInsertDto();
          p.setDaiyaRevisionId(Long.valueOf(daiyaRevisionId));
          p.setLineRouteId(Long.valueOf(lineRouteId));
          p.setBusServiceTripId(busServiceTripId);
          p.setBusServiceName(busServiceName);
          p.setCalendarId(Integer.valueOf(calendarId));
          insertMap.put(key, p);
        }
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
      if (!insertMap.isEmpty()) {
        this.registerTrips(new ArrayList<>(insertMap.values()),
            getUserSession().getUserName());
      }

      // 2-3) 便ダイヤ明細データを登録する（運行カレンダー毎の行数分）
      Map<TripKey, Long> tripIdMap = new HashMap<>();
      for (Map.Entry<TripKey, ImportBusServicesInsertDto> e : insertMap.entrySet()) {
        Long tripId = e.getValue().getBusServiceId();
        if (tripId != null) {
          tripIdMap.put(e.getKey(), tripId);
        }
      }

      // Excel全行を再走査して明細を作成
      List<ImportBusServicesDetailInsertDto> detailList =
          new ArrayList<>();
      List<String> businessCorpIds = new ArrayList<>();
      try (ImportBusServicesExcelUpload detailExcel = new ImportBusServicesExcelUpload(file,
          uploadFileName)) {
        for (int i = 0; i < detailExcel.getDataCount(); i++) {
          ImportBusServicesExcelDto row = detailExcel.getData(i);

          String revId = row.getDaiyaRevisionId();
          String calId = row.getCalendarId();
          String excelTripId = ExObjectUtils.toS(row.getBusServiceTripId(), null);
          businessCorpIds.add(row.getBusinessCorpId());
          if (ExObjectUtils.isBlank(revId) || ExObjectUtils.isBlank(
              calId) || ExObjectUtils.isBlank(excelTripId)) {
            continue; // 必須不足はスキップ
          }

          TripKey key = new TripKey(revId, calId, excelTripId);
          Long tripId = tripIdMap.get(key);
          if (tripId == null) {
            // 便が未登録（2-2でスキップ/不正） → 明細は作らない
            msgList.add(
                "行" + (detailExcel.getDataStartDataRow() + i) + " 対応する便が見つかりません（改正ID/カレンダーID/便番号）。");
            continue;
          }

          // 停留所ID（系統停留所ID）
          String lineRouteBusStopId = ExObjectUtils.toS(row.getLineRouteBusStopId(), null);

          // 到着/出発時刻（HHMM形式のみ設定）
          String arr = ExObjectUtils.toS(row.getBusStopArrivalTime(), null);
          String dep = ExObjectUtils.toS(row.getBusStopDepartureTime(), null);
          String arrHHmm = isHHmm(arr) ? arr : null;
          String depHHmm = isHHmm(dep) ? dep : null;

          // 明細DTO組み立て
          ImportBusServicesDetailInsertDto d =
              new ImportBusServicesDetailInsertDto();
          d.setDelFlag(0);                        // 削除フラグ = 0
          d.setBusServiceId(tripId);              // 便ID（FK）
          d.setLineRouteBusStopId(Integer.parseInt(lineRouteBusStopId)); // 系統停留所ID
          d.setBusStopArrivalTime(arrHHmm);       // 到着時刻（HHMM or null）
          d.setBusStopDepartureTime(depHHmm);     // 出発時刻（HHMM or null）
          d.setCreatedUser(getUserSession().getUserName());
          d.setCreatedAt(LocalDateTime.now());
          d.setUpdatedUser(getUserSession().getUserName());
          d.setUpdatedAt(LocalDateTime.now());
          detailList.add(d);
        }
      }

      if (!detailList.isEmpty() && msgList.isEmpty()) {
        this.registerTripDetails(detailList, getUserSession().getUserName());
      }

      // 日毎便ダイヤの再作成
      for (String d : businessCorpIds) {
        // String → Long へ安全変換（null/空/不正は null）
        Long businessCorpId = Long.valueOf(d);
        if (businessCorpId == null) {
          // 必要に応じてログ出力など
          continue;
        }
        List<Long> busServiceIds = insertMap.values().stream().mapToLong(
                ImportBusServicesInsertDto::getBusServiceId).boxed()
            .toList();
        List<BusServiceInfoDto> busServiceList =
            busServicesMapper.getBusServicesByBusServiceId(businessCorpId, busServiceIds);
        if (ExObjectUtils.isNotBlank(busServiceList)) {
          for (BusServiceInfoDto busServiceDto : busServiceList) {
            dailyBusDaiyasService.regenerateDailyBusDaiya(busServiceDto);
          }
        }
      }
    }
  }

  /**
   * Excelアップロード時の拡張子チェック
   *
   * @param file アップロードファイル
   * @return true:OK,false:NG
   */
  public boolean checkFileExtension(MultipartFile file) {
    return CommConstant.EXCEL_FILE_EXTENSION.equals(
        file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."))) ? true : false;
  }

  /** 事業者存在チェック（削除=0・利用=1・事業者ID） */
  public boolean existsBusinessActive(String businessCorpId) {
    return agenciesMapper.countBusinessActive(businessCorpId) > 0;
  }

  /** 改正存在チェック（削除=0・開始日 > baseDate・路線ID・改正ID） */
  public int existsRevisionActiveAfterDate(String businessCorpId, String daiyaRevisionId,
      LocalDate baseDate) {
    return daiyaRevisionsMapper.countRevisionActiveAfterDateByBusinessCorpId(businessCorpId,
        daiyaRevisionId, baseDate);
  }

  /** 運行カレンダー存在チェック（削除=0・利用=1・事業者ID・カレンダーID）  */
  public int existsCalendarActive(String businessCorpId, String calendarId) {
    return calendarsMapper.countCalendarActive(businessCorpId, calendarId);
  }

  /**
   * 便ダイヤ改正IDから便番号が重複しているかをチェック
   *
   * @param daiyaRevisionId 改正ID
   * @param busServiceTripId 便番号
   * @return 存在すればtrue、存在しなければfalse
   */
  public boolean existsDuplicatedBusServiceTripId(String daiyaRevisionId, String busServiceTripId) {
    return busServicesMapper.countBusServiceTripIdByRevisionId(daiyaRevisionId, busServiceTripId,
        null) > 0;
  }

  /** 系統停留所の運行順序取得（削除=0・系統ID・系統停留所ID） */
  public List<FindLineRouteStopSequenceDto> getLineRouteStopSequence(String daiyaRevisionId,
      String businessCorpId) {
    return lineRouteBusStopsMapper.findLineRouteStopSequence(daiyaRevisionId, businessCorpId);
  }

  /** 系統停留所IDに対応する「運行順序（停留所の並び順）」を取得します。 */
  public int getBusStopSeqSequence(String lineRouteBusStopId) {
    return busStopMapper.getBusStopSeqSequence(lineRouteBusStopId);
  }

  /** 指定した系統IDに紐づく「有効な系統停留所」の件数を取得します。 */
  public int countActiveLineRouteBusStops(String lineRouteId) {
    return lineRouteBusStopsMapper.countActiveStopsByLineRoute(lineRouteId);
  }

  /**
   * 2-1) 便データ登録
   */
  @Transactional
  public int registerTrips(List<ImportBusServicesInsertDto> list, String userName) {
    if (list == null || list.isEmpty())
      return 0;

    int cnt = 0;
    List<ImportBusServicesInsertDto> inserts = new ArrayList<>(list.size());
    for (ImportBusServicesInsertDto t : list) {
      ImportBusServicesInsertDto row = t;
      if (row.getDelFlag() == null) {
        row.setDelFlag(0);
      }
      //notnull臨時
      row.setCreatedUser(getUserSession().getUserName());
      //notnull臨時
      row.setUpdatedUser(getUserSession().getUserName());
      // 便ID採番(連番)
      cnt += busServicesMapper.insertBusServiceForImport(row);
    }
    return cnt;
  }


  /**
   * 2-3) 便ダイヤ明細登録
   */
  @Transactional
  public int registerTripDetails(List<ImportBusServicesDetailInsertDto> list, String userName) {
    if (list == null || list.isEmpty())
      return 0;

    List<ImportBusServicesDetailInsertDto> inserts = new ArrayList<>(list.size());
    for (ImportBusServicesDetailInsertDto d : list) {
      ImportBusServicesDetailInsertDto row = d;
      if (row.getDelFlag() == null) {
        row.setDelFlag(0);
      }
      row.setCreatedUser(userName);
      row.setUpdatedUser(userName);
      inserts.add(row);
    }
    if (inserts.isEmpty())
      return 0;
    return busDaiyasMapper.insertTripDetails(inserts);
  }

  /**
   * アップロード時バリデーション
   *
   * @param file 対象のファイル
   * @return true:OK false:NG
   */
  public boolean uploadFileValidationCheck(MultipartFile file) {
    // 登録するファイルの存在チェック
    if (ExObjectUtils.isBlank(file) || ExObjectUtils.isBlank(file.getOriginalFilename())) {
      return false;
    }
    // 登録するファイルの拡張子チェック
    if (!this.checkFileExtension(file)) {
      return false;
    }
    // フォームの値については入力チェックは不要
    return true;
  }

  /**
   * 文字列が時刻(HHMM)形式かを判定します。
   * <p>
   * 許容範囲: 00:00 ～ 23:59。<br> 入力が null の場合は false を返します。<br> 数字4桁以外や「24xx」などの不正値は一致しません。
   *
   * @param s 判定対象の文字列（例: "0930"）
   * @return true = HHMM 形式, false = それ以外
   */
  private boolean isHHmm(String s) {
    return s != null && java.util.regex.Pattern.matches("([01]\\d|2[0-3])[0-5]\\d", s);
  }

  /**
   * HHMM 形式の文字列を分に換算します。
   * <p>
   * 前提: 引数は HHMM 形式（事前に {@link #isHHmm(String)} で検証済みであること）。<br> 例: "0130" -> 90, "2305" -> 1385
   * <p>
   * 注意: フォーマット不正な文字列を渡すと NumberFormatException や StringIndexOutOfBoundsException が発生します。
   *
   * @param s HHMM 形式の文字列
   * @return 総分（0 以上）
   */
  private int toMinutes(String s) {
    int hh = Integer.parseInt(s.substring(0, 2));
    int mm = Integer.parseInt(s.substring(2, 4));
    return hh * 60 + mm;
  }
}
