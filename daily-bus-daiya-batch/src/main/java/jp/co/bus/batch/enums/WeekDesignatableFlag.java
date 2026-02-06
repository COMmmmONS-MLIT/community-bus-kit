package jp.co.bus.batch.enums;

import lombok.Getter;

/**
 * 曜日指定可否区分
 *
 * @author g.ko
 */
@Getter
public enum WeekDesignatableFlag {

  /** 指定不可 */
  DESIGNATION_DISABLED("指定不可", 0)
  /** 指定可 */
  , DESIGNATION_ENABLED("指定可", 1);

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
  WeekDesignatableFlag(String text, Integer value) {
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
    for (WeekDesignatableFlag v : values()) {
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
  public static WeekDesignatableFlag getByValue(Integer value) {
    for (WeekDesignatableFlag e : WeekDesignatableFlag.values()) {
      if (e.getValue().equals(value))
        return e;
    }
    return null;
  }
}
