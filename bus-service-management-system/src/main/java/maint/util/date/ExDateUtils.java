package maint.util.date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 日付関連 Utils<br>
 *
 * <p>
 * <b>コーディング規約</b><br>
 * Javaクラス内の日付については、String型の「uuuu-MM-dd」または「uuuu-MM-dd HH:mm:ss」形式のみとする。<br>
 * </p>
 * <p>
 * <b>禁止事項</b><br>
 * <span>SimpleDateFormatは使用しないこと！！</span>
 * </p>
 *
 * @author administrator
 */
public class ExDateUtils {

  /** ロガー */
  protected static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * 現在日時を取得
   *
   * @return 現在日時（uuuu-MM-dd HH:mm:ss）
   */
  public static String getCurrentDateTime() {
    return LocalDateTime.now().format(ExDateFormat.uuuuMMddHHmmss_HYPHEN);
  }

  /**
   * 現在日付を取得
   *
   * @return 現在日付（uuuu-MM-dd）
   */
  public static String getCurrentDate() {
    return LocalDate.now().format(ExDateFormat.uuuuMMdd_HYPEN);
  }

  /**
   * 文字列日時（uuuu-MM-dd HH:mm:ss）をLocalDateTimeにパース
   *
   * @param dateTime 文字列日時（uuuu-MM-dd HH:mm:ss）
   * @return LocalDateTime
   */
  public static LocalDateTime parseLocalDateTime(String dateTime) {
    try {
      return LocalDateTime.parse(dateTime, ExDateFormat.uuuuMMddHHmmss_HYPHEN);
    } catch (DateTimeParseException e) {
      // 引数の日付形式が「uuuu-MM-dd HH:mm:ss」以外の場合、ログを出力してエラー発生
      LOGGER.error(
          "【日付Utils】引数の日付形式(uuuu-MM-dd HH:mm:ss)が正しくありません。引数=" + dateTime);
      throw e;
    }
  }

  /**
   * 文字列日付（uuuu-MM-dd）をLocalDateにパース
   *
   * @param date 文字列日付（uuuu-MM-dd）
   * @return LocalDate
   */
  public static LocalDate parseLocalDate(String date) {
    try {
      return LocalDate.parse(date, ExDateFormat.uuuuMMdd_HYPEN);
    } catch (DateTimeParseException e) {
      // 引数の日付形式が「uuuu-MM-dd」以外の場合、ログを出力してエラー発生
      LOGGER.error("【日付Utils】引数の日付形式(uuuu-MM-dd)が正しくありません。引数=" + date);
      throw e;
    }
  }

  /**
   * 日時のフォーマットを変換（画面表示兼用）<br>
   * <br>
   * ※変換元の日時は「uuuu-MM-dd HH:mm:ss」形式であること<br> 例）uuuu-MM-dd HH:mm:ss -> uuuu年MM月dd日<br> 例）uuuu-MM-dd
   * HH:mm:ss -> uuuu/MM/dd HH:mm:ss<br> 例）uuuu-MM-dd HH:mm:ss -> HHmmss
   *
   * @param dateTime     文字列日時（uuuu-MM-dd HH:mm:ss）
   * @param targetFormat 出力したい日付時刻フォーマッター
   * @return 変換された日付
   */
  public static String convertDateTimeFormat(String dateTime, DateTimeFormatter targetFormat) {
    return parseLocalDateTime(dateTime).format(targetFormat);
  }

  /**
   * 日付の比較<br>
   * <br>
   * date1 < date2 = -1<br> date1 == date2 = 0<br> date1 > date2 = 1
   *
   * @param date1 文字列日付（uuuu-MM-dd）
   * @param date2 文字列日付（uuuu-MM-dd）
   * @return 比較結果
   */
  public static int compareLocalDate(String date1, String date2) {
    return parseLocalDate(date1).compareTo(parseLocalDate(date2));
  }

  /**
   * 日時に分を足す
   *
   * @param dateTime 文字列日時（uuuu-MM-dd HH:mm:ss）
   * @param addMins  加算する分
   * @return 分加算後の日時
   */
  public static String addMinsToDateTime(String dateTime, int addMins) {
    return parseLocalDateTime(dateTime).plusMinutes(addMins)
        .format(ExDateFormat.uuuuMMddHHmmss_HYPHEN);
  }

  /**
   * 日付に日数を引く
   *
   * @param date 文字列日付（uuuu-MM-dd）
   * @return 日数減算後の日付
   */
  public static String minusDaysToDate(String date, int minusDays) {
    return parseLocalDate(date).minusDays(minusDays).format(ExDateFormat.uuuuMMdd_HYPEN);
  }
}
