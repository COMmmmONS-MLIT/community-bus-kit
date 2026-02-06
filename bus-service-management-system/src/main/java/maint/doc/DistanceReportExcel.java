package maint.doc;

import maint.dto.document.DistanceReportDto;
import maint.utils.ExObjectUtils;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * キロ程表出力
 *
 * @author y.kim
 */
public class DistanceReportExcel extends BaseOriginMakeExcelSheet {

  private final XSSFWorkbook workbook;

  public DistanceReportExcel(XSSFWorkbook workbook) {
    this.workbook = workbook;
    setExcelSheet(this.workbook.getSheetAt(0));
  }

  public void setSheetName(String sheetName) {
    workbook.setSheetName(0, sheetName);
  }

  public SXSSFWorkbook getSXSSFWorkbook() {
    return new SXSSFWorkbook(this.workbook);
  }

  /** Excelテンプレートのシートを複製、シート命名 */
  public void createSheetFromTemplate(String lineRouteName) {
    String safe = WorkbookUtil.createSafeSheetName(
        (lineRouteName != null && !lineRouteName.isBlank()) ? lineRouteName : "無題");
    XSSFSheet cloned = workbook.cloneSheet(0);
    int idx = workbook.getSheetIndex(cloned);
    workbook.setSheetName(idx, safe);

    setExcelSheet(cloned);
  }

  public void writeDistanceReport(List<DistanceReportDto> reportList) {
    // 重複を除いた往路系統の件数分、シート作成処理を実行
    List<String> lineRouteNames = reportList.stream().map(DistanceReportDto::getLineRouteName).distinct().toList();
    for (String lineRouteName : lineRouteNames) {
      createSheetFromTemplate(lineRouteName);
      List<DistanceReportDto> list = reportList.stream().filter(dto -> dto.getLineRouteName().equals(lineRouteName)).toList();
      writeDistanceReportDetail(list);
    }
    // テンプレートシートを削除
    if (workbook.getNumberOfSheets() > 1) workbook.removeSheetAt(0);
  }

  public void writeDistanceReportDetail(List<DistanceReportDto> reportList) {
    if (reportList == null || reportList.isEmpty()) {
      return;
    }

    // 同じ標柱（往路）に対して複数の標柱（復路）が紐づく場合、先頭の1件分のみ出力する
    reportList = reportList.stream()
        .collect(Collectors.toMap(DistanceReportDto::getStopSequence, // key: stopSequence
            Function.identity(),                // value: DistanceReportDto
            (existing, replacement) -> existing // 既存の値を維持
        )).values().stream().toList();

    // 系統名 設定 (D1)
    setCellData(0, 3, reportList.get(0).getLineRouteName());

    int row = 2; // データの始まり
    int no = 1;
    // 終点の通算距離
    int totalDistance = 0;

    for (int i = 0; i < reportList.size(); i++) {
      DistanceReportDto dto = reportList.get(i);
      String inboundAddress = dto.getInboundAddress();
      // キロ(往路基準)
      Integer shapeDistTraveled = ExObjectUtils.isBlank(dto.getOutboundShapeDistTraveled()) ? 0 : dto.getOutboundShapeDistTraveled();
      Integer afterShapeDistTraveled = 0;
      if (i != reportList.size() - 1) {
        afterShapeDistTraveled = ExObjectUtils.isBlank(reportList.get(i + 1).getOutboundShapeDistTraveled()) ? 0 : reportList.get(i + 1).getOutboundShapeDistTraveled();
        totalDistance += afterShapeDistTraveled - shapeDistTraveled;
      }

      // No.
      setCellData(row, 2, no);
      no++;
      // 名称
      setCellData(row, 3, dto.getBusStopName());
      // 位置
      setCellData(row, 4, "往：");
      setCellData(row + 1, 4, "複：");
      setCellData(row, 5, dto.getOutboundAddress());
      setCellData(row + 1, 5, ExObjectUtils.isNotBlank(inboundAddress) ? inboundAddress : "");
      // キロ程（km）
      if (i == 0) {
        setCellData(row, 6,"↓往");
        setCellData(row + 1, 6, convertMetersToKilometers(afterShapeDistTraveled - shapeDistTraveled));
        if (reportList.size() == 1) {
          setCellData(row + 1, 7,"↑復");
        } else {
          setCellData(row + 1, 7,convertMetersToKilometers(afterShapeDistTraveled - shapeDistTraveled));
        }
      } else if ( i == reportList.size() - 1) {
        setCellData(row, 6, "");
        setCellData(row, 7, "");
        setCellData(2, 7,convertMetersToKilometers(totalDistance) + "km");
        setCellData(row + 1, 6,convertMetersToKilometers(totalDistance) + "km");
        setCellData(row + 1, 7,"↑復");
      } else {
        setCellData(row, 6, "");
        setCellData(row, 7, "");
        setCellData(row + 1, 6, convertMetersToKilometers(afterShapeDistTraveled - shapeDistTraveled));
        setCellData(row + 1, 7, convertMetersToKilometers(afterShapeDistTraveled - shapeDistTraveled));
      }

      // 最後の表ではない場合
      if ( i != reportList.size() - 1) {
        copyRow(workbook, 2, row + 2);     // テンプレートcopy (往路)
        copyRow(workbook, 3, row + 3); // テンプレートcopy (復路)
        // border 削除
        removeMiddleHorizontalBorder(row + 1, row + 2, 6);
        removeMiddleHorizontalBorder(row + 1, row + 2, 7);
      } else {
        // 最後の表 bottomBorder 追加
        addBottomBorder(row + 1, 6);
        addBottomBorder(row + 1, 7);
      }
      row += 2;
    }
  }

  /**
   * mをkmに変換
   *
   * @param meters m
   * @return km
   */
  public String convertMetersToKilometers(int meters) {
    double kilometers = meters / 1000.0;
    DecimalFormat df = new DecimalFormat("0.0");
    return df.format(kilometers);
  }
}
