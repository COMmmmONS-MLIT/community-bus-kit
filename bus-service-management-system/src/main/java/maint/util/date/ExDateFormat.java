package maint.util.date;

import java.time.format.DateTimeFormatter;

/**
 * Date Format
 *
 * <p>
 * ※DateTimeFormatterのみ使ってください。
 * </p>
 *
 * @author administrator
 */
public class ExDateFormat {
  /**
   * DateTime
   */
  public static final DateTimeFormatter uuuuMMddHHmmss_HYPHEN = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
  /** Date time formatter uuuu-MM-dd */
  public static final DateTimeFormatter uuuuMMdd_HYPEN = DateTimeFormatter.ofPattern("uuuu-MM-dd");
  /** Date formatter yyMMdd */
  public static final DateTimeFormatter yyMMdd = DateTimeFormatter.ofPattern("yyMMdd");
}
