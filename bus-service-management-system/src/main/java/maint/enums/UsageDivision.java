package maint.enums;

import lombok.Getter;

/**
 * 利用区分
 *
 * @author y.kim
 */
@Getter
public enum UsageDivision implements ValidateEnumsInterFace {

  /** 利用不可 */
  DISABLED("利用不可", 0)
  /** 利用中 */
  , ACTIVE("利用中", 1);

  /** 名称 */
  private final String text;
  /** 値 */
  private final Integer value;

  /**
   * コンストラクタ
   *
   * @param text  名称
   * @param value 値
   */
  UsageDivision(String text, Integer value) {
    this.text = text;
    this.value = value;
  }

  /**
   * 値から名称を取得
   *
   * @param value 値
   * @return 名称
   */
  public static String getTextByValue(Integer value) {
    for (UsageDivision v : values()) {
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
  public static UsageDivision getByValue(Integer value) {
    for (UsageDivision e : UsageDivision.values()) {
      if (e.getValue().equals(value))
        return e;
    }
    return null;
  }
}
