package maint.doc;

import maint.dto.record.operatingReport.OperatingReportDaiyasRegisterDownLoadDto;
import maint.form.operatingReport.OperatingReportListsExcelForm;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Map;

/**
 * 運行回数出力表出力
 *
 * @author JEKAL.H
 */
public class OperatingReportDaiyasRegisterListExcel extends BaseOriginMakeExcelSheet {

  /** ワークブック */
  private XSSFWorkbook workbook;

  private final Map<CellStyle, CellStyle> borderStyleCache = new java.util.HashMap<>();

  /**
   * コンストラクタ
   *
   * @param workbook
   */
  public OperatingReportDaiyasRegisterListExcel(XSSFWorkbook workbook) {
    this.workbook = workbook;
    setExcelSheet(this.workbook.getSheetAt(0));
  }

  /**
   * 運行回数出力情報設定
   *
   * @param currentRow 現在の行
   * @param dto        運行回数出力情報Dto
   */
  public int setOperatingReportDaiyasRegisterInfo(int currentRow,
      OperatingReportDaiyasRegisterDownLoadDto dto, OperatingReportListsExcelForm form) {

    int rowIdx = currentRow;
    if (getExcelSheet().getRow(rowIdx) == null) {
      getExcelSheet().createRow(rowIdx);
    }

    int col = 0;

    setCellData(rowIdx, col++, dto.getLineRouteCode());
    setCellData(rowIdx, col++, dto.getLineRouteName());
    setCellData(rowIdx, col++, dto.getSalesCancelFlg0Count());
    setCellData(rowIdx, col++, dto.getSalesCancelFlg1Count());

    // 罫線適用
    applyBorders(getExcelSheet().getRow(rowIdx), 0, 3);

    return rowIdx + 1;

  }

  /**
   * シート名の設定(1シート目固定)
   *
   * @param sheetName シート名
   */
  public void setSheetName(String sheetName) {
    this.workbook.setSheetName(0, sheetName);
  }

  /**
   * SXSSFのブックを取得する
   *
   * @return SXSSFWorkbook
   */
  public SXSSFWorkbook getSXSSFWorkbook() {
    return new SXSSFWorkbook(this.workbook);
  }

  /**
   * 罫線設定
   *
   * @param base 元となるセルスタイル
   * @param wb 対象のWorkbook
   * @return 黒罫線が設定されたセルスタイル
   */
  private CellStyle addThinBorderTo(CellStyle base, Workbook wb) {
    if (base != null && borderStyleCache.containsKey(base)) {
      return borderStyleCache.get(base);
    }

    CellStyle s = wb.createCellStyle();
    if (base != null) {
      s.cloneStyleFrom(base);
    }
    s.setBorderTop(BorderStyle.THIN);
    s.setBorderBottom(BorderStyle.THIN);
    s.setBorderLeft(BorderStyle.THIN);
    s.setBorderRight(BorderStyle.THIN);
    s.setTopBorderColor(IndexedColors.BLACK.getIndex());
    s.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    s.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    s.setRightBorderColor(IndexedColors.BLACK.getIndex());

    if (base != null) {
      borderStyleCache.put(base, s);
    }
    return s;
  }

  /**
   * 罫線適用
   *
   * @param row 対象行
   * @param fromCol 開始列インデックス
   * @param toCol 終了列インデックス
   */
  private void applyBorders(Row row, int fromCol, int toCol) {
    Workbook wb = row.getSheet().getWorkbook();
    for (int c = fromCol; c <= toCol; c++) {
      Cell cell = row.getCell(c);
      if (cell == null) cell = row.createCell(c);
      CellStyle styled = addThinBorderTo(cell.getCellStyle(), wb);
      cell.setCellStyle(styled);
    }
  }

}
