package maint.service.operation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import maint.constants.CommConstant;
import maint.doc.LineBusStopsReportListsExcelUpload;
import maint.dto.operation.lineBusStopsReport.LineBusStopsReportInsertDto;
import maint.dto.operation.lineBusStopsReport.LineBusStopsReportListsExcelDto;
import maint.dto.operation.lineBusStopsReport.SignpostInsertDto;
import maint.entity.LineBusStopsReportListsExcelEntity;
import maint.entity.SignpostInsertEntity;
import maint.enums.BusStopType;
import maint.enums.UsageDivision;
import maint.mapper.AgenciesMapper;
import maint.mapper.BusStopMapper;
import maint.mapper.SignpostsLinkMapper;
import maint.service.BaseService;
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
import java.util.*;
import static maint.utils.date.ExDateUtils.getCurrentDate;

/**
 * 停留所情報インポートSERVICE
 *
 * @author JEON
 */

@Service
public class LineBusStopsReportListsService extends BaseService {

  /** テンプレートEXCELファイル */
  private final String TEMP_EXCEL_FILE = "lineBusStopsReport";

  /** EXCELファイル名 */
  private final String EXCEL_FILE_NAME = "停留所情報インポート";

  /** ResourceLoader */
  @Autowired
  private ResourceLoader resourceLoader;

  /** 事業者MAPPER */
  @Autowired
  AgenciesMapper agenciesMapper;

  /** 停留所MAPPER */
  @Autowired
  BusStopMapper busStopMapper;

  /** 標柱MAPPER */
  @Autowired
  SignpostsLinkMapper signpostsMapper;

  /**
   * 指定された事業者IDに対応する事業者が有効かどうかを判定する。
   *
   * @param businessCorpId 事業者ID
   * @return true：有効な事業者が存在する / false：存在しない
   */
  public boolean existsBusinessActive(String businessCorpId) {
    return agenciesMapper.countBusinessActive(businessCorpId) > 0;
  }

  /**
   * Excel行データに対して、停留所コードの重複チェックを行う。
   *
   * @param rowDto Excelから読み込んだ1行分の停留所データ
   * @return 重複件数
   */
  public int validateDuplicateForRow(LineBusStopsReportListsExcelDto rowDto) {
    String stopCode = rowDto.getBusStopCode();
    String excelStart = getCurrentDate();
    String excelEnd = "9999-12-31";
    return busStopMapper.countDuplicateStopCode(stopCode, excelStart, excelEnd);
  }

  /**
   * バリデーションメッセージの表示順を制御するためのフィールド順マップ
   */
  private static final Map<String, Integer> FIELD_ORDER;
  static {
    LinkedHashMap<String, Integer> m = new LinkedHashMap<>();
    int idx = 1;

    m.put("businessCorpId", idx++);
    m.put("busStopCode", idx++);
    m.put("busStopType", idx++);
    m.put("busStopName", idx++);
    m.put("busStopKanaName", idx++);
    m.put("busStopEnName", idx++);
    m.put("busStopUrl", idx++);
    m.put("indoorBusTerminalFlag", idx++);
    m.put("latitudeLongitude", idx++);

    m.put("signpostBranchNumber", idx++);
    m.put("signpostCode", idx++);
    m.put("signpostName", idx++);
    m.put("signpostKanaName", idx++);
    m.put("signpostEnName", idx++);
    m.put("signpostSubName", idx++);
    m.put("address", idx++);
    m.put("placeName", idx++);
    m.put("signpostDesc", idx++);
    m.put("signpostUrl", idx++);
    m.put("platformCode", idx++);
    m.put("wheelchairAccessibleType", idx++);

    FIELD_ORDER = Collections.unmodifiableMap(m);
  }

  /**
   * 任意のDTOを対象に指定グループでバリデーションを行い、
   * 発生したエラーをメッセージリストへ追加
   *
   * @param validator Validatorインスタンス
   * @param targetDto バリデーション対象DTO
   * @param rowNo Excel上の行番号
   * @param msgList エラーメッセージリスト
   * @param groups 実行するバリデーショングループ
   * @param <T> DTO型
   */
  @SafeVarargs
  private final <T> void validateAndAddMessagesGeneric(
      Validator validator,
      T targetDto,
      int rowNo,
      List<String> msgList,
      Class<?>... groups
  ) {
    Set<ConstraintViolation<T>> violations = validator.validate(targetDto, groups);

    if (!violations.isEmpty()) {
      // 登録項目順でソート
      List<ConstraintViolation<T>> sorted = new ArrayList<>(violations);

      sorted.sort(
          Comparator
              .comparingInt((ConstraintViolation<T> v) -> {
                String fieldName = v.getPropertyPath().toString();
                return FIELD_ORDER.getOrDefault(fieldName, Integer.MAX_VALUE);
              })
              .thenComparing(ConstraintViolation::getMessage)
      );

      for (ConstraintViolation<T> v : sorted) {
        String resolved = v.getMessage();
        if (resolved != null && resolved.startsWith("{") && resolved.endsWith("}")) {
          String key = resolved.substring(1, resolved.length() - 1);
          resolved = messageSource.getMessage(key, null, Locale.JAPAN);
        }
        msgList.add("行" + rowNo + " " + resolved);
      }
    }
  }

  /**
   * 停留所情報インポート用テンプレートExcelをダウンロード
   *
   * @return Excelバイナリ
   * @throws Exception 生成中の例外は呼出し側へスロー
   */
  public ResponseEntity<?> getLineBusStopReportTemplate() throws Exception {
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

  /**
   * ファイルアップロード処理
   *
   * @param file
   * @param uploadFileName
   * @return msgList
   * @throws IOException
   */
  @Transactional
  public List<String> processUploadExcel(MultipartFile file, String uploadFileName)
      throws IOException {
    List<String> msgList = new ArrayList<>();
    List<LineBusStopsReportInsertDto> LineBusStopsReportinsertList = new ArrayList<>();
    List<SignpostInsertDto> SignpostInsertList = new ArrayList<>();

    try (LineBusStopsReportListsExcelUpload excel = new LineBusStopsReportListsExcelUpload(file,
        uploadFileName)) {

      if (excel.getDataCount() == 0) {
        String msg = this.getMessage("validation.linebusstops.report.template.empty");
        msgList.add(msg);
        return msgList;
      }

      Validator localValidator;
      try (ValidatorFactory factory = Validation.byProvider(HibernateValidator.class).configure()
          .failFast(false).buildValidatorFactory()) {
        localValidator = factory.getValidator();
      }

      // 1列ずつ検証する
      for (int i = 0; i < excel.getDataCount(); i++) {
        LineBusStopsReportListsExcelDto rowDto = excel.getData(i);
        int rowNo = excel.getDataStartDataRow() + i;

        // Default（必須） グループ
        validateAndAddMessagesGeneric(localValidator, rowDto, rowNo, msgList,
            LineBusStopsReportListsExcelDto.DefaultGroupSequence.class);

        // 停留所（必須） or 標柱（必須）ごとのグループ
        if (BusStopType.BUS_STOP.getValue().toString().equals(rowDto.getBusStopType())) {
          validateAndAddMessagesGeneric(localValidator, rowDto, rowNo, msgList,
              LineBusStopsReportListsExcelDto.StopGroupSequence.class);
        } else if (BusStopType.ROAD_SIGN.getValue().toString().equals(rowDto.getBusStopType())) {
          validateAndAddMessagesGeneric(localValidator, rowDto, rowNo, msgList,
              LineBusStopsReportListsExcelDto.SignpostGroupSequence.class);
        }

        if (!msgList.isEmpty()) {
          Map<String, Object> okBody = new HashMap<>();
          okBody.put("success", false);
          okBody.put("errors", msgList);
          okBody.put("redirect", true);
          return msgList;
        }

        // 事業者権限チェック（行の事業者ID ≠ セッション事業者ID）
        if (ExObjectUtils.isNotBlank(rowDto.getBusinessCorpId()) && ExObjectUtils.isNotBlank(
            getUserSession().getBusinessCorpId()) && !ExObjectUtils.equals(
            rowDto.getBusinessCorpId(), getUserSession().getBusinessCorpId())) {
          msgList.add("行" + rowNo + " " + this.getMessage(
              "validation.signposts.excel.error.businesscorpid"));
          break;
        }

        // 事業者存在チェック（削除フラグ=0・利用可否=1・事業者ID=EXCEL）
        String businessCorpId = rowDto.getBusinessCorpId();
        if (ExObjectUtils.isNotBlank(businessCorpId)) {
          boolean exist = this.existsBusinessActive(businessCorpId);
          if (!exist) {
            msgList.add("行" + rowNo + " " + this.getMessage(
                "validation.signposts.excel.error.businesscorpid"));
            break;
          }
        }

        // 停留所コード重複チェック（停留所の場合のみ）
        if (ExObjectUtils.isNotBlank(businessCorpId) && BusStopType.BUS_STOP.getValue().toString()
            .equals(rowDto.getBusStopType())) {
          if (this.validateDuplicateForRow(rowDto) != 0) {
            msgList.add("行" + rowNo + " " + this.getMessage(
                "validation.signposts.excel.error.busstopcode"));
            break;
          }
        }

        if (BusStopType.BUS_STOP.getValue().toString().equals(rowDto.getBusStopType())) {
          LineBusStopsReportInsertDto insertDto = new LineBusStopsReportInsertDto();

          insertDto.setBusinessCorpId(getUserSession().getBusinessCorpId());
          insertDto.setBusStopCode(rowDto.getBusStopCode());
          insertDto.setServiceStartDate(getCurrentDate());
          insertDto.setServiceEndDate("9999-12-31");
          insertDto.setBusStopName(rowDto.getBusStopName());
          insertDto.setBusStopKanaName(rowDto.getBusStopKanaName());
          insertDto.setBusStopEnName(rowDto.getBusStopEnName());
          insertDto.setLatitudeLongitude(rowDto.getLatitudeLongitude());
          insertDto.setBusStopUrl(rowDto.getBusStopUrl());
          insertDto.setIndoorBusTerminalFlag(rowDto.getIndoorBusTerminalFlag());
          insertDto.setBusStopType(BusStopType.BUS_STOP.getValue());

          Integer busStopId = this.insertLineBusStop(insertDto);
          insertDto.setBusStopId("" + busStopId);

          LineBusStopsReportinsertList.add(insertDto);

        } else if (BusStopType.ROAD_SIGN.getValue().toString().equals(rowDto.getBusStopType())) {

          SignpostInsertDto insertDto = new SignpostInsertDto();

          // 固定値（仕様により固定）
          insertDto.setDelFlag(0);                         // 削除フラグ：0固定
          insertDto.setBusStopType(
              BusStopType.ROAD_SIGN.getValue().shortValue());  // 停留所・標柱区分：0固定（標柱）
          insertDto.setAvailableFlag(
              UsageDivision.ACTIVE.getValue().shortValue()); // 利用可否区分：1固定（利用可能）

          // Excel項目（rowDtoから取得）        // 停留所ID
          insertDto.setSignpostBranchNumber(rowDto.getSignpostBranchNumber());      // 標柱枝番
          insertDto.setSignpostCode(rowDto.getSignpostCode());                      // 標柱番号
          insertDto.setSignpostName(rowDto.getSignpostName());                      // 標柱名称
          insertDto.setSignpostKanaName(rowDto.getSignpostKanaName());              // 標柱名称 かな
          insertDto.setSignpostEnName(rowDto.getSignpostEnName());                  // 標柱名称 英語
          insertDto.setSignpostSubName(rowDto.getSignpostSubName());                // 標柱副名称
          insertDto.setAddress(rowDto.getAddress());                                // 標柱住所
          insertDto.setPlaceName(rowDto.getPlaceName());                            // 標柱地先名
          insertDto.setSignpostDesc(rowDto.getSignpostDesc());                      // 標柱付加情報
          insertDto.setLatitudeLongitude(rowDto.getLatitudeLongitude());            // 緯度・経度（文字列）
          insertDto.setSignpostUrl(rowDto.getSignpostUrl());                        // 標柱URL
          insertDto.setPlatformCode(rowDto.getPlatformCode());                      // のりば情報
          insertDto.setWheelchairAccessibleType(
              ExObjectUtils.toShort(rowDto.getWheelchairAccessibleType()));         // 車いす利用区分
          insertDto.setArrivalPoint(rowDto.getArrivalPoint());                      //　到着判定緯度、経度
          insertDto.setArrivalPointRadius(
              ExObjectUtils.toI(rowDto.getArrivalPointRadius()));                   //　到着判定半径
          insertDto.setBusStopCode(rowDto.getBusStopCode());                        //　停留所コード
          SignpostInsertList.add(insertDto);

        }
      }
    }

    for (SignpostInsertDto iter : SignpostInsertList) {
      for (LineBusStopsReportInsertDto compare : LineBusStopsReportinsertList) {
        if (compare.getBusStopCode().equals(iter.getBusStopCode())) {
          iter.setBusStopId(compare.getBusStopId());
          this.insertSignpost(iter);
        }
      }
    }

    return msgList;
  }

  /**
   * 停留所情報を登録する。
   *
   * @param lineBusStop 登録対象の停留所情報
   */
  @Transactional
  public Integer insertLineBusStop(LineBusStopsReportInsertDto lineBusStop) {
    LineBusStopsReportListsExcelEntity entity = new LineBusStopsReportListsExcelEntity(lineBusStop);
    busStopMapper.insertLineBusStop(entity);
    return entity.getBusStopId();
  }

  /**
   * 標柱情報をデータベースに登録する。
   *
   * @param insertDto 登録対象の標柱情報（SignpostInsertDto）
   */
  @Transactional
  public void insertSignpost(SignpostInsertDto insertDto) {
    SignpostInsertEntity signpostInsertEntity = new SignpostInsertEntity(insertDto);
    signpostsMapper.insertSignpost(signpostInsertEntity);
  }
}

