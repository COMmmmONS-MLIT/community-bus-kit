package maint.utils;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 文字列関連 Utils
 *
 * @author administrator
 */
public class ExStringUtils extends StringUtils {

  /**
   * Null To Blank
   *
   * @param str
   * @return
   */
  public static String nullToBlank(String str) {
    return str != null ? str : "";
  }

  /**
   * 丸括弧結合文字列取得
   *
   * @param index   丸括弧が始まる配列Index
   * @param strings 文字列配列
   * @return 丸括弧結合文字列
   */
  public static String makeTextWithParentheses(int index, String... strings) {
    String ret = "";
    List<String> strList = Arrays.asList(strings);
    for (int i = 0; i < strList.size(); i++) {
      if (i >= index) {
        ret = new StringBuilder(ret).append("(").append(strList.get(i)).append(")").toString();
        continue;
      }
      ret = new StringBuilder(ret).append(strList.get(i)).toString();
    }
    return ret;
  }

  /**
   * コンマ区切り及び円Foramt文字列取得
   *
   * @param value 値
   * @return X, XXX円
   */
  public static String getMoneyFormatString(Object value) {
    NumberFormat fm = NumberFormat.getInstance();
    return fm.format(value) + "円";
  }

  /**
   * コンマ区切り及び回数Foramt文字列取得
   *
   * @param value 値
   * @return X, XXX回
   */
  public static String getCountFormatString(Object value) {
    NumberFormat fm = NumberFormat.getInstance();
    return fm.format(value) + "回";
  }

  /**
   * コンマ区切り及び距離（Κm）Foramt文字列取得
   *
   * @param value 値
   * @return X, XXXΚm
   */
  public static String getDistanceFormatString(Object value) {
    NumberFormat fm = NumberFormat.getInstance();
    return fm.format(value) + "Km";
  }

  /**
   * コンマ区切り及び距離（km）Foramt文字列取得
   *
   * @param value 値
   * @return X, XXXkm
   */
  public static String getDistanceFormatStringWithSmallkm(Object value) {
    NumberFormat fm = NumberFormat.getInstance();
    return fm.format(value) + "km";
  }

  /**
   * 最初の文字のみ、小文字に変換する
   *
   * @param str 文字列
   * @return 変換された文字列
   */
  public static String getLowerFirstChar(String str) {
    char[] chars = str.toCharArray();
    chars[0] += 32;
    return String.valueOf(chars);
  }

  /**
   * 渡された文字列が数値であるかチェック
   *
   * @param str 変換するDTOリスト
   * @return 変換後のMapリスト
   */
  public static boolean isNumber(String str) {
    try {
      Double.parseDouble(str);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  /**
   * ファイル名の禁則文字を大文字に変換
   *
   * @param str
   * @return 大文字に変換した禁則文字
   */
  public static String convertFileName(String str) {
    if (str == null)
      return null;
    str = str.replaceAll("\\\\", "￥");
    str = str.replaceAll("/", "／");
    str = str.replaceAll(":", "：");
    str = str.replaceAll("\\*", "＊");
    str = str.replaceAll("\"", "'");
    str = str.replaceAll("<", "＜");
    str = str.replaceAll(">", "＞");
    str = str.replaceAll("\\|", "｜");
    return str;
  }

  /**
   * 数値変換 String → long
   *
   * @param number 数値(String)
   * @return 数値(long)
   */
  public static long convertStringToLong(String number) {
    return Long.parseLong(number.replaceAll(",", ""));
  }

  /**
   * 数値変換 String → int
   *
   * @param number 数値(String)
   * @return 数値(int)
   */
  public static int convertStringToInteger(String number) {
    return Integer.parseInt(number.replaceAll(",", ""));
  }

  /**
   * Null -> 空白文字
   *
   * @param value
   * @return
   */
  public static String replaceNullToBlank(Object value) {
    if (value == null)
      return "";
    return value.toString();
  }

  public static int stringToNumber(String value) {
    try {
      return Integer.valueOf(value);
    } catch (Exception e) {
      return 0;
    }
  }
}
