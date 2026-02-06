package maint.enums;

import lombok.Getter;

/**
 * 退職済みフラグ
 *
 * @author g.ko
 */
@Getter
public enum RetirementFlag implements ValidateEnumsInterFace {

  /** 在職 */
  EMPLOYED("在職", 0)
  /** 退職 */
  , RETIRED("退職", 1);

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
  RetirementFlag(String text, Integer value) {
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
    for (RetirementFlag v : values()) {
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
  public static RetirementFlag getByValue(Integer value) {
    for (RetirementFlag e : RetirementFlag.values()) {
      if (e.getValue().equals(value))
        return e;
    }
    return null;
  }
}
