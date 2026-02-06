package jp.co.bus.batch.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Calendar;
import org.apache.commons.lang3.StringUtils;

/**
 * Common Utility
 * 
 * @author g.ko
 *
 */
public class CommonUtils {

  /** Date time formatter uuuuMMddHHmmss */
  public static final DateTimeFormatter FORMATTER_uuuuMMddHHmmss =
      DateTimeFormatter.ofPattern("uuuuMMddHHmmss");

  /** formatter uuuu-MM-dd H:m:s */
  private static final DateTimeFormatter uuuu_MM_dd_hh_mm_ss_DTF =
      DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss").withResolverStyle(ResolverStyle.STRICT);


  /** Date time formatter uuuuMMdd */
  public static final DateTimeFormatter FORMATTER_uuuuMMdd =
      DateTimeFormatter.ofPattern("uuuuMMdd");

  /** Date time formatter uuuu/MM/dd */
  public static final DateTimeFormatter FORMATTER_uuuuMMdd_SLASH =
      DateTimeFormatter.ofPattern("uuuu/MM/dd");

  /** Date time formatter HH:mm */
  public static final DateTimeFormatter FORMATTER_hhmm_SLASH =
      DateTimeFormatter.ofPattern("HH:mm");

  /**
   * 現在の時刻取得（uuuuMMddHHmmss）
   * 
   * @return
   */
  public static boolean isDateTime(String dateStr) {
    try {
      LocalDate.parse(dateStr, FORMATTER_uuuuMMdd);
    } catch (Exception e) {
      return false;
    }
    return true;
  }


  /**
   * 表示用日付に返還
   * 
   * @param dateStr uuuuMMdd
   * @return uuuu/MM/dd
   */
  public static String convertDisplayDate(String dateStr) {
    return LocalDate.parse(dateStr, FORMATTER_uuuuMMdd).format(FORMATTER_uuuuMMdd_SLASH);
  }

  /**
   * 指定の日数を加算
   * 
   * @param dateStr uuuuMMdd
   * @param days 日数
   * @return uuuuMMdd
   */
  public static String getAddDay(String dateStr, Long days) {
    return LocalDate.parse(dateStr, FORMATTER_uuuuMMdd).plusDays(days).format(FORMATTER_uuuuMMdd);
  }

  /**
   * 現在の時刻取得（uuuuMMddHHmmss）
   * 
   * @return
   */
  public static String getCurrentDate() {
    LocalDateTime d = LocalDateTime.now();
    return d.format(FORMATTER_uuuuMMdd);
  }


  /**
   * 現在の時刻取得（uuuuMMddHHmmss）
   * 
   * @return
   */
  public static String getCurrentDateTime() {
    LocalDateTime d = LocalDateTime.now();
    return d.format(FORMATTER_uuuuMMddHHmmss);
  }

  /**
   * システム時刻取得
   * 
   * @return YYYY-MM-DD HH24:MI:SS
   */
  public static String getSystemDateCurrentTimestamp() {
    return LocalDateTime.now().format(uuuu_MM_dd_hh_mm_ss_DTF);
  }

  /**
   * 日本語表示の日付取得（yyyy年MM月dd日 HH時MM分）
   * 
   * @param dateStr yyyyMMddHHmmss
   * @return
   * @throws ParseException
   */
  public static String convDspDate(String dateStr) {
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
      Calendar calender = Calendar.getInstance();
      calender.setTime(dateFormat.parse(dateStr));
      return String.format("%d年%02d月%02d日 %02d時%02d分", calender.get(Calendar.YEAR),
          calender.get(Calendar.MONTH) + 1, calender.get(Calendar.DAY_OF_MONTH),
          calender.get(Calendar.HOUR_OF_DAY), calender.get(Calendar.MINUTE));
    } catch (Exception e) {
      return "";
    }
  }

  
  /**
   * yyyyMMddHHmmss -> yyyy/MM/dd
   * 
   * @param dateStr yyyyMMddHHmmss
   * @return
   * @throws ParseException
   */
  public static String convSlashDate(String dateStr) {
    return LocalDateTime.parse(dateStr, FORMATTER_uuuuMMddHHmmss).format(FORMATTER_uuuuMMdd_SLASH);
  }

  /**
   * yyyyMMddHHmmss -> HH:mm
   * 
   * @param dateStr yyyyMMddHHmmss
   * @return
   * @throws ParseException
   */
  public static String getHHmmSlash(String dateStr) {
    return LocalDateTime.parse(dateStr, FORMATTER_uuuuMMddHHmmss).format(FORMATTER_hhmm_SLASH);
  }

  /**
   * サービス利用時間を「%d時間%d分」形式に変換
   * 
   * @param time
   * @return
   */
  public static String convSvTime(String time) {
    if (StringUtils.isBlank(time))
      return null;
    try {
      int intTime = Integer.parseInt(time);
      return intTime >= 100 ? String.format("%d時間%02d分", intTime / 100, intTime % 100)
          : String.format("%02d分", intTime % 100);
    } catch (Exception e) {
      return null;
    }
  }
  
  /** QR URL作成 */
  public static String createQrUrl(String domain, String orderNo, String reservationNo) {
    StringBuilder retStr = new StringBuilder("https://");
    retStr.append(domain);
    retStr.append("/apps/user/ticket/?orderNo=");
    retStr.append(orderNo);
    retStr.append("&reservationNo=");
    retStr.append(reservationNo);
    return retStr.toString();
  }
  
}
