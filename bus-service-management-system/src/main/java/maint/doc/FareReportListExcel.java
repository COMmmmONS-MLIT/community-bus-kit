package maint.doc;

import maint.dto.plan.fareReport.FareReportDownLoadDto;
import maint.form.fareReport.FareReportListsExcelForm;
import maint.utils.ExObjectUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 運賃表出力
 *
 * @author JEKAL.H
 */
public class FareReportListExcel extends BaseOriginMakeExcelSheet {

  /** ワークブック */
  private XSSFWorkbook workbook;

  /**
   * コンストラクタ
   *
   * @param workbook
   */
  public FareReportListExcel(XSSFWorkbook workbook) {
    this.workbook = workbook;
    setExcelSheet(this.workbook.getSheetAt(0));
  }

  /**
   * 運賃表出力情報設定
   *
   * @param currentRow 現在の行
   * @param dto        運賃表出力情報Dto
   */
  public int setFareReportInfo(int currentRow,
      FareReportDownLoadDto dto, FareReportListsExcelForm form) {

    int rowIdx = currentRow;
    if (getExcelSheet().getRow(rowIdx) == null) {
      getExcelSheet().createRow(rowIdx);
    }

    int col = 0;

    setCellData(rowIdx, col++, dto.getRosenName());
    setCellData(rowIdx, col++, ExObjectUtils.toI(dto.getSequenceNoFrom()));
    setCellData(rowIdx, col++, dto.getBusStopNameFrom());
    setCellData(rowIdx, col++, ExObjectUtils.toI(dto.getSequenceNoTo()));
    setCellData(rowIdx, col++, dto.getBusStopNameTo());
    setCellData(rowIdx, col++, ExObjectUtils.toI(dto.getCharge()));

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

}
