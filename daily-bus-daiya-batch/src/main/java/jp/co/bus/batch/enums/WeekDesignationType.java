package jp.co.bus.batch.enums;

import lombok.Getter;

/**
 * 曜日指定区分
 *
 * @author g.ko
 */
@Getter
public enum WeekDesignationType {

  /** 祝日優先 */
  HOLIDAY_PRIORITY("祝日優先", "0")
  /** 曜日優先 */
  , DAY_OF_WEEK_PRIORITY("曜日優先", "1");

  /** 名称 */
  private final String text;
  /** 値 */
  private final String value;

  /**
   * コンストラクタ
   *
   * @param text  名称
   * @param value 値
   */
  WeekDesignationType(String text, String value) {
    this.text = text;
    this.value = value;
  }

  /**
   * 値から名称を取得
   *
   * @param value 値
   * @return 名称
   */
  public static String getTextByValue(String value) {
    for (WeekDesignationType v : values()) {
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
  public static WeekDesignationType getByValue(String value) {
    for (WeekDesignationType e : WeekDesignationType.values()) {
      if (e.getValue().equals(value))
        return e;
    }
    return null;
  }

}
