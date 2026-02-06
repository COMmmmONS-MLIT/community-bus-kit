package maint.enums;

import lombok.Getter;

/**
 * 支払タイミング
 *
 * @author g.ko
 */
@Getter
public enum PaymentMethod implements ValidateEnumsInterFace {

  /** 乗車後に支払う */
  PAY_AFTER_BOARDING("乗車後に支払う", 0)
  /** 乗車前に支払う */
  , PAY_BEFORE_BOARDING("乗車前に支払う", 1);

  /** 名称 */
  private final String text;
  /** 値 */
  private final Integer value;

  /**
   * コンストラクタ
   *
   * @param text 名称
   * @param value 値
   */
  PaymentMethod(String text, Integer value) {
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
    for (PaymentMethod v : values()) {
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
  public static PaymentMethod getByValue(Integer value) {
    for (PaymentMethod e : PaymentMethod.values()) {
      if (e.getValue().equals(value)) {
        return e;
      }
    }
    return null;
  }
}
