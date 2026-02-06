package maint.enums;

import lombok.Getter;

/**
 * 自転車持ち込み区分
 *
 * @author jeon
 */
@Getter
public enum BikesAllowedTypeDivision implements ValidateEnumsInterFace {
  /** 情報なし */
  NO_INFO("情報なし", 0),
  /** 1台は持込可 */
  ONE_VEHICLE_USE("1台は持込可", 1),
  /** 不可 */
  DISABLED("不可", 2);

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
  BikesAllowedTypeDivision(String text, Integer value) {
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
    for (BikesAllowedTypeDivision v : values()) {
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
  public static BikesAllowedTypeDivision getByValue(Integer value) {
    for (BikesAllowedTypeDivision e : BikesAllowedTypeDivision.values()) {
      if (e.getValue().equals(value))
        return e;
    }
    return null;
  }
}
