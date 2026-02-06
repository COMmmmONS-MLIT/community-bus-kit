package jp.co.bus.batch.utils.date;

import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日付関連 Utils<br>
 *
 * <p>
 * <b>コーディング規約</b><br>
 * Javaクラス内の日付については、String型の「uuuu-MM-dd」または「uuuu-MM-dd HH:mm:ss」形式のみとする。<br>
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
   * 文字列がJava内で使用可能な「yyyy-MM-dd」形式であるかチェック
   *
   * @param input 入力値
   * @return true:正常、false:異常
   */
  public static boolean isFormatDate(String input) {
    try {
      LocalDate.parse(input, ExDateFormat.uuuuMMdd_HYPEN);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 文字列がJava内で使用可能な「yyyy-MM-dd HH:mm:ss」形式であるかチェック
   *
   * @param input 入力値
   * @return true:正常、false:異常
   */
  public static boolean isFormatDateTime(String input) {
    try {
      LocalDateTime.parse(input, ExDateFormat.uuuuMMddHHmmss_HYPHEN);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 文字列日時（uuuu-MM-dd HH:mm:ss）をLocalDateTimeにパース
   *
   * @param dateTime 文字列日時（uuuu-MM-dd HH:mm:ss）
   * @return LocalDateTime
   */
  private static LocalDateTime parseLocalDateTime(String dateTime) {
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
  private static LocalDate parseLocalDate(String date) {
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
   * @param dateTime 文字列日時（uuuu-MM-dd HH:mm:ss）
   * @param targetFormat 出力したい日付時刻フォーマッター
   * @return 変換された日付
   */
  public static String convertDateTimeFormat(String dateTime, DateTimeFormatter targetFormat) {
    return parseLocalDateTime(dateTime).format(targetFormat);
  }

  /**
   * 日付のフォーマットを変換する（画面表示兼用）<br>
   * <br>
   * ※変換元の日付は「uuuu-MM-dd」形式であること<br> 例）uuuu-MM-dd -> uuuu年MM月dd日<br> 例）uuuu-MM-dd HH:mm:ss ->
   * uuuu/MM/dd（日時からLocalDate型に変換できる）
   *
   * @param date 文字列日付（uuuu-MM-dd）
   * @param targetFormat 出力したい日付時刻フォーマッター
   * @return 変換された日付
   */
  public static String convertDateFormat(String date, DateTimeFormatter targetFormat) {
    return parseLocalDate(date).format(targetFormat);
  }

  /**
   * 日時の比較<br>
   * <br>
   * dateTime1 < dateTime2 = -1<br> dateTime1 == dateTime2 = 0<br> dateTime1 > dateTime2 = 1
   *
   * @param dateTime1 文字列日時1（uuuu-MM-dd HH:mm:ss）
   * @param dateTime2 文字列日時2（uuuu-MM-dd HH:mm:ss）
   * @return 比較結果
   */
  public static int compareLocalDateTime(String dateTime1, String dateTime2) {
    return parseLocalDateTime(dateTime1).compareTo(parseLocalDateTime(dateTime2));
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
   * 日時に月数を足す
   *
   * @param dateTime 文字列日時（uuuu-MM-dd HH:mm:ss）
   * @param addMonths 加算する月数
   * @return 月数加算後の日時
   */
  public static String addMonthsToDateTime(String dateTime, int addMonths) {
    return parseLocalDateTime(dateTime).plusMonths(addMonths)
        .format(ExDateFormat.uuuuMMddHHmmss_HYPHEN);
  }

  /**
   * 日時に日数を足す
   *
   * @param dateTime 文字列日時（uuuu-MM-dd HH:mm:ss）
   * @param addDays 加算する日数
   * @return 日数加算後の日時
   */
  public static String addDaysToDateTime(String dateTime, int addDays) {
    return parseLocalDateTime(dateTime).plusDays(addDays)
        .format(ExDateFormat.uuuuMMddHHmmss_HYPHEN);
  }

  /**
   * 日時に分を足す
   *
   * @param dateTime 文字列日時（uuuu-MM-dd HH:mm:ss）
   * @param addMins 加算する分
   * @return 分加算後の日時
   */
  public static String addMinsToDateTime(String dateTime, int addMins) {
    return parseLocalDateTime(dateTime).plusMinutes(addMins)
        .format(ExDateFormat.uuuuMMddHHmmss_HYPHEN);
  }

  /**
   * 日付に月数を足す
   *
   * @param date 文字列日付（uuuu-MM-dd）
   * @param addMonths 加算する月数
   * @return 月数加算後の日付
   */
  public static String addMonthsToDate(String date, int addMonths) {
    return parseLocalDate(date).plusMonths(addMonths).format(ExDateFormat.uuuuMMdd_HYPEN);
  }

  /**
   * 日付に日数を足す
   *
   * @param date 文字列日付（uuuu-MM-dd）
   * @param addDays 加算する日数
   * @return 日数加算後の日付
   */
  public static String addDaysToDate(String date, int addDays) {
    return parseLocalDate(date).plusDays(addDays).format(ExDateFormat.uuuuMMdd_HYPEN);
  }

  /**
   * 日付に日数を引く
   *
   * @param date 文字列日付（uuuu-MM-dd）
   * @param addDays 減算する日数
   * @return 日数減算後の日付
   */
  public static String minusDaysToDate(String date, int minusDays) {
    return parseLocalDate(date).minusDays(minusDays).format(ExDateFormat.uuuuMMdd_HYPEN);
  }

  /**
   * 日時期間の日数を取得<br>
   * <br>
   * 例）2022-11-12 12:00:00 ~ 2022-12-13 12:00:00の場合、31（日）が出力
   *
   * @param startDate 開始日時（uuuu-MM-dd HH:mm:ss）
   * @param endDate 終了日時（uuuu-MM-dd HH:mm:ss）
   * @return 期間の日数
   */
  public static long getDaysBetweenDateTimes(String startDate, String endDate) {
    return ChronoUnit.DAYS.between(parseLocalDateTime(startDate), parseLocalDateTime(endDate));
  }

  /**
   * 日付期間の日数を取得<br>
   * <br>
   * 例）2022-11-12 ~ 2022-12-13の場合、31（日）が出力
   *
   * @param startDate 開始日（uuuu-MM-dd）
   * @param endDate 終了日（uuuu-MM-dd）
   * @return 期間の日数
   */
  public static long getDaysBetweenDates(String startDate, String endDate) {
    return ChronoUnit.DAYS.between(parseLocalDate(startDate), parseLocalDate(endDate));
  }

  /**
   * 日付期間の年数を取得<br>
   * <br>
   * 例）2019-01-01 ~ 2022-01-01の場合、（年）が出力
   *
   * @param startDate 開始日（uuuu-MM-dd）
   * @param endDate 終了日（uuuu-MM-dd）
   * @return 期間の年数
   */
  public static long getYearsBetweenDates(String startDate, String endDate) {
    return ChronoUnit.YEARS.between(parseLocalDate(startDate), parseLocalDate(endDate));
  }

  /**
   * 日付から日本語の曜日を取得<br>
   * <br>
   * 例）2022-11-08 -> 火
   *
   * @param date 文字列日付（uuuu-MM-dd）
   * @return 日本語の曜日
   */
  public static String getJapaneseDayOfWeek(String date) {
    // TextStyle.NARROW又はSHORT（=火）、TextStyle.FULL（=火曜日）
    return parseLocalDate(date).getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.JAPANESE);
  }

  /**
   * 対象日付の初日を取得
   *
   * @param date 文字列日付（uuuu-MM-dd）
   * @return 初日の日付（uuuu-MM-dd）
   */
  public static String getFirstDayOfMonth(String date) {
    return parseLocalDate(date).withDayOfMonth(1).format(ExDateFormat.uuuuMMdd_HYPEN);
  }

  /**
   * 対象日付の末日を取得
   *
   * @param date 文字列日付（uuuu-MM-dd）
   * @return 末日の日付（uuuu-MM-dd）
   */
  public static String getLastDayOfMonth(String date) {
    LocalDate ld = parseLocalDate(date);
    return ld.withDayOfMonth(ld.lengthOfMonth()).format(ExDateFormat.uuuuMMdd_HYPEN);
  }

  /**
   * 日付文字列の比較
   *
   * @param baseDate yyyy-MM-dd
   * @param targetDate yyyy-MM-dd
   * @return baseDate > targetDate 1,baseDate = targetDate 0, baseDate < targetDate -1
   */
  public static Integer compareStringDate(String baseDate, String targetDate) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date base = sdf.parse(baseDate);
      Date target = sdf.parse(targetDate);
      return base.compareTo(target);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 開始日～終了日間の日付を取得
   *
   * @param startDate 開始日（uuuu-MM-dd）
   * @param endDate 終了日（uuuu-MM-dd）
   * @return 開始日～終了日間の日付リスト
   */
  public static List<String> listDatesInclusive(String startDate, String endDate) {
    List<String> result = new ArrayList<>();
    for (LocalDate d = parseLocalDate(startDate); !d.isAfter(parseLocalDate(endDate));
        d = d.plusDays(1)) {
      result.add(d.format(ExDateFormat.uuuuMMdd_HYPEN));
    }
    return result;
  }
}
