package maint.doc;

import maint.utils.ExObjectUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Excel Base
 *
 * @author jeon
 */
public class BaseOriginMakeExcelSheet {

  /** ロガー */
  protected static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /** シーツObject */
  private XSSFSheet sheet;

  // 共通の線スタイル
  private CellStyle defaultBorderStyle;

  public void setExcelSheet(XSSFSheet sheet) {
    this.sheet = sheet;
  }

  public XSSFSheet getExcelSheet() {
    return this.sheet;
  }

  /**
   * 指定セルに値を設定します。文字列が複数行（改行含む）の場合は行の高さを調整します。
   */
  public void setCellData(int rowNo, int cellNo, Object value) {
    if (value == null)
      value = "";

    XSSFRow row = sheet.getRow(rowNo);
    if (row == null) {
      return;
    }

    XSSFCell cell = row.getCell(cellNo, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
    if (cell == null) {
      return;
    }

    if (isIntValue(value)) {
      int tmpVal = (Integer) value;
      cell.setCellValue(tmpVal);
    } else if (isBigDecimalValue(value)) {
      BigDecimal tmpVal = (BigDecimal) value;
      cell.setCellValue(tmpVal.doubleValue());
    } else if (isLongValue(value)) {
      Long tmpVal = (Long) value;
      cell.setCellValue(tmpVal);
    } else {
      String tmpVal = ExObjectUtils.isNullAny(value) ? "" : String.valueOf(value);
      String[] returnCnt = tmpVal.split("\\n");
      if (returnCnt.length > 1) {
        row.setHeightInPoints((returnCnt.length * sheet.getDefaultRowHeightInPoints()));
      }
      cell.setCellValue(tmpVal);
    }
  }

  /**
   * 指定セルに値を設定します。文字列が複数行（改行含む）の場合は行の高さを調整します。
   * 併せて、セルにデフォルトの罫線スタイル（細線）を適用します。
   */
  public void setCellDataBorder(int rowNo, int cellNo, Object value) {
    if (value == null)
      value = "";
    XSSFRow row = sheet.getRow(rowNo);
    if (row == null)
      row = sheet.createRow(rowNo);
    XSSFCell cell = row.getCell(cellNo, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
    if (cell == null)
      cell = row.createCell(cellNo);

    cell.setCellStyle(getDefaultBorderStyle());

    if (isIntValue(value)) {
      int tmpVal = (Integer) value;
      cell.setCellValue(tmpVal);
    } else if (isBigDecimalValue(value)) {
      BigDecimal tmpVal = (BigDecimal) value;
      cell.setCellValue(tmpVal.doubleValue());
    } else if (isLongValue(value)) {
      Long tmpVal = (Long) value;
      cell.setCellValue(tmpVal);
    } else {
      String tmpVal = ExObjectUtils.isNullAny(value) ? "" : String.valueOf(value);
      String[] returnCnt = tmpVal.split("\\n");
      if (returnCnt.length > 1) {
        row.setHeightInPoints((returnCnt.length * sheet.getDefaultRowHeightInPoints()));
      }
      cell.setCellValue(tmpVal);
    }
  }


  /**
   * 指定セルに値を設定します。文字列が複数行（改行含む）の場合は行の高さを調整します。
   * 併せて、セルにデフォルトの罫線スタイル（細線）を適用しつつ、テンプレートセルの色などのスタイルも保持します。
   *
   * @param rowNo 行番号
   * @param cellNo 列番号
   * @param value セットする値
   * @param templateRowNo テンプレートセルの行番号
   * @param templateCellNo テンプレートセルの列番号
   */
  public void setCellDataBorder(int rowNo, int cellNo, Object value, int templateRowNo, int templateCellNo, XSSFSheet sheet) {
    if (value == null) {
      value = "";
    }
    XSSFRow row = sheet.getRow(rowNo);
    if (row == null) {
      row = sheet.createRow(rowNo);
    }
    XSSFCell cell = row.getCell(cellNo, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
    if (cell == null) {
      cell = row.createCell(cellNo);
    }

    // テンプレートセルを取得
    XSSFRow templateRow = sheet.getRow(templateRowNo);
    Cell templateCell = null;
    if (templateRow != null) {
      templateCell = templateRow.getCell(templateCellNo);
    }

    // テンプレートセルのスタイルをベースにした新しいスタイルを作成
    Workbook workbook = sheet.getWorkbook();
    CellStyle newStyle = workbook.createCellStyle();

    if (templateCell != null && templateCell.getCellStyle() != null) {
      // テンプレートのスタイルをクローン
      newStyle.cloneStyleFrom(templateCell.getCellStyle());
    }

    // 罫線を追加（既存のスタイルの上に罫線を設定）
    newStyle.setBorderTop(BorderStyle.THIN);
    newStyle.setBorderBottom(BorderStyle.THIN);
    newStyle.setBorderLeft(BorderStyle.THIN);
    newStyle.setBorderRight(BorderStyle.THIN);

    cell.setCellStyle(newStyle);

    // 値を設定する処理（既存の処理と同じ）
    if (isIntValue(value)) {
      int tmpVal = (Integer) value;
      cell.setCellValue(tmpVal);
    } else if (isBigDecimalValue(value)) {
      BigDecimal tmpVal = (BigDecimal) value;
      cell.setCellValue(tmpVal.doubleValue());
    } else if (isLongValue(value)) {
      Long tmpVal = (Long) value;
      cell.setCellValue(tmpVal);
    } else {
      String tmpVal = ExObjectUtils.isNullAny(value) ? "" : String.valueOf(value);
      String[] returnCnt = tmpVal.split("\\n");
      if (returnCnt.length > 1) {
        row.setHeightInPoints((returnCnt.length * sheet.getDefaultRowHeightInPoints()));
      }
      cell.setCellValue(tmpVal);
    }
  }

  // Int型チェック
  public boolean isIntValue(Object obj) {
    boolean retFlg = false;
    if (obj == null)
      return false;
    try {
      int tmpInt = (Integer) obj;
      if (String.valueOf(obj).equals(String.valueOf(tmpInt))) {
        return true;
      }
    } catch (Exception e) {
      return false;
    }
    return retFlg;
  }

  // BigDecimal型チェック
  public boolean isBigDecimalValue(Object obj) {
    try {
      @SuppressWarnings("unused")
      BigDecimal tmpInt = (BigDecimal) obj;
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  // Long型チェック
  public boolean isLongValue(Object obj) {
    try {
      @SuppressWarnings("unused")
      Long tmpBig = (Long) obj;
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * デフォルトの罫線（細線）スタイルを作成・キャッシュして返却します。
   * CellStyleは上限があるため、毎回生成せず再利用します。
   */
  private CellStyle getDefaultBorderStyle() {
    if (defaultBorderStyle == null) {
      Workbook wb = sheet.getWorkbook();
      CellStyle cs = wb.createCellStyle();
      cs.setBorderTop(BorderStyle.THIN);
      cs.setBorderRight(BorderStyle.THIN);
      cs.setBorderBottom(BorderStyle.THIN);
      cs.setBorderLeft(BorderStyle.THIN);
      // 必要に応じて自動改行を有効化できます
      // cs.setWrapText(true);
      defaultBorderStyle = cs;
    }
    return defaultBorderStyle;
  }

  /**
   * copyRow
   *
   * @param workbook workbook
   * @param sourceRowNum コピー元
   * @param targetRowNum コピー先
   */
  public void copyRow(XSSFWorkbook workbook, int sourceRowNum, int targetRowNum) {
    XSSFRow sourceRow = sheet.getRow(sourceRowNum);
    XSSFRow targetRow = sheet.getRow(targetRowNum);

    if (targetRow != null) {
      sheet.removeRow(targetRow); // remove existing row
    }

    targetRow = sheet.createRow(targetRowNum);
    targetRow.setHeight(sourceRow.getHeight()); // copy height

    for (int i = sourceRow.getFirstCellNum(); i < sourceRow.getLastCellNum(); i++) {
      XSSFCell oldCell = sourceRow.getCell(i);
      XSSFCell newCell = targetRow.createCell(i);

      if (oldCell == null) {
        continue;
      }

      // copy style
      CellStyle newCellStyle = workbook.createCellStyle();
      newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
      newCell.setCellStyle(newCellStyle);

      // copy type
      newCell.setCellType(oldCell.getCellType());

      // copy value
      switch (oldCell.getCellType()) {
        case STRING:
          newCell.setCellValue(oldCell.getStringCellValue());
          break;
        case NUMERIC:
          newCell.setCellValue(oldCell.getNumericCellValue());
          break;
        case BOOLEAN:
          newCell.setCellValue(oldCell.getBooleanCellValue());
          break;
        case FORMULA:
          newCell.setCellFormula(oldCell.getCellFormula());
          break;
        case ERROR:
          newCell.setCellErrorValue(oldCell.getErrorCellValue());
          break;
        case BLANK:
          newCell.setBlank();
          break;
      }
    }
  }

  // Excelテンプレート　キーワード→該当データ置換処理
  public static void replaceKeywords(XSSFSheet sheet, Map<String, String> kv) {
    for (Row row : sheet) {
      for (Cell cell : row) {
        if (cell != null && cell.getCellType() == CellType.STRING) {
          String v = cell.getStringCellValue();
          boolean hit = false;
          for (Map.Entry<String, String> e : kv.entrySet()) {
            String k = e.getKey();
            String val = e.getValue() == null ? "" : e.getValue();
            if (v.contains(k)) {
              v = v.replace(k, val);
              hit = true;
            }
          }
          if (hit)
            cell.setCellValue(v);
        }
      }
    }
  }

  /**
   * 2つの行の間にある、指定したセルの中央の横線（ボーダー）を削除。
   *
   * @param row1 上側の行番号
   * @param row2 下側の行番号
   * @param cellNo 対象となるセルの列番号
   */
  public void removeMiddleHorizontalBorder(int row1, int row2, int cellNo) {

    XSSFWorkbook workbook = sheet.getWorkbook();

    // 上側の行を取得
    XSSFRow upperRow = sheet.getRow(row1);

    // 下側の行を取得
    XSSFRow lowerRow = sheet.getRow(row2);

    if (ExObjectUtils.isNotBlank(upperRow) && ExObjectUtils.isNotBlank(upperRow.getCell(cellNo))) {
      // 上側のセルの下ボーダーを削除
      CellStyle upperStyle = workbook.createCellStyle();
      upperStyle.cloneStyleFrom(upperRow.getCell(cellNo).getCellStyle()); // 元のスタイルをコピー
      upperStyle.setBorderBottom(BorderStyle.NONE);
      upperRow.getCell(cellNo).setCellStyle(upperStyle);
    }

    if (ExObjectUtils.isNotBlank(lowerRow) && ExObjectUtils.isNotBlank(lowerRow.getCell(cellNo))) {
      // 下側のセルの上ボーダーを削除
      CellStyle lowerStyle = workbook.createCellStyle();
      lowerStyle.cloneStyleFrom(lowerRow.getCell(cellNo).getCellStyle()); // 元のスタイルをコピー
      lowerStyle.setBorderTop(BorderStyle.NONE);
      lowerRow.getCell(cellNo).setCellStyle(lowerStyle);
    }
  }

  /**
   * 指定したセルに下ボーダー（下線）を追加。
   *
   * @param rowNo 行番号（0から始まる）
   * @param cellNo 列番号（0から始まる）
   */
  public void addBottomBorder(int rowNo, int cellNo) {
    XSSFWorkbook workbook = sheet.getWorkbook();

    if (ExObjectUtils.isNotBlank(sheet.getRow(rowNo)) && ExObjectUtils.isNotBlank(
        sheet.getRow(rowNo).getCell(cellNo))) {
      // 元のスタイルをコピーして、下ボーダーを追加
      CellStyle style = workbook.createCellStyle();
      style.cloneStyleFrom(sheet.getRow(rowNo).getCell(cellNo).getCellStyle()); // 既存のスタイルを保持
      style.setBorderBottom(BorderStyle.THIN);   // 下線を追加（細線）

      sheet.getRow(rowNo).getCell(cellNo).setCellStyle(style);
    }
  }

  // 現在のシートを保持するためのフック
  public void setActiveSheet(org.apache.poi.ss.usermodel.Sheet sheet) {
    if (!(sheet instanceof org.apache.poi.xssf.usermodel.XSSFSheet)) {
      throw new IllegalArgumentException("XSSFSheet is required.");
    }
    this.sheet = (org.apache.poi.xssf.usermodel.XSSFSheet) sheet;
  }
}
