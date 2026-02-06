package maint.enums;

import lombok.Getter;

/**
 * 運休区分
 *
 * @author jeon
 */
@Getter
public enum SuspensionDivision implements ValidateEnumsInterFace {

  /** 通常運行 */
  OPERATION("通常運行", 0)
  /** 運休便 */
  , SUSPENDED("運休便", 1);

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
  SuspensionDivision(String text, Integer value) {
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
    for (SuspensionDivision v : values()) {
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
  public static SuspensionDivision getByValue(Integer value) {
    for (SuspensionDivision e : SuspensionDivision.values()) {
      if (e.getValue().equals(value))
        return e;
    }
    return null;
  }
}
