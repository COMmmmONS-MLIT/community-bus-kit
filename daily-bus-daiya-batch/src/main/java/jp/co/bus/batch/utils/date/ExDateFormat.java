package jp.co.bus.batch.utils.date;

import java.time.format.DateTimeFormatter;

/**
 * Date Format
 *
 * @author administrator
 */
public class ExDateFormat {
  /**
   * DateTime
   */
  /** Date time formatter uuuuMMddHHmmss */
  public static final DateTimeFormatter uuuuMMddHHmmss =
      DateTimeFormatter.ofPattern("uuuuMMddHHmmss");
  /** Date time formatter uuuu-MM-dd HH:mm:ss */
  public static final DateTimeFormatter uuuuMMddHHmmss_HYPHEN =
      DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
  /** Date time formatter uuuu/MM/dd HH:mm:ss */
  public static final DateTimeFormatter uuuuMMddHHmmss_SLASH =
      DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");

  /**
   * Date
   */
  /** Date time formatter uuuuMMdd */
  public static final DateTimeFormatter uuuuMMdd = DateTimeFormatter.ofPattern("uuuuMMdd");
  /** Date time formatter uuuu-MM-dd */
  public static final DateTimeFormatter uuuuMMdd_HYPEN = DateTimeFormatter.ofPattern("uuuu-MM-dd");
  /** Date time formatter uuuu-MM */
  public static final DateTimeFormatter uuuuMM_HYPEN = DateTimeFormatter.ofPattern("uuuu-MM");
  /** Date time formatter uuuu-MM */
  public static final DateTimeFormatter uuuuMM = DateTimeFormatter.ofPattern("uuuuMM");
  /** Date time formatter uuuu/MM/dd */
  public static final DateTimeFormatter uuuuMMdd_SLASH = DateTimeFormatter.ofPattern("uuuu/MM/dd");
  /** Date time formatter uuuu年MM月dd日 */
  public static final DateTimeFormatter uuuuMMdd_Jp = DateTimeFormatter.ofPattern("uuuu年MM月dd日");
  /** Date time formatter uuuu年MM月dd日 HH時mm分 */
  public static final DateTimeFormatter uuuuMMddHHmm_Jp =
      DateTimeFormatter.ofPattern("uuuu年MM月dd日 HH時mm分");
  /** Date time formatter uuuu年MM月dd日 HH時mm分ss秒 */
  public static final DateTimeFormatter uuuuMMddHHmmss_Jp =
      DateTimeFormatter.ofPattern("uuuu年MM月dd日 HH時mm分ss秒");
  /** Date time formatter uuuu年MM月 */
  public static final DateTimeFormatter uuuuMM_Jp = DateTimeFormatter.ofPattern("uuuu年MM月");
  /** Date time formatter uu年MM月 */
  public static final DateTimeFormatter uuMM_Jp = DateTimeFormatter.ofPattern("uu年MM月");
  /** Date time formatter uu年MM月dd日 */
  public static final DateTimeFormatter uuMMdd_Jp = DateTimeFormatter.ofPattern("uu年MM月dd日");

  /**
   * Time
   */
  /** Date time formatter HHmm */
  public static final DateTimeFormatter HHmm = DateTimeFormatter.ofPattern("HHmm");
  /** Date time formatter HH:mm */
  public static final DateTimeFormatter HHmm_COLON = DateTimeFormatter.ofPattern("HH:mm");
  /** Date time formatter HHmmss */
  public static final DateTimeFormatter HHmmss = DateTimeFormatter.ofPattern("HHmmss");
  /** Date time formatter HH:mm:ss */
  public static final DateTimeFormatter HHmmss_COLON = DateTimeFormatter.ofPattern("HH:mm:ss");

} 
