package maint.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 乗降区分
 *
 * @author y.kim
 */
@Getter
public enum RideAlightDivision implements ValidateEnumsInterFace {

  /** 乗車可能 */
  RIDE_ONLY(0, "乗車"),
  /** 降車 */
  ALIGHT_ONLY(1, "降車"),
  /** 乗降車 */
  RIDE_ALIGHT(2, "乗降車");

  RideAlightDivision(Integer value, String text) {
    this.value = value;
    this.text = text;
  }

  /** 値 */
  private final Integer value;
  /** 名称 */
  private final String text;

  /**
   * コンストラクタ
   *
   * @param text 名称
   * @param value 値
   */
  RideAlightDivision(String text, Integer value) {
    this.text = text;
    this.value = value;
  }

  /**
   * 運行区分(lineDetailServiceDivision)に応じた順番でリストを返す
   *
   * @param serviceDivision 運行区分 (null, "0", "1"など)
   * @return 並び替えられたリスト
   */
  public static List<RideAlightDivision> getListByServiceDivision(String serviceDivision) {
    if ("0".equals(serviceDivision)) {
      // 下りの場合：降車 → 乗車 → 乗降車
      return Arrays.asList(ALIGHT_ONLY, RIDE_ONLY, RIDE_ALIGHT);
    } else {
      // 上り または 未選択：デフォルト順
      return Arrays.asList(values());
    }
  }

  /**
   * 値から名称を取得
   *
   * @param value 値
   * @return 名称
   */
  public static String getTextByValue(Integer value) {
    for (RideAlightDivision v : values()) {
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
  public static RideAlightDivision getByValue(Integer value) {
    for (RideAlightDivision e : RideAlightDivision.values()) {
      if (e.getValue().equals(value)) {
        return e;
      }
    }
    return null;
  }
}
