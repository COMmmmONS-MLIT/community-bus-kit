package maint.enums;

import lombok.Getter;

/**
 * 往路、復路区分
 *
 * @author jeon
 */
@Getter
public enum LineDetailServiceDivision implements ValidateEnumsInterFace {

  /** 復路 */
  RETURN_TRIP("復路", 0)
  /** 往路 */
  , OUTBOUND_TRIP("往路", 1);

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
  LineDetailServiceDivision(String text, Integer value) {
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
    for (LineDetailServiceDivision v : values()) {
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
  public static LineDetailServiceDivision getByValue(Integer value) {
    for (LineDetailServiceDivision e : LineDetailServiceDivision.values()) {
      if (e.getValue().equals(value))
        return e;
    }
    return null;
  }
}
