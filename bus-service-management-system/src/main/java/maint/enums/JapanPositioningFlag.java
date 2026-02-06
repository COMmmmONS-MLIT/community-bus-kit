package maint.enums;

import lombok.Getter;

/**
 * 測地系区分フラグ
 *
 * @author JEKAL.H
 */
@Getter
public enum JapanPositioningFlag implements ValidateEnumsInterFace {

  /** 世界測地系 */
  WORLD_POSITIONING("世界測地系", 0)
  /** 日本測地系 */
  , JAPAN_POSITIONING("日本測地系", 1);

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
  JapanPositioningFlag(String text, Integer value) {
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
    for (JapanPositioningFlag v : values()) {
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
  public static JapanPositioningFlag getByValue(Integer value) {
    for (JapanPositioningFlag e : JapanPositioningFlag.values()) {
      if (e.getValue().equals(value))
        return e;
    }
    return null;
  }
}
