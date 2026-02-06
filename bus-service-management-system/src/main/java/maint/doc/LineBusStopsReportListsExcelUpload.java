package maint.doc;

import maint.dto.operation.lineBusStopsReport.LineBusStopsReportListsExcelDto;
import maint.utils.ExObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 停留所情報インポートエクセルアップロード
 *
 * @author JEON
 */
public class LineBusStopsReportListsExcelUpload extends BaseExcel {

  /** データ行の開始位置 */
  public static final int START_DATA_ROW_NO = 2;

  /** 事業者ID列 */
  public static final int BUSINESS_CORP_ID_COL = 1;

  /** 停留所コード列 */
  public static final int BUS_STOP_CODE_COL = 2;

  /** 停留所・標柱区分列 */
  public static final int BUS_STOP_TYPE_COL = 3;

  /** 停留所名称列 */
  public static final int BUS_STOP_NAME_COL = 4;

  /** 停留所名称 かな列 */
  public static final int BUS_STOP_KANA_NAME_COL = 5;

  /** 停留所名称 英語列 */
  public static final int BUS_STOP_EN_NAME_COL = 6;

  /** 緯度・経度 */
  public static final int SIGNPOST_PLACE_COL = 7;

  /** 停留所URL列 */
  public static final int BUS_STOP_URL_COL = 8;

  /** 屋内バスターミナルフラグ列 */
  public static final int INDOOR_BUS_TERMINAL_FLAG_COL = 9;

  /** 標柱枝番列 */
  public static final int SIGNPOST_BRANCH_NUMBER_COL = 10;

  /** 標柱番号列 */
  public static final int SIGNPOST_CODE_COL = 11;

  /** 標柱名称列 */
  public static final int SIGNPOST_NAME_COL = 12;

  /** 標柱名称 かな列 */
  public static final int SIGNPOST_KANA_NAME_COL = 13;

  /** 標柱名称 英語列 */
  public static final int SIGNPOST_EN_NAME_COL = 14;

  /** 標柱副名称列 */
  public static final int SIGNPOST_SUB_NAME_COL = 15;

  /** 標柱住所列 */
  public static final int ADDRESS_COL = 16;

  /** 標柱地先名列 */
  public static final int PLACE_NAME_COL = 17;

  /** 標柱付加情報列 */
  public static final int SIGNPOST_DESC_COL = 18;

  /** 標柱URL列 */
  public static final int SIGNPOST_URL_COL = 19;

  /** のりば情報列 */
  public static final int PLATFORM_CODE_COL = 20;

  /** 車いす利用区分列 */
  public static final int WHEELCHAIR_ACCESSIBLE_TYPE_COL = 21;

  /** 到着判定緯度、経度 */
  public static final int CHECK_ARRIVAL_PLACE_COL = 22;

  /** 到着判定半径 */
  public static final int CHECK_ARRIVAL_RADIUS_COL = 23;




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
  public LineBusStopsReportListsExcelUpload(MultipartFile file, String uploadFileName)
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
  public  LineBusStopsReportListsExcelDto getData(int index) {
    LineBusStopsReportListsExcelDto data = new LineBusStopsReportListsExcelDto();
    int currentRow = START_DATA_ROW_NO + index;

    // 与えられた順序に従ってセル値を取得して DTO にセットする
    data.setBusinessCorpId(getStringCellValue(currentRow, BUSINESS_CORP_ID_COL));                       // 事業者ID
    data.setBusStopCode(getStringCellValue(currentRow, BUS_STOP_CODE_COL));                             // 停留所コード
    data.setBusStopType(getStringCellValue(currentRow, BUS_STOP_TYPE_COL));                             // 停留所・標柱区分
    data.setBusStopName(getStringCellValue(currentRow, BUS_STOP_NAME_COL));                             // 停留所名称
    data.setBusStopKanaName(getStringCellValue(currentRow, BUS_STOP_KANA_NAME_COL));                    // 停留所名称 かな
    data.setBusStopEnName(getStringCellValue(currentRow, BUS_STOP_EN_NAME_COL));                        // 停留所名称 英語
    data.setLatitudeLongitude(getStringCellValue(currentRow, SIGNPOST_PLACE_COL));                      // 緯度経度
    data.setBusStopUrl(getStringCellValue(currentRow, BUS_STOP_URL_COL));                               // 停留所URL
    data.setIndoorBusTerminalFlag(getStringCellValue(currentRow, INDOOR_BUS_TERMINAL_FLAG_COL));        // 屋内バスターミナルフラグ
    data.setSignpostBranchNumber(getStringCellValue(currentRow, SIGNPOST_BRANCH_NUMBER_COL));           // 標柱枝番
    data.setSignpostCode(getStringCellValue(currentRow, SIGNPOST_CODE_COL));                            // 標柱番号
    data.setSignpostName(getStringCellValue(currentRow, SIGNPOST_NAME_COL));                            // 標柱名称
    data.setSignpostKanaName(getStringCellValue(currentRow, SIGNPOST_KANA_NAME_COL));                   // 標柱名称 かな
    data.setSignpostEnName(getStringCellValue(currentRow, SIGNPOST_EN_NAME_COL));                       // 標柱名称 英語
    data.setSignpostSubName(getStringCellValue(currentRow, SIGNPOST_SUB_NAME_COL));                     // 標柱副名称
    data.setAddress(getStringCellValue(currentRow, ADDRESS_COL));                                       // 標柱住所
    data.setPlaceName(getStringCellValue(currentRow, PLACE_NAME_COL));                                  // 標柱地先名
    data.setSignpostDesc(getStringCellValue(currentRow, SIGNPOST_DESC_COL));                            // 標柱付加情報
    data.setSignpostUrl(getStringCellValue(currentRow, SIGNPOST_URL_COL));                              // 標柱URL
    data.setPlatformCode(getStringCellValue(currentRow, PLATFORM_CODE_COL));                            // のりば情報
    data.setWheelchairAccessibleType(getStringCellValue(currentRow, WHEELCHAIR_ACCESSIBLE_TYPE_COL));   // 車いす利用区分
    data.setArrivalPoint(getStringCellValue(currentRow, CHECK_ARRIVAL_PLACE_COL));                      // 到着判定緯度、経度
    data.setArrivalPointRadius(getStringCellValue(currentRow, CHECK_ARRIVAL_RADIUS_COL));               // 到着判定半径

    return data;
  }


  /**
   * データ数取得
   *
   * @return データ数
   */
  public int getDataStartDataRow() {
    return LineBusStopsReportListsExcelUpload.START_DATA_ROW_NO;
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
