package maint.enums;

import lombok.Getter;

/**
 * 運行対象外フラグ
 *
 * @author g.ko
 */
@Getter
public enum OperationCancelFlag implements ValidateEnumsInterFace {

  /** 運行 */
  OPERATING("運行", 0)
  /** 運行対象外 */
  , EXCLUDED("運行対象外", 1);

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
  OperationCancelFlag(String text, Integer value) {
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
    for (OperationCancelFlag v : values()) {
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
  public static OperationCancelFlag getByValue(Integer value) {
    for (OperationCancelFlag e : OperationCancelFlag.values()) {
      if (e.getValue().equals(value))
        return e;
    }
    return null;
  }
}
