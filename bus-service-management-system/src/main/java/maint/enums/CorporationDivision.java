package maint.enums;

import lombok.Getter;

/**
 * 会社区分
 *
 * @author y.kim
 */
@Getter
public enum CorporationDivision implements ValidateEnumsInterFace {

  /** 管理会社 */
  MANAGE_CORP("管理会社", "01")
  /** 幹事会社 */
  , CALC_CORP("幹事会社", "02")
  /** 運行会社 */
  , SERVICE_CORP("運行会社", "03");

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
  CorporationDivision(String text, String value) {
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
    for (CorporationDivision v : values()) {
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
  public static CorporationDivision getByValue(String value) {
    for (CorporationDivision e : CorporationDivision.values()) {
      if (e.getValue().equals(value))
        return e;
    }
    return null;
  }

}
