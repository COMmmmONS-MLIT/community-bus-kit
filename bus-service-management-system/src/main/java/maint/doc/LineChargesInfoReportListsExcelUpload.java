package maint.doc;

import maint.dto.operation.lineChargesInfoReport.LineChargesInfoReportListsExcelDto;
import maint.utils.ExObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 便ダイヤ登録エクセルアップロード
 *
 * @author jeon
 */
public class LineChargesInfoReportListsExcelUpload extends BaseExcel {

  /** データ行の開始位置 */
  public static final int START_DATA_ROW_NO = 2;

  /** 事業者ID */
  public static final int BUSINESS_CORP_ID_COL = 1;

  /** 運賃改定ID */
  public static final int CHARGE_REVISION_ID_COL = 2;

  /** 運賃区分 */
  public static final int LINE_CHARGE_DIVISION_COL = 3;

  /** 均一運賃額（円） */
  public static final int UNIFORM_CHARGE_COL = 4;

  /** 均一運賃額_ICカード（円） */
  public static final int UNIFORM_IC_PRICE_COL = 5;

  /** 均一運賃乗換可否 */
  public static final int UNIFORM_TRANSFER_TYPE_COL = 6;

  /** 支払いタイミング */
  public static final int PAYMENT_METHOD_COL = 7;

  /** 路線停留所IDFrom */
  public static final int ROSEN_BUS_STOP_ID_FROM_COL = 8;

  /** 路線停留所IDTo */
  public static final int ROSEN_BUS_STOP_ID_TO_COL = 9;

  /** 運賃（円） */
  public static final int LINE_CHARGE_COL = 10;

  /** ICカード運賃（円） */
  public static final int IC_PRICE_COL = 11;

  /** アップロードファイル名 */
  private final String uploadFileName;

  /** データ数 */
  private int dataCount;

  /**
   * コンストラクタ
   *
   * @param file           Excelファイル
   * @param uploadFileName ファイル名
   */
  public LineChargesInfoReportListsExcelUpload(MultipartFile file, String uploadFileName)
      throws IOException {
    super(file);
    this.uploadFileName = uploadFileName;
    setDataCount();
  }

  /**
   * ファイルの情報を元にデータ数をセット
   */
  private void setDataCount() {
    // データ数初期化
    this.dataCount = 0;
    for (int currentRow = START_DATA_ROW_NO; ; currentRow++) {
      // 更新区分が空欄ならデータなしとみなして終了する
      String updateDevision = getStringCellValue(currentRow, 1);
      if (ExObjectUtils.isBlank(updateDevision)) {
        break;
      }
      // 更新区分にデータがあればデータありと見なしてカウントアップ
      this.dataCount++;
    }
  }

  /**
   * 便ダイヤ登録EXCEL Dto取得
   *
   * @param index 要素番号（=実際の行数-先頭の行番号）
   * @return 便ダイヤ登録EXCEL Dto
   */
  public  LineChargesInfoReportListsExcelDto getData(int index) {
    LineChargesInfoReportListsExcelDto data = new LineChargesInfoReportListsExcelDto();
    int currentRow = START_DATA_ROW_NO + index;

    // 与えられた順序に従ってセル値を取得して DTO にセットする
    data.setBusinessCorpId(getStringCellValue(currentRow, BUSINESS_CORP_ID_COL));                 // 運賃ID
    data.setChargeRevisionId(getStringCellValue(currentRow, CHARGE_REVISION_ID_COL));         // 運賃改定ID
    data.setLineChargeDivision(getStringCellValue(currentRow, LINE_CHARGE_DIVISION_COL));     // 運賃区分
    data.setUniformCharge(getStringCellValue(currentRow, UNIFORM_CHARGE_COL));                // 均一運賃額（円）
    data.setUniformIcPrice(getStringCellValue(currentRow, UNIFORM_IC_PRICE_COL));             // 均一運賃額_ICカード（円）
    data.setUniformTransferType(getStringCellValue(currentRow, UNIFORM_TRANSFER_TYPE_COL));   // 均一運賃乗換可否
    data.setPaymentMethod(getStringCellValue(currentRow, PAYMENT_METHOD_COL));                // 支払いタイミング
    data.setRosenBusStopIdFrom(getStringCellValue(currentRow, ROSEN_BUS_STOP_ID_FROM_COL));   // 路線停留所IDFrom
    data.setRosenBusStopIdTo(getStringCellValue(currentRow, ROSEN_BUS_STOP_ID_TO_COL));       // 路線停留所IDTo
    data.setLineCharge(getStringCellValue(currentRow, LINE_CHARGE_COL));                      // 運賃（円）
    data.setIcPrice(getStringCellValue(currentRow, IC_PRICE_COL));                            // ICカード運賃（円）

    return data;
  }


  /**
   * データ数取得
   *
   * @return データ数
   */
  public int getDataStartDataRow() {
    return LineChargesInfoReportListsExcelUpload.START_DATA_ROW_NO;
  }

  /**
   * データ数取得
   *
   * @return データ数
   */
  public int getDataCount() {
    return this.dataCount;
  }

}
