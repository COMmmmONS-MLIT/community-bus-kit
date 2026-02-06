package maint.enums;

import lombok.Getter;

/**
 * 屋内バスターミナルフラグ
 *
 * @author jeon
 */
@Getter
public enum IndoorDivision implements ValidateEnumsInterFace {

  /** 屋内 */
  INDOOR("屋内", 1)
  /** 屋外 */
  , OUTDOOR("屋外", 0);

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
  IndoorDivision(String text, Integer value) {
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
    for (IndoorDivision v : values()) {
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
  public static IndoorDivision getByValue(Integer value) {
    for (IndoorDivision e : IndoorDivision.values()) {
      if (e.getValue().equals(value))
        return e;
    }
    return null;
  }
}
