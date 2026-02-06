package maint.service.operation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import maint.constants.CommConstant;
import maint.doc.LineChargesInfoReportListsExcelUpload;
import maint.dto.operation.lineChargesInfoReport.LineChargesInfoReportInsertDto;
import maint.dto.operation.lineChargesInfoReport.LineChargeDetailsDto;
import maint.dto.operation.lineChargesInfoReport.LineChargesInfoReportListsExcelDto;
import maint.entity.LineChargeDetailsInsertEntity;
import maint.entity.LineChargesInfoReportListsExcelEntity;
import maint.enums.LineChargeDivision;
import maint.enums.RideAlightDivision;
import maint.mapper.*;
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

/**
 * 運賃情報インポートSERVICE
 *
 * @author JEON
 */

@Service
public class LineChargesInfoReportListsService extends BaseService {

  /** テンプレートEXCELファイル */
  private final String TEMP_EXCEL_FILE = "lineChargesInfoReport";

  /** EXCELファイル名 */
  private final String EXCEL_FILE_NAME = "運賃情報インポート";

  /** ResourceLoader */
  @Autowired
  private ResourceLoader resourceLoader;

  /** 事業者MAPPER */
  @Autowired
  AgenciesMapper agenciesMapper;

  /** 運賃改定MAPPER */
  @Autowired
  ChargeRevisionsMapper chargeRevisionsMapper;

  /** 路線停留所Mapper */
  @Autowired
  LineBusStopsMapper lineBusStopsMapper;

  /** 運賃MAPPER */
  @Autowired
  LineChargesMapper lineChargesMapper;

  /** 運賃明細MAPPER */
  @Autowired
  LineChargeDetailsMapper lineChargeDetailsMapper;

  /**
   * 運賃情報インポート用テンプレートExcelをダウンロード
   *
   * @return Excelバイナリ
   * @throws Exception 生成中の例外は呼出し側へスロー
   */
  public ResponseEntity<?> getLineChargesInfoReportTemplate() throws Exception {
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
   * ファイルアップロード処理（運賃情報インポート）
   *
   * @param file アップロードファイル
   * @param uploadFileName 画面表示用ファイル名
   * @return エラーメッセージリスト（空なら正常）
   */
  @Transactional
  public List<String> processUploadExcel(MultipartFile file, String uploadFileName)
      throws IOException {

    List<String> msgList = new ArrayList<>();
    List<LineChargesInfoReportInsertDto> insertList = new ArrayList<>();

    try (LineChargesInfoReportListsExcelUpload excel =
        new LineChargesInfoReportListsExcelUpload(file, uploadFileName)) {

      // 中身が無い場合
      if (ExObjectUtils.isBlank(excel) || excel.getDataCount() <= 0) {
        msgList.add(getMessage("message.error.file.no.content.found"));
        return msgList;
      }

      // Validator 準備
      try (ValidatorFactory factory = Validation.byProvider(HibernateValidator.class)
          .configure()
          .failFast(false)
          .buildValidatorFactory()) {

        Validator localValidator = factory.getValidator();
        msgList = validateLineChargesInfo(excel, insertList, localValidator);
      }

      if (!msgList.isEmpty()) {
        return msgList;
      }

      // エラーが無ければ登録
      register(insertList);
    }

    return msgList;
  }


  /**
   * 入力チェック
   *
   * @param excel 便ダイヤ登録アップロード用Excel
   * @param lineChargesInfoReportinsertList 運賃情報インポート登録DTOリスト
   * @param localValidator localValidator
   * @return msgList
   */
  public List<String> validateLineChargesInfo(LineChargesInfoReportListsExcelUpload excel,
      List<LineChargesInfoReportInsertDto> lineChargesInfoReportinsertList,
      Validator localValidator) {

    List<String> msgList = new ArrayList<>();

    // クラスのフィールドまたはメソッドの先頭で宣言
    Map<String, List<String>> stopDuplicateMap = new HashMap<>();
    List<String> toStopList;
    // 1列ずつ検証する
    for (int i = 0; i < excel.getDataCount(); i++) {
      LineChargesInfoReportListsExcelDto rowDto = excel.getData(i);

      int rowNo = excel.getDataStartDataRow() + i;
      boolean isLastRow = (i == excel.getDataCount() - 1);

      // 1:均一運賃
      if (LineChargeDivision.FLAT_FARE.getValue().toString().equals(rowDto.getLineChargeDivision())) {
        Set<ConstraintViolation<LineChargesInfoReportListsExcelDto>> flatFareViolations =
            localValidator.validate(rowDto,
                LineChargesInfoReportListsExcelDto.FlatFareGroup.class);
        if (!flatFareViolations.isEmpty()) {
          for (ConstraintViolation<LineChargesInfoReportListsExcelDto> v : flatFareViolations) {
            String resolved = v.getMessage();
            if (resolved != null && resolved.startsWith("{") && resolved.endsWith("}")) {
              String key = resolved.substring(1, resolved.length() - 1);
              resolved = messageSource.getMessage(key, null, Locale.JAPAN);
            }
            msgList.add("行" + rowNo + " " + resolved);
          }
        }
        // 2:三角運賃
      } else if (LineChargeDivision.TRIANGULAR_FARE.getValue().toString().equals(rowDto.getLineChargeDivision())) {
        Set<ConstraintViolation<LineChargesInfoReportListsExcelDto>> triangularFareViolations =
            localValidator.validate(rowDto,
                LineChargesInfoReportListsExcelDto.TriangleFareGroup.class);
        if (!triangularFareViolations.isEmpty()) {
          for (ConstraintViolation<LineChargesInfoReportListsExcelDto> v : triangularFareViolations) {
            String resolved = v.getMessage();
            if (resolved != null && resolved.startsWith("{") && resolved.endsWith("}")) {
              String key = resolved.substring(1, resolved.length() - 1);
              resolved = messageSource.getMessage(key, null, Locale.JAPAN);
            }
            msgList.add("行" + rowNo + " " + resolved);
          }
        }
      }

      Set<ConstraintViolation<LineChargesInfoReportListsExcelDto>> violations =
          localValidator.validate(rowDto);
      if (!violations.isEmpty()) {
        for (ConstraintViolation<LineChargesInfoReportListsExcelDto> v : violations) {
          String resolved = v.getMessage();
          if (resolved != null && resolved.startsWith("{") && resolved.endsWith("}")) {
            String key = resolved.substring(1, resolved.length() - 1);
            resolved = messageSource.getMessage(key, null, Locale.JAPAN);
          }
          msgList.add("行" + rowNo + " " + resolved);
        }
      }

      if (!msgList.isEmpty()) {
        return msgList;
      }

      // 事業者権限チェック（行の事業者ID ≠ セッション事業者ID）
      if (ExObjectUtils.isNotBlank(rowDto.getBusinessCorpId()) && ExObjectUtils.isNotBlank(
          getUserSession().getBusinessCorpId()) && !ExObjectUtils.equals(
          rowDto.getBusinessCorpId(), getUserSession().getBusinessCorpId())) {
        msgList.add("行" + rowNo + " " + this.getMessage(
            "validation.linefares.excel.error.businesscorpid"));
        break;
      }

      // 事業者存在チェック（削除フラグ=0・利用可否=1・事業者ID=EXCEL）
      String businessCorpId = rowDto.getBusinessCorpId();
      if (ExObjectUtils.isNotBlank(businessCorpId)) {
        boolean exist = this.existsBusinessActive(businessCorpId);
        if (!exist) {
          msgList.add("行" + rowNo + " " + this.getMessage(
              "validation.linefares.excel.error.businesscorpid"));
          break;
        }
      }

      // 運賃改定存在チェック（削除フラグ=0・開始日>システム日付・運賃改定ID=EXCEL）
      String chargeRevisionId = rowDto.getChargeRevisionId();
      String businesscorpId = rowDto.getBusinessCorpId();
      if (ExObjectUtils.isNotBlank(chargeRevisionId) && ExObjectUtils.isNotBlank(
          businesscorpId)) {
        boolean exists =
            this.existsValidRevisionWithLine(chargeRevisionId,
                businesscorpId);
        if (!exists) {
          msgList.add("行" + rowNo + " " + this.getMessage(
              "validation.linefares.excel.error.chargerevisionid.withline"));
          break;
        }
      }

      List<Map<String, Object>> stopList = new ArrayList<>();

      // 運賃区分が「1：均一運賃」の場合はチェックをスキップ
      if ("2".equals(rowDto.getLineChargeDivision())) {
        chargeRevisionId = rowDto.getChargeRevisionId();
        String fromStopId = rowDto.getRosenBusStopIdFrom();
        String toStopId = rowDto.getRosenBusStopIdTo();

        if (ExObjectUtils.isNotBlank(chargeRevisionId) && ExObjectUtils.isNotBlank(
            businessCorpId) && ExObjectUtils.isNotBlank(fromStopId) && ExObjectUtils.isNotBlank(
            toStopId)) {

          stopList = this.getValidStopsForRevision(chargeRevisionId,
              businessCorpId);

          // ⑤ 路線停留所存在チェック
          boolean fromExists = stopList.stream().anyMatch(
              // stopListに路線停留所Fromが存在し、乗降区分が「降車」でない場合
              stop -> fromStopId.equals(String.valueOf(
                  stop.get("stop_id"))) && !RideAlightDivision.ALIGHT_ONLY.getValue().toString()
                  .equals(String.valueOf(stop.get("boarding_type"))));
          boolean toExists = stopList.stream().anyMatch(
              // stopListに路線停留所Toが存在し、乗降区分が「乗車」でない場合
              stop -> toStopId.equals(
                  String.valueOf(stop.get("stop_id"))) && !RideAlightDivision.RIDE_ONLY.getValue()
                  .toString().equals(String.valueOf(stop.get("boarding_type"))));

          if (!fromExists) {
            msgList.add("行" + rowNo + " " + this.getMessage(
                "validation.linefares.excel.error.stopid.from"));
            break;
          }

          if (!toExists) {
            msgList.add("行" + rowNo + " " + this.getMessage(
                "validation.linefares.excel.error.stopid.to"));
            break;
          }
          // ⑥ 運行順序チェック
          Integer fromOrder = stopList.stream()
              .filter(stop -> fromStopId.equals(String.valueOf(stop.get("stop_id"))))
              .map(stop -> (Integer) stop.get("sequence_no")).findFirst().orElse(null);

          Integer toOrder = stopList.stream()
              .filter(stop -> toStopId.equals(String.valueOf(stop.get("stop_id"))))
              .map(stop -> (Integer) stop.get("sequence_no")).findFirst().orElse(null);

          if (fromOrder != null && toOrder != null && fromOrder >= toOrder) {
            msgList.add("行" + rowNo + " " + this.getMessage(
                "validation.linefares.excel.error.operationorder"));
            break;
          }
        }

        // ⑦ 路線停留所の重複チェック
        String duplicateKey = chargeRevisionId + "_" + rowDto.getRosenBusStopIdFrom();
        String toStopIdOuter = rowDto.getRosenBusStopIdTo();

        toStopList = stopDuplicateMap.get(duplicateKey);

        if ("2".equals(rowDto.getLineChargeDivision())) {
          if (toStopList == null) {
            toStopList = new ArrayList<>();
            toStopList.add(toStopIdOuter);
            stopDuplicateMap.put(duplicateKey, toStopList);
          } else {
            if (toStopList.contains(toStopId)) {
              // 重複エラー
              msgList.add("行" + rowNo + " " + this.getMessage(
                  "validation.linefares.excel.error.duplicate.stopid"));
              break;
            } else {
              // 未登録なら追加
              toStopList.add(toStopId);
            }
          }

          // 1-2) 三角運賃かつ最終行の場合、路線停留所の入力漏れチェックを実施
          if (isLastRow) {
            for (Map.Entry<String, List<String>> entry : stopDuplicateMap.entrySet()) {
              String key = entry.getKey(); // chargeRevisionId_fromStopId
              List<String> toStopIds = entry.getValue();

              // キー分解
              String[] parts = key.split("_", 2);
              if (parts.length != 2)
                continue;

              String revisionId = parts[0];
              String fromStopIdKey = parts[1];

              // stopListからrosenIdを取得（fromStopIdに一致するもの）
              String rosenId = stopList.stream()
                  .filter(stop -> fromStopIdKey.equals(String.valueOf(stop.get("stop_id"))))
                  .map(stop -> String.valueOf(stop.get("rosen_id"))).findFirst().orElse(null);

              if (rosenId == null)
                continue;

              // DBから降車停留所候補を取得
              List<Map<String, Object>> expectedToStops =
                  this.getExpectedToStops(revisionId, rosenId,
                      fromStopIdKey);

              // 実際に登録されたTo停留所ID数と、DBから取得した候補数が一致しない場合はエラー
              if (expectedToStops.size() != toStopIds.size()) {
                msgList.add("行" + rowNo + " " + this.getMessage(
                    "validation.linefares.excel.error.missing.stopid.to"));
                break;
              }
            }
          }
        }
      }

      if (!msgList.isEmpty()) {
        return msgList;
      }

      // 運賃データ作成
      LineChargesInfoReportInsertDto lineChargeDto = lineChargesInfoReportinsertList.stream()
          .filter(dto -> dto.getChargeRevisionId().equals(rowDto.getChargeRevisionId())).findFirst()
          .orElse(new LineChargesInfoReportInsertDto());

      // 三角運賃の場合、運賃明細の項目をセット
      if (LineChargeDivision.TRIANGULAR_FARE.getValue().toString().equals(rowDto.getLineChargeDivision())) {
        LineChargeDetailsDto detailDto = new LineChargeDetailsDto();
        detailDto.setLineChargeId(lineChargeDto.getLineChargeId());
        detailDto.setRosenBusStopIdFrom(ExObjectUtils.toI(rowDto.getRosenBusStopIdFrom()));
        detailDto.setRosenBusStopIdTo(ExObjectUtils.toI(rowDto.getRosenBusStopIdTo()));
        detailDto.setLineCharge(ExObjectUtils.toI(rowDto.getLineCharge()));
        detailDto.setIcPrice(ExObjectUtils.toI(rowDto.getIcPrice()));
        lineChargeDto.addDetails(detailDto);
      }
      // 運賃マスタの項目をセット
      if (lineChargeDto.getChargeRevisionId() == null) {
        lineChargeDto.setChargeRevisionId(rowDto.getChargeRevisionId());
        lineChargeDto.setLineChargeDivision(rowDto.getLineChargeDivision());
        lineChargeDto.setPaymentMethod(rowDto.getPaymentMethod());
        // 均一運賃のみの項目をセット
        if (LineChargeDivision.FLAT_FARE.getValue().toString().equals(rowDto.getLineChargeDivision())) {
          lineChargeDto.setUniformCharge(ExObjectUtils.toI(rowDto.getUniformCharge()));
          lineChargeDto.setUniformIcPrice(ExObjectUtils.toI(rowDto.getUniformIcPrice()));
          lineChargeDto.setUniformTransferType(rowDto.getUniformTransferType());
        }
        lineChargesInfoReportinsertList.add(lineChargeDto);
      }
    }
    return msgList;
  }

  /**
   * 登録処理
   *
   * @param lineChargesInfoReportinsertList 運賃情報インポート登録DTOリスト
   */
  public void register(List<LineChargesInfoReportInsertDto> lineChargesInfoReportinsertList) {
    for (int i = 0; i < lineChargesInfoReportinsertList.size(); i++) {
      LineChargesInfoReportInsertDto dto = lineChargesInfoReportinsertList.get(i);
      this.insertLineCharge(dto);
      for (int j = 0; j < dto.getDetails().size(); j++) {
        LineChargeDetailsDto detailDto = dto.getDetails().get(j);
        detailDto.setLineChargeId(dto.getLineChargeId());
        this.insertLineChargeDetail(detailDto);
      }
    }
  }

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
   * 運賃改定IDと事業者IDに基づいて有効な運賃改定が存在するかをチェックする
   *
   * @param chargeRevisionId 運賃改定ID
   * @param businessCorpId 事業者ID
   * @return 存在すれば true、存在しなければ false
   */
  public boolean existsValidRevisionWithLine(String chargeRevisionId, String businessCorpId) {
    return chargeRevisionsMapper.existsValidRevisionWithLine(chargeRevisionId, businessCorpId);
  }

  /**
   * 運賃改定IDと事業者IDに基づいて有効な停留所一覧を取得する
   *
   * @param chargeRevisionId 運賃改定ID
   * @param businessCorpId 事業者ID
   * @return 有効な停留所のリスト
   */
  public List<Map<String, Object>> getValidStopsForRevision(String chargeRevisionId,
      String businessCorpId) {
    return lineBusStopsMapper.selectValidStopsForRevision(chargeRevisionId, businessCorpId);
  }

  /**
   * 三角運賃の最終行チェック用：乗車停留所に対応する降車候補停留所を取得
   *
   * @param chargeRevisionId 運賃改定ID
   * @param rosenId 路線ID
   * @param fromStopId 乗車停留所ID
   * @return 降車候補停留所のリスト
   */
  public List<Map<String, Object>> getExpectedToStops(String chargeRevisionId, String rosenId,
      String fromStopId) {
    return lineBusStopsMapper.getExpectedToStops(chargeRevisionId, rosenId, fromStopId);
  }

  /**
   * 運賃情報を登録する
   * @param dto 運賃情報DTO
   * @return 登録された運賃ID
   */
  public Integer insertLineCharge(LineChargesInfoReportInsertDto dto) {
    LineChargesInfoReportListsExcelEntity entity = new LineChargesInfoReportListsExcelEntity(dto);
    lineChargesMapper.insertLineCharge(entity);
    dto.setLineChargeId(entity.getLineChargeId());
    return entity.getLineChargeId();
  }

  /**
   * 運賃明細情報を登録する
   * @param dto 運賃明細DTO
   */
  public void insertLineChargeDetail(LineChargeDetailsDto dto) {
    LineChargeDetailsInsertEntity entity = new LineChargeDetailsInsertEntity(dto);
    lineChargeDetailsMapper.insertLineChargeDetail(entity);
  }

}

