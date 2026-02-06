package maint.enums;

import lombok.Getter;

/**
 * 運賃区分
 *
 * @author g.ko
 */
@Getter
public enum LineChargeDivision implements ValidateEnumsInterFace {

  /** 均一運賃 */
  FLAT_FARE("均一運賃", 1)
  /** 三角運賃 */
  , TRIANGULAR_FARE("三角運賃", 2);

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
  LineChargeDivision(String text, Integer value) {
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
    for (LineChargeDivision v : values()) {
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
  public static LineChargeDivision getByValue(Integer value) {
    for (LineChargeDivision e : LineChargeDivision.values()) {
      if (e.getValue().equals(value)) {
        return e;
      }
    }
    return null;
  }
}
