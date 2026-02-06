package maint.service.record;

import java.util.List;
import maint.dto.record.operationRecords.OperationRecordsDto;
import maint.mapper.LinesMapper;
import maint.service.BaseService;
import maint.utils.ExObjectUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

/**
 * 運行実績SERVICE
 *
 * @author g.ko
 */

@Service
public class OperationRecordsService extends BaseService {

  /** 路線MAPPER */
  @Autowired
  LinesMapper linesMapper;

  /**
   * 運行実績取得
   *
   * @param extractionStartDate 抽出開始日
   * @param extractionEndDate 抽出終了日
   * @param rosenId 路線ID
   * @return 運行実績DTOリスト
   */
  public List<OperationRecordsDto> getOperationRecords(String extractionStartDate,
      String extractionEndDate, Long rosenId) {
    return linesMapper.getOperationRecords(extractionStartDate, extractionEndDate,
        rosenId, getUserSession().getBusinessCorpId());
  }

  /**
   * Excelファイル作成
   *
   * @param list 運行実績情報リスト
   * @return ファイル
   * @throws Exception 例外
   */
  public byte[] buildExcelFromTemplate(List<OperationRecordsDto> list) throws Exception {
    final String TEMPLATE_PATH = "excel/operationRecord.xlsx";
    try (var in = new ClassPathResource(TEMPLATE_PATH).getInputStream();
        Workbook wb = new XSSFWorkbook(in);
        var bos = new java.io.ByteArrayOutputStream()) {

      // シート
      Sheet sh = wb.getSheet("運行実績");
      if (ExObjectUtils.isBlank(sh)) {
        sh = wb.getSheetAt(0);
      }

      // ヘッダー
      Row headerRow = sh.getRow(0);
      if (ExObjectUtils.isBlank(headerRow)) {
        headerRow = sh.createRow(0);
      }

      // テンプレ列数
      final int colCount = 11;

      // データ用スタイル生成(ヘッダースタイルを利用)
      CellStyle[] dataStyles = new CellStyle[colCount];
      for (int i = 0; i < colCount; i++) {
        Cell headerCell = headerRow.getCell(i);
        CellStyle headerStyle =
            (ExObjectUtils.isNotBlank(headerCell)) ? headerCell.getCellStyle() : null;

        CellStyle s = wb.createCellStyle();
        if (ExObjectUtils.isNotBlank(headerStyle)) {
          s.cloneStyleFrom(headerStyle);
        }
        // 背景は白
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        s.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        s.setFillBackgroundColor(IndexedColors.WHITE.getIndex());

        // 罫線(外枠)
        s.setBorderTop(BorderStyle.THIN);
        s.setBorderBottom(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN);
        s.setBorderRight(BorderStyle.THIN);

        dataStyles[i] = s;
      }

      // データ開始行
      int rowIdx = 1;

      for (OperationRecordsDto d : list) {
        Row row = sh.getRow(rowIdx);
        if (ExObjectUtils.isBlank(row)) {
          row = sh.createRow(rowIdx);
        }

        setString(row, 1, d.getLineRouteNo(), dataStyles[0]);
        setString(row, 2, d.getOrigin(), dataStyles[1]);
        setString(row, 3, d.getMainViaStops(), dataStyles[2]);
        setString(row, 4, d.getDestination(), dataStyles[3]);
        setString(row, 5, d.getFirstDepartureTime(), dataStyles[4]);
        setString(row, 6, d.getLastDepartureTime(), dataStyles[5]);
        setString(row, 7, d.getDurationMinutes(), dataStyles[6]);
        setString(row, 8, d.getOperationCount(), dataStyles[7]);
        setString(row, 9, d.getDistanceKm(), dataStyles[8]);

        rowIdx++;
      }

      for (int c = 0; c < colCount; c++) {
        sh.autoSizeColumn(c);
        int w = sh.getColumnWidth(c);
        if (w < 3000) {
          sh.setColumnWidth(c, 3000);
        }
      }

      wb.write(bos);
      return bos.toByteArray();
    }
  }

  /** 列設定 */
  private void setString(Row row, int c, String val, CellStyle style) {
    Cell cell = row.getCell(c - 1);
    if (ExObjectUtils.isBlank(cell)) {
      cell = row.createCell(c - 1);
    }
    if (ExObjectUtils.isBlank(val)) {
      cell.setBlank();
    } else {
      cell.setCellValue(val);
    }
    if (ExObjectUtils.isNotBlank(style)) {
      cell.setCellStyle(style);
    }
  }

}
