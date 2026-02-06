package maint.constants;

/**
 * Common constant
 *
 * @author masaya.tanaka
 */
public class CommConstant {

  /** 禁則文字 */
  public static final String ILLEGAL_CHARACTERS = "<>=';";

  /** 検索画面の1画面の最大表示件数 */
  public static final Long PAGER_DISPLAY_MAX_ROW = 10L;

  /** Page表示範囲 */
  public static final Long PAGER_DISPLAY_PAGE_WIDTH = 5L;

  /** 基本利用終了日 */
  public static final String DEFAULT_SEVICE_END_DATE = "9999-12-31";

  /** テンプレートEXCELファイルのフォルダパス */
  public static final String TEMP_EXCEL_FOLDER_PATH = "classpath:excel/";

  /** EXCEL拡張子 */
  public static final String EXCEL_FILE_EXTENSION = ".xlsx";

}
