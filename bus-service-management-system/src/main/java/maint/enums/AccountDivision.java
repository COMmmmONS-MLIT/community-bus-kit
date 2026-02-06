package maint.enums;

import lombok.Getter;

/**
 * アカウント区分
 *
 * @author g.ko
 */
@Getter
public enum AccountDivision implements ValidateEnumsInterFace {

  /** システム管理者 */
  SYSTEM_ADMIN("システム管理者", "01")
  /** 事業者 */
  , AGENCY("事業者", "02");

  /** 名称 */
  private final String text;
  /** 値 */
  private final String value;

  /**
   * コンストラクタ
   *
   * @param text  名称
   * @param value 値
   */
  AccountDivision(String text, String value) {
    this.text = text;
    this.value = value;
  }

  /**
   * 値から名称を取得
   *
   * @param value 値
   * @return 名称
   */
  public static String getTextByValue(String value) {
    for (AccountDivision v : values()) {
      if (v.getValue().equals(value)) {
        return v.getText();
      }
    }
    return "";
  }

  /**
   * 値でENUM取得
   *
   * @param value 値
   * @return ENUM
   */
  public static AccountDivision getByValue(String value) {
    for (AccountDivision e : AccountDivision.values()) {
      if (e.getValue().equals(value))
        return e;
    }
    return null;
  }

}
