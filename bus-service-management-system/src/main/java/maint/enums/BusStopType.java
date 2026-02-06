package maint.enums;

import lombok.Getter;

/**
 * 停留所・標柱区分
 *
 * @author g.ko
 */
@Getter
public enum BusStopType implements ValidateEnumsInterFace {

  /** 標柱 */
  ROAD_SIGN("標柱", 0)
  /** 停留所 */
  , BUS_STOP("停留所", 1);

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
  BusStopType(String text, Integer value) {
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
    for (BusStopType v : values()) {
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
  public static BusStopType getByValue(Integer value) {
    for (BusStopType e : BusStopType.values()) {
      if (e.getValue().equals(value))
        return e;
    }
    return null;
  }
}
