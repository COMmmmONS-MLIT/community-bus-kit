package maint.enums;

import lombok.Getter;

/**
 * 運賃区分
 *
 * @author r.hayashi
 */
@Getter
public enum UniformTranceferType implements ValidateEnumsInterFace {

  /** 乗換不可 */
  NO_TRANSFER("乗換不可", 0),
  /** 1度可能 */
  ONE_TRANSFER_ALLOWED("1度可能", 1),
  /** 2度可能 */
  TWO_TRANSFERS_ALLOWED("2度可能", 2),
  /** 乗換制限なし可能 */
  UNLIMITED_TRANSFERS("乗換制限なし可能", 9);

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
  UniformTranceferType(String text, Integer value) {
    this.text = text;
    this.value = value;
  }

  /**
   * 名称を取得
   *
   * @return 名称
   */
  public String getText() {
    return this.text;
  }

  /**
   * 値を取得
   *
   * @return 値
   */
  public Integer getValue() {
    return this.value;
  }

  /**
   * 値から名称を取得
   *
   * @param value 値
   * @return 名称
   */
  public static String getTextByValue(Integer value) {
    for (UniformTranceferType v : values()) {
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
  public static UniformTranceferType getByValue(Integer value) {
    for (UniformTranceferType e : UniformTranceferType.values()) {
      if (e.getValue().equals(value)) {
        return e;
      }
    }
    return null;
  }
}
