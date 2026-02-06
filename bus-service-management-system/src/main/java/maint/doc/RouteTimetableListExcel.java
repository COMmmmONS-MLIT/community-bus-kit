package maint.doc;

import maint.dto.plan.RouteTimetableListDownloadDto;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.HashMap;
import java.util.Map;

/**
 * 路線時刻表出力
 *
 * @author 林竜生
 */
public class RouteTimetableListExcel extends BaseOriginMakeExcelSheet {

  private final XSSFWorkbook xssf;   // コピー元テンプレート保持
  private SXSSFWorkbook sxssf;       // 大容量ストリーミング用
  private Sheet activeSheet;

  public RouteTimetableListExcel(XSSFWorkbook workbook) {
    if (workbook == null) throw new IllegalArgumentException("Provided workbook cannot be null.");
    if (workbook.getNumberOfSheets() == 0) throw new IllegalStateException("テンプレートにシートが存在しません");
    this.xssf = workbook;
  }

  private final Map<String, XSSFSheet> sheetMap = new HashMap<>();

  /** データをプロットするシートを選択 */
  public void selectSheetFor(RouteTimetableListDownloadDto dto) {
    String key = dto.getRosenName() + "###" + dto.getCalendarType() + "###" + dto.getLineDetailServiceDivision();
    XSSFSheet s = sheetMap.get(key);
    if (s == null) throw new IllegalStateException("Sheet not found for key=" + key);
    this.activeSheet = s;
    super.setActiveSheet(this.activeSheet);
  }

  /** Excelテンプレートのシートを複製、シート命名 */
  public void createSheetFromTemplate(String rosenName, String calendarType, String lineDetailServiceDivision) {
    String sheetNameBase = "";
    if (rosenName != null && !rosenName.isBlank()) {
      String truncated = rosenName.length() > 10 ? rosenName.substring(0, 10) : rosenName;
      sheetNameBase += truncated;
    } else {
      sheetNameBase += "無題";
    }
    if (calendarType != null && !calendarType.isBlank()) {
      String truncated = calendarType.length() > 10 ? calendarType.substring(0, 10) : calendarType;
      sheetNameBase += "_" + truncated;
    }
    if (lineDetailServiceDivision != null && !lineDetailServiceDivision.isBlank()) {
      String division = lineDetailServiceDivision.equals("1") ? "往路" : "復路";
      sheetNameBase += "_" + division;
    }

    String safe = WorkbookUtil.createSafeSheetName(sheetNameBase);
    XSSFSheet cloned = xssf.cloneSheet(0);
    int idx = xssf.getSheetIndex(cloned);
    xssf.setSheetName(idx, safe);

    this.activeSheet = cloned;
    super.setActiveSheet(this.activeSheet);

    String key = rosenName + "###" + calendarType + "###" + lineDetailServiceDivision;
    sheetMap.put(key, cloned);
  }


  /** 現在のアクティブシートを返す（null セーフ） */
  public XSSFSheet getActiveSheet() {
    if (activeSheet == null) throw new IllegalStateException("call createSheetFromTemplate() first.");
    return (XSSFSheet) activeSheet;
  }

  /** 便ヘッダー（上段=系統名, 下段=便名）を書き出す */
  public void writeHeader(int baseRow, int col, String lineRouteName, String busServiceName) {
    XSSFSheet sheet = getActiveSheet();
    // baseRow-1 に系統名、baseRow に便名を書く（テンプレの行構成に合わせる）
    setCellDataBorder(baseRow - 1, col, lineRouteName, baseRow - 1, 0, sheet);
    setCellDataBorder(baseRow, col, busServiceName, baseRow - 1, 0, sheet);
  }

  /** 停留所名の A-B 結合と表示（行ラベル） */
  public void writeStopNameRow(int row, String busStopName) {
    XSSFSheet sheet = getActiveSheet();

    // セルが既にマージされているかチェック
    boolean alreadyMerged = false;
    for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
      CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
      if (mergedRegion.getFirstRow() == row && mergedRegion.getLastRow() == row &&
          mergedRegion.getFirstColumn() == 0 && mergedRegion.getLastColumn() == 1) {
        alreadyMerged = true;
        break;
      }
    }

    // マージされていない場合のみマージを実行
    if (!alreadyMerged) {
      sheet.addMergedRegion(new CellRangeAddress(row, row, 0, 1)); // A-B を結合
    }

    setCellDataBorder(row, 0, busStopName);
    setCellDataBorder(row, 1, null); // 停留所名の結合セルのデザイン調整
  }


  /** 路線情報（置換キーワード）設定 */
  public void setRouteTimetableStaticPlot(RouteTimetableListDownloadDto dto) {
    if (activeSheet == null) throw new IllegalStateException("call createSheetFromTemplate() first.");
    Map<String, String> kv = new HashMap<>();
    dto.convertExcelHashMap().forEach((k, v) -> kv.put(k, v == null ? "" : String.valueOf(v)));
    BaseOriginMakeExcelSheet.replaceKeywords((XSSFSheet) activeSheet, kv);
  }

  /** テンプレートシートの削除 */
  public void removeTemplateSheet() {
    if (xssf.getNumberOfSheets() > 0) xssf.removeSheetAt(0);
  }

  /** ストリーミング用SXSSFWorkbookの生成 */
  public SXSSFWorkbook buildStreamingWorkbook() {
    if (sxssf == null) sxssf = new SXSSFWorkbook(xssf, 100);
    return sxssf;
  }
}
