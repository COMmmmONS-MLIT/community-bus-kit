package jp.co.bus.batch.enums;

import lombok.Getter;

/**
 * 曜日区分
 *
 * @author g.ko
 */
@Getter
public enum DayWeekType {

  /** 日曜 */
  SUNDAY("日", "1")
  /** 月曜 */
  , MONDAY("月", "2")
  /** 火曜 */
  , TUESDAY("火", "3")
  /** 水曜 */
  , WEDNESDAY("水", "4")
  /** 木曜 */
  , THURSDAY("木", "5")
  /** 金曜 */
  , FRIDAY("金", "6")
  /** 土曜 */
  , SATURDAY("土", "7")
  /** 祝日 */
  , HOLIDAY("祝日", "10");

  /** 名称 */
  private final String text;
  /** 値 */
  private final String value;

  /**
   * コンストラクタ
   *
   * @param text 名称
   * @param value 値
   */
  DayWeekType(String text, String value) {
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
    for (DayWeekType v : values()) {
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
  public static DayWeekType getByValue(String value) {
    for (DayWeekType e : DayWeekType.values()) {
      if (e.getValue().equals(value)) {
        return e;
      }
    }
    return null;
  }

  public static String getValueByJapanese(String jp) {
    for (DayWeekType t : values()) {
      if (t != HOLIDAY && t.getText().equals(jp)) {
        return t.getValue();
      }
    }
    return null;
  }

}
