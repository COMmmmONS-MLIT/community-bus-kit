package maint.doc;

import maint.dto.operation.importBusServices.ImportBusServicesExcelDto;
import maint.utils.ExObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 便ダイヤ登録エクセルアップロード
 *
 * @author jeon
 */
public class ImportBusServicesExcelUpload extends BaseExcel {

  /** データ行開始位置 */
  private final static int START_DATA_ROW_NO = 2;

  /** 事業者ID列 */
  private final static int BUSINESS_CORP_ID_COL = 1;

  /** ダイヤ改正ID列 */
  private final static int DAIYA_REVISION_ID_COL = 2;

  /** 便番号列 */
  private final static int BUS_SERVICE_TRIP_ID_COL = 3;

  /** 便名称列 */
  private final static int BUS_SERVICE_NAME_COL = 4;

  /** 運行カレンダーID列 */
  private final static int CALENDAR_ID_COL = 5;

  /** 系統停留所ID列 */
  private final static int LINE_ROUTE_BUS_STOP_ID_COL = 6;

  /** 到着時刻列 */
  private final static int BUS_STOP_ARRIVAL_TIME_COL = 7;

  /** 出発時刻列 */
  private final static int BUS_STOP_DEPARTURE_TIME_COL = 8;

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
  public ImportBusServicesExcelUpload(MultipartFile file, String uploadFileName)
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
      String updateDevision = getStringCellValue(currentRow, 2);
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
  public ImportBusServicesExcelDto getData(int index) {
    ImportBusServicesExcelDto data = new ImportBusServicesExcelDto();
    // index
    int currentRow = START_DATA_ROW_NO + index;

    // 基本情報
    data.setBusinessCorpId(getStringCellValue(currentRow, BUSINESS_CORP_ID_COL));
    data.setDaiyaRevisionId(getStringCellValue(currentRow, DAIYA_REVISION_ID_COL));
    data.setCalendarId(getStringCellValue(currentRow, CALENDAR_ID_COL));

    // 便情報（便 ID/行先/名称 等）
    data.setBusServiceTripId(getStringCellValue(currentRow, BUS_SERVICE_TRIP_ID_COL));
    data.setBusServiceName(getStringCellValue(currentRow, BUS_SERVICE_NAME_COL));

    // 系統停留所・時刻
    data.setLineRouteBusStopId(getStringCellValue(currentRow, LINE_ROUTE_BUS_STOP_ID_COL));
    data.setBusStopArrivalTime(getStringCellValue(currentRow, BUS_STOP_ARRIVAL_TIME_COL));
    data.setBusStopDepartureTime(getStringCellValue(currentRow, BUS_STOP_DEPARTURE_TIME_COL));

    return data;
  }


  /**
   * データ数取得
   *
   * @return データ数
   */
  public int getDataStartDataRow() {
    return ImportBusServicesExcelUpload.START_DATA_ROW_NO;
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
