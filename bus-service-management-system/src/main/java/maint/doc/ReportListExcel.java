package maint.doc;

import maint.dto.export.lists.ReportDownLoadDto;
import maint.form.report.ReportListsExcelForm;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import maint.enums.ReportType;

import java.util.Map;

/**
 * 一覧表出力表出力
 *
 * @author JEKAL.H
 */
public class ReportListExcel extends BaseOriginMakeExcelSheet {

  /**
   * ワークブック
   */
  private XSSFWorkbook workbook;

  private final Map<CellStyle, CellStyle> borderStyleCache = new java.util.HashMap<>();

  /**
   * コンストラクタ
   *
   * @param workbook
   */
  public ReportListExcel(XSSFWorkbook workbook) {
    this.workbook = workbook;
    setExcelSheet(this.workbook.getSheetAt(0));
  }

  /**
   * 一覧表出力情報設定
   *
   * @param currentRow 現在の行
   * @param dto        一覧表出力情報Dto
   */
  public int setReportRegisterInfo(int currentRow, ReportDownLoadDto dto,
      ReportListsExcelForm form) {

    int rowIdx = currentRow;

    if (getExcelSheet().getRow(rowIdx) == null) {
      getExcelSheet().createRow(rowIdx);
    }

    // 出力対象ごとに委譲
    if ((ReportType.BUS_STOP_LIST.getValue()).equals(form.getReportType())) {
      writeBusStopListRow(rowIdx, dto);
      applyBorders(getExcelSheet().getRow(rowIdx), 0, 6);
    } else if ((ReportType.SIGN_POST_LIST.getValue()).equals(form.getReportType())) {
      writeSignPostsListRow(rowIdx, dto);
      applyBorders(getExcelSheet().getRow(rowIdx), 0, 14);
    } else if ((ReportType.SIGN_POST_STOP_LIST.getValue()).equals(form.getReportType())) {
      writeSignPostsBusStopListRow(rowIdx, dto);
      applyBorders(getExcelSheet().getRow(rowIdx), 0, 19);
    } else if ((ReportType.SYSTEM_LIST.getValue()).equals(form.getReportType())) {
      writeLineRoutesListRow(rowIdx, dto);
      applyBorders(getExcelSheet().getRow(rowIdx), 0, 22);
    } else {
      writeDaiyaListRow(rowIdx, dto);
      applyBorders(getExcelSheet().getRow(rowIdx), 0, 15);
    }

    return rowIdx + 1;

  }

  /**
   * 出力対象が停留所一覧情報設定
   *
   * @param rowIdx rowインデクス
   * @param dto    一覧表出力情報Dto
   */
  private void writeBusStopListRow(int rowIdx, ReportDownLoadDto dto) {
    int col = 0;
    setCellData(rowIdx, col++, dto.getBusinessCorpId());
    setCellData(rowIdx, col++, dto.getBusStopId());
    setCellData(rowIdx, col++, dto.getBusStopName());
    setCellData(rowIdx, col++, dto.getServiceStartDate());
    setCellData(rowIdx, col++, dto.getServiceEndDate());
    setCellData(rowIdx, col++, dto.getLatitudeLongitude());
    setCellData(rowIdx, col++, dto.getBusStopUrl());
  }

  /**
   * 出力対象が標柱一覧情報設定
   *
   * @param rowIdx rowインデクス
   * @param dto    一覧表出力情報Dto
   */
  private void writeSignPostsListRow(int rowIdx, ReportDownLoadDto dto) {
    int col = 0;
    setCellData(rowIdx, col++, dto.getBusStopId());
    setCellData(rowIdx, col++, dto.getBusStopName());
    setCellData(rowIdx, col++, dto.getSignpostId());
    setCellData(rowIdx, col++, dto.getSignpostBranchNumber());
    setCellData(rowIdx, col++, dto.getSignpostCode());
    setCellData(rowIdx, col++, dto.getSignpostName());
    setCellData(rowIdx, col++, dto.getAddress());
    setCellData(rowIdx, col++, dto.getPlaceName());
    setCellData(rowIdx, col++, dto.getSignpostDesc());
    setCellData(rowIdx, col++, dto.getSignLatitudeLongitude());
    setCellData(rowIdx, col++, dto.getSignpostUrl());
    setCellData(rowIdx, col++, dto.getBusStopType());
    setCellData(rowIdx, col++, dto.getPlatformCode());
    setCellData(rowIdx, col++, dto.getArrivalPoint());
    setCellData(rowIdx, col++, dto.getArrivalPointRadius());
  }

  /**
   * 出力対象が標柱停留所一覧情報設定
   *
   * @param rowIdx rowインデクス
   * @param dto    一覧表出力情報Dto
   */
  private void writeSignPostsBusStopListRow(int rowIdx, ReportDownLoadDto dto) {
    int col = 0;
    setCellData(rowIdx, col++, dto.getBusinessCorpId());
    setCellData(rowIdx, col++, dto.getBusStopId());
    setCellData(rowIdx, col++, dto.getBusStopName());
    setCellData(rowIdx, col++, dto.getServiceStartDate());
    setCellData(rowIdx, col++, dto.getServiceEndDate());
    setCellData(rowIdx, col++, dto.getLatitudeLongitude());
    setCellData(rowIdx, col++, dto.getBusStopUrl());
    setCellData(rowIdx, col++, dto.getSignpostId());
    setCellData(rowIdx, col++, dto.getSignpostBranchNumber());
    setCellData(rowIdx, col++, dto.getSignpostCode());
    setCellData(rowIdx, col++, dto.getSignpostName());
    setCellData(rowIdx, col++, dto.getAddress());
    setCellData(rowIdx, col++, dto.getPlaceName());
    setCellData(rowIdx, col++, dto.getSignpostDesc());
    setCellData(rowIdx, col++, dto.getSignLatitudeLongitude());
    setCellData(rowIdx, col++, dto.getSignpostUrl());
    setCellData(rowIdx, col++, dto.getBusStopType());
    setCellData(rowIdx, col++, dto.getPlatformCode());
    setCellData(rowIdx, col++, dto.getArrivalPoint());
    setCellData(rowIdx, col++, dto.getArrivalPointRadius());
  }

  /**
   * 出力対象が系統一覧情報設定
   *
   * @param rowIdx rowインデクス
   * @param dto    一覧表出力情報Dto
   */
  private void writeLineRoutesListRow(int rowIdx, ReportDownLoadDto dto) {
    int col = 0;
    setCellData(rowIdx, col++, dto.getRosenName());
    setCellData(rowIdx, col++, dto.getLineRouteId());
    setCellData(rowIdx, col++, dto.getLineRouteCode());
    setCellData(rowIdx, col++, dto.getLineRouteServiceStartDate());
    setCellData(rowIdx, col++, dto.getLineRouteServiceEndDate());
    setCellData(rowIdx, col++, dto.getLineRouteName());
    setCellData(rowIdx, col++, dto.getLineRouteShortName());
    setCellData(rowIdx, col++, dto.getLineRouteNumber());
    setCellData(rowIdx, col++, dto.getLineDetailServiceDivision());
    setCellData(rowIdx, col++, dto.getLineRouteUrl());
    setCellData(rowIdx, col++, dto.getLineRouteColor());
    setCellData(rowIdx, col++, dto.getLineRouteTextColor());
    setCellData(rowIdx, col++, dto.getLineRouteRemarks());
    setCellData(rowIdx, col++, dto.getRosenBusStopId());
    setCellData(rowIdx, col++, dto.getLineRouteBusStopId());
    setCellData(rowIdx, col++, dto.getSignpostId());
    setCellData(rowIdx, col++, dto.getBusStopName());
    setCellData(rowIdx, col++, dto.getSignpostName());
    setCellData(rowIdx, col++, dto.getRideAlightDivision());
    setCellData(rowIdx, col++, dto.getDropOffDivision());
    setCellData(rowIdx, col++, dto.getStopHeadsign());
    setCellData(rowIdx, col++, dto.getShapeDistTraveled());
    setCellData(rowIdx, col++, dto.getStopSequence());
  }

  /**
   * 出力対象が便ダイヤ一覧情報設定
   *
   * @param rowIdx rowインデクス
   * @param dto    一覧表出力情報Dto
   */
  private void writeDaiyaListRow(int rowIdx, ReportDownLoadDto dto) {
    int col = 0;
    setCellData(rowIdx, col++, dto.getBusServiceId());
    setCellData(rowIdx, col++, dto.getBusServiceTripId());
    setCellData(rowIdx, col++, dto.getBusServiceTripHeadsign());
    setCellData(rowIdx, col++, dto.getBusServiceName());
    setCellData(rowIdx, col++, dto.getCalendarId());
    setCellData(rowIdx, col++, dto.getWheelchairAccessibleType());
    setCellData(rowIdx, col++, dto.getBikesAllowedType());
    setCellData(rowIdx, col++, dto.getBusServiceTripDesc());
    setCellData(rowIdx, col++, dto.getBusServiceTripDescSymbol());
    setCellData(rowIdx, col++, dto.getOfficeId());
    setCellData(rowIdx, col++, dto.getWorkNumber());
    setCellData(rowIdx, col++, dto.getLineRouteBusStopId());
    setCellData(rowIdx, col++, dto.getBusStopName());
    setCellData(rowIdx, col++, dto.getBusStopArrivalTime());
    setCellData(rowIdx, col++, dto.getBusStopDepartureTime());
    setCellData(rowIdx, col++, dto.getVehicleId());
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
