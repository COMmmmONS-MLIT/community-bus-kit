package maint.doc;

import maint.dto.document.StopMapListDownloadDto;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.HashMap;
import java.util.Map;

/**
 * 停留所付近図出力
 */
public class StopMapListExcel extends BaseOriginMakeExcelSheet {

  private final XSSFWorkbook xssf;   // コピー元テンプレート保持
  private SXSSFWorkbook sxssf; // 最後に作る
  private Sheet activeSheet;

  /**
   * コンストラクタ
   * @param workbook EXCELテンプレート
   */
  public StopMapListExcel(XSSFWorkbook workbook) {
    if (workbook == null) throw new IllegalArgumentException("Provided workbook cannot be null.");
    if (workbook.getNumberOfSheets() == 0) throw new IllegalStateException("テンプレートにシートが存在しません");
    this.xssf = workbook;
  }

  /**
   * Excelテンプレートのシートを複製、シート命名
   * @param busStopName   停留所名
   * @param outSignpostId 往路標柱ID
   * @param inSignpostId  復路標柱ID
   */
  public void createSheetFromTemplate(String busStopName, Long outSignpostId, Long inSignpostId) {
    String sheetNameBase = "";

    // 停留所名
    if (busStopName != null && !busStopName.isBlank()) {
      String truncated = busStopName.length() > 8 ? busStopName.substring(0, 8) : busStopName;
      sheetNameBase += truncated;
    } else {
      sheetNameBase += "無題";
    }

    // 往路標柱名
    if (outSignpostId != null) {
      String outSignpostName = outSignpostId.toString();
      String truncated = outSignpostName.length() > 11 ? outSignpostName.substring(0, 11) : outSignpostName;
      sheetNameBase += "_" + truncated;
    }

    // 復路標柱名
    if (inSignpostId != null) {
      String inSignpostName = inSignpostId.toString();
      String truncated = inSignpostName.length() > 11 ? inSignpostName.substring(0, 11) : inSignpostName;
      sheetNameBase += "_" + truncated;
    }

    String safe = WorkbookUtil.createSafeSheetName(sheetNameBase);
    XSSFSheet cloned = xssf.cloneSheet(0);
    int idx = xssf.getSheetIndex(cloned);
    xssf.setSheetName(idx, safe);

    this.activeSheet = cloned;
    super.setActiveSheet(this.activeSheet);
  }


  /**
   * 出力シートを作成する
   * @param dto EXCEL出力データDTO
   */
  public void setStopMapProtInfo(StopMapListDownloadDto dto) {
    if (activeSheet == null) throw new IllegalStateException("call createSheetFromTemplate() first.");

    Map<String, String> kv = new HashMap<>();
    dto.convertExcelHashMap().forEach((k, v) -> kv.put(k, v == null ? "" : String.valueOf(v)));

    BaseOriginMakeExcelSheet.replaceKeywords((XSSFSheet) activeSheet, kv);
  }

  /**
   * EXCELのコピー元となるシートを削除する
   */
  public void removeTemplateSheet() {
    if (xssf.getNumberOfSheets() > 0) xssf.removeSheetAt(0);
  }

  /**
   * EXCELワークブックオブジェクトを生成する。書き出し直前に一度だけ実行する
   * @return EXCELワークブック
   */
  public SXSSFWorkbook buildStreamingWorkbook() {
    if (sxssf == null) sxssf = new SXSSFWorkbook(xssf, 100);
    return sxssf;
  }
}

