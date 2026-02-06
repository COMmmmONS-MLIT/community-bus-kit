package maint.utils;

import maint.constants.CommConstant;

/**
 * 正規表現 Utils
 *
 * @author administrator
 */
public class ExRegUtils {

  // Email正規表現
  public static final String emailReg = "^[\\w\\-._+]+@[\\w\\-._]+\\.[A-Za-z]+|$";
  // パスワード正規表現
  public static final String crewAppPasswordReg = "^|(?=.*[a-zA-Z])(?=.*[0-9])[0-9a-zA-Z]{8,15}$";
  // パスワード正規表現(空白も許可)
  public static final String passwordRegPermitBlank =
      "^|(?=.*[a-zA-Z])(?=.*[0-9])[0-9a-zA-Z]{8,20}$";
  // パスワード正規表現（半角英数字混在、記号1文字以上）
  public static final String passwordRegIncludeSign =
      "^|(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,20}$";
  // 乗務員端末用パスワード正規表現(空白も許可)
  public static final String passwordCrewRegPermitBlank =
      "^|(?=.*[a-zA-Z])(?=.*[0-9])[0-9a-zA-Z]{8,15}$";
  // IllegalCharacters
  public static final String illegalCharactersReg =
      ".*[" + CommConstant.ILLEGAL_CHARACTERS + "]+.*";
  // 半角のみ
  public static final String halfReg = "^[ -~｡-ﾟ]*$";
  // 英数字のみ
  public static final String engNumberReg = "^[a-zA-Z0-9]*$";
  // 英数字と-/のみ
  public static final String engNumberSymbolReg = "^[a-zA-Z0-9-/]*$";
  // 時間hh:mm:ss
  public static final String timeColonhhmmss = "^([0-1][0-9]|2[0-6]):[0-5][0-9]:[0-5][0-9]$";
  // 数字のみ
  public static final String onlyNumberReg = "^[0-9]*$";
  // ID正規表現
  public static final String idReg = "^[a-zA-Z0-9!@#$%^&*]{1,6}$";
  // 時間hhmm
  public static final String timehhmm = "^(0[3-9]|1[0-9]|2[0-6])[0-5][0-9]$";
  // 時間hh:mm
  public static final String timeColonhhmm = "^(0[3-9]|1[0-9]|2[0-6]):[0-5][0-9]$";
  // 電話番号(数値、-のみ。「-」有無は任意)
  public static final String phoneNumber = "^[0-9\\-]*$";
  // 法人番号（13桁数値）
  public static final String operationCompanyNo = "^\\d{13}$";
  // 交通種別（0～9999の数字）
  public static final String transportationType = "^(?:[0-9]{1,4})?$";
  /** 1～11桁の数値にマッチ（必須入力用） */
  public static final String numeric1to11Reg = "^\\d{1,11}$";
  /** 1～11桁の数値にマッチ（任意入力・空文字可） */
  public static final String optionalNumeric1to11Reg = "^(\\d{1,11})?$";
  /** 0～1のいずれか（任意入力・空文字可） */
  public static final String zeroToOneOptionalReg = "^[01]?$";
  /** 0～2のいずれか（任意入力・空文字可） */
  public static final String zeroToTwoOptionalReg = "^[0-2]?$";
  /** 時刻（HHMM 24時間表記）にマッチ（00:00～23:59） */
  public static final String timeHHmmReg = "^([01]\\d|2[0-3])[0-5]\\d$";
  /** 時刻（HHMM 24時間表記）にマッチ（00:00～23:59）（空白も許可） */
  public static final String timeHHmmWithBlankReg = "^|([01]\\d|2[0-3])[0-5]\\d$";
  // カレンダー年（2000〜2999）
  public static final String calendarYearReg  = "^2[0-9]{3}$";
  // カレンダー月（01〜12）
  public static final String calendarMonthReg = "^(0[1-9]|1[0-2])$";
  // 緯度、経度
  public static final String latitudeLongitude =  "^-?(90|[1-8]?[0-9])(\\.[0-9]{1,12})?,-?(180|1[0-7][0-9]|[1-9]?[0-9])(\\.[0-9]{1,12})?$";
  // 緯度、経度（空文字可）
  public static final String latitudeLongitudeWithBlankReg =  "^|-?(90|[1-8]?[0-9])(\\.[0-9]{1,12})?,-?(180|1[0-7][0-9]|[1-9]?[0-9])(\\.[0-9]{1,12})?$";
  // カラーコード
  public static final String hexColorReg = "^$|^[0-9A-Fa-f]{6}$";;

  /**
   * 正規表現と一致するかどうか？
   *
   * @param str 対象文字列
   * @param reg 正規表現
   * @return true: 成功 false: 失敗
   */
  public static boolean isValidString(String str, String reg) {
    return str.matches(reg);
  }

  /**
   * 正規表現(複数対象)と一致するかどうか？
   *
   * @param value (,で区切られた複数の対象)
   * @param reg   正規表現
   * @return true: 成功 false: 失敗
   */
  public static boolean isValidForArrayString(String value, String reg) {
    if (ExObjectUtils.isBlank(value)) {
      return true;
    }
    String[] array = value.trim().split(",");
    if (ExObjectUtils.isBlank(array)) {
      return false;
    }
    for (String str : array) {
      if (ExObjectUtils.isBlank(str)) {
        continue;
      }
      if (!ExRegUtils.isValidString(str.trim(), reg)) {
        return false;
      }
    }
    return true;
  }

}
