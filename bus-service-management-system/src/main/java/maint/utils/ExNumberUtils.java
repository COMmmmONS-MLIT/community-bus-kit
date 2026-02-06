package maint.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * 数値関連 Utils
 *
 * @author administrator
 */
public final class ExNumberUtils {

  /**
   * コンストラクタ
   */
  private ExNumberUtils() {
  }

  /** 文字列=>Long型数値変換(数値以外ならnull) */
  public static Long toLongReturnNullUnlessNumber(String str) {
    return toLong(str, null);
  }

  /** 文字列=>Long型数値変換(数値以外なら0リタン) */
  public static Long toLongReturnZeroUnlessNumber(String str) {
    return toLong(str, 0L);
  }

  /** 文字列=>Long型数値変換(数値以外なら指定デフォルト値) */
  public static Long toLong(String str, Long defaultVal) {
    if (str == null) {
      return defaultVal;
    }
    try {
      return Long.parseLong(str);
    } catch (NumberFormatException nfe) {
      return defaultVal;
    }
  }

  /** 文字列数値判定（数値ならTrue） */
  public static boolean isValidNumber(String str) {
    return ExObjectUtils.isNotBlank(toLong(str, null));
  }

  /**
   * Integer, Long, Double, Short</br> null or zero チェック
   *
   * @param number 対象数字
   * @return true : null or zero, false : 0以上の数字
   */
  public static boolean isNullOrZero(final Number number) {
    return number == null || (number instanceof Integer ?
        number.intValue() == 0 :
        number instanceof Long ?
            number.longValue() == 0 :
            number instanceof Double ?
                number.doubleValue() == 0 :
                number instanceof Short ? number.shortValue() == 0 : number.floatValue() == 0);
  }

  /**
   * Integer, Long, Double, Short</br> null or zero以下チェック
   *
   * @param number 対象数字
   * @return true : null or 0以下, false : 1以上の数字
   */
  public static boolean isNullOrUnderOne(final Number number) {
    return number == null || (number instanceof Integer ?
        number.intValue() <= 0 :
        number instanceof Long ?
            number.longValue() <= 0 :
            number instanceof Double ?
                number.doubleValue() <= 0 :
                number instanceof Short ? number.shortValue() <= 0 : number.floatValue() <= 0);
  }

  /**
   * カンマ区切りの数字を返す
   *
   * @param number
   * @return カンマ区切りの数字
   */
  public static String numberFormatComma(long number) {
    return NumberFormat.getNumberInstance().format(number);
  }

  /**
   * 文字列=>BigDecimal型数値変換(数値以外なら指定デフォルト値)
   *
   * @param str        文字列
   * @param defaultVal デフォルト値
   * @return BigDecimal型数値
   */
  public static BigDecimal toBigDecimal(String str, BigDecimal defaultVal) {
    if (str == null) {
      return defaultVal;
    }
    try {
      return new BigDecimal(str);
    } catch (NumberFormatException nfe) {
      return defaultVal;
    }
  }

  /**
   * 文字列=>BigDecimal型数値変換(数値以外ならnull)
   *
   * @param str 文字列
   * @return BigDecimal型数値
   */
  public static BigDecimal toBigDecimalReturnNullUnlessNumber(String str) {
    return toBigDecimal(str, null);
  }

}
