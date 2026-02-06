package maint.doc;

import maint.utils.ExRegUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Excelを扱うBaseクラス
 *
 * @author jeon
 */
public class BaseExcel implements AutoCloseable {

  /** Excelファイルオブジェクト */
  protected Workbook workbook;

  /** 操作対象のシート（デフォルトは最初のシート） */
  protected Sheet sheet;

  /**
   * コンストラクタ
   *
   * @param file アップロードファイル
   */
  public BaseExcel(MultipartFile file) throws IOException {
    try (InputStream in = file.getInputStream()) {
      workbook = new XSSFWorkbook(in);
      sheet = workbook.getSheetAt(0);
    }
  }

  /**
   * Excelファイルを閉じる
   *
   */
  @Override
  public void close() throws IOException {
    workbook.close();
  }

  /**
   * ファイル内容ストリーム出力
   *
   * @param outputStream OutputStream
   */
  public void write(OutputStream outputStream) throws IOException {
    workbook.write(outputStream);
  }

  /**
   * 対象シートのセルをA1形式で指定して取得します。(セルオブジェクトが存在しない場合はセルを生成して返します)
   *
   * <pre>{@code
   * Cell cell = getCell(sheet, "A1");
   * }</pre>
   *
   * @param a1 A1形式のセル参照文字列
   * @return セルオブジェクト
   * @see <a href="https://knooto.info/java-poi-snippets/">参考</a>
   */
  protected Cell getCell(String a1) {
    CellReference ref = new CellReference(a1);
    return getCell(ref.getRow(), ref.getCol());
  }

  /**
   * 対象シートのセルを行・列番号を指定して取得します。(セルオブジェクトが存在しない場合はセルを生成して返します)
   *
   * <pre>{@code
   * Cell cell = getCell(sheet, 0, 0);
   * }</pre>
   *
   * @param rowIndex 行番号 (0ベース)
   * @param colIndex 列番号 (0ベース)
   * @return セルオブジェクト
   * @see <a href="https://knooto.info/java-poi-snippets/">参考</a>
   */
  protected Cell getCell(int rowIndex, int colIndex) {
    Row row = sheet.getRow(rowIndex);
    if (row == null)
      row = sheet.createRow(rowIndex);

    Cell cell = row.getCell(colIndex);
    if (cell == null)
      cell = row.createCell(colIndex);

    return cell;
  }

  /**
   * セルの値を取得
   *
   * @param a1 A1形式のセル参照文字列
   * @return セルの値（Long）
   */
  protected Long getLongCellValue(String a1) {
    Cell cell = getCell(a1);
    CellType cellType = cell.getCellType();
    if (cellType == CellType.NUMERIC) {
      return Double.valueOf(cell.getNumericCellValue()).longValue();
    } else if (cellType == CellType.STRING) {
      try {
        return Long.valueOf(cell.getStringCellValue());
      } catch (NumberFormatException e) {
        if (!cell.getStringCellValue().matches(ExRegUtils.onlyNumberReg)) {
          return 0L;
        }
        throw new IllegalArgumentException("Cannot convert string to long", e);
      }
    } else {
      throw new IllegalArgumentException("Invalid cell type");
    }
  }

  /**
   * セルの値を取得
   *
   * @param rowNum 行番号（1から開始）
   * @param colNum 列番号（1から開始）
   * @return セルの値（String型）
   */
  protected String getStringCellValue(int rowNum, int colNum) {
    Cell cell = getCell(rowNum - 1, colNum - 1);
    CellType cellType = cell.getCellType();
    if (cellType == CellType.STRING) {
      return cell.getStringCellValue();
    } else if (cellType == CellType.NUMERIC) {
      return Integer.toString((int) cell.getNumericCellValue());
    } else if (cellType == CellType.BLANK) {
      return "";
    } else {
      throw new IllegalArgumentException("Invalid cell type");
    }
  }


}
