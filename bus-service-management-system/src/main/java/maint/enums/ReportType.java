package maint.enums;

import lombok.Getter;

/**
 * 出力対象
 *
 * @author JEON
 */
@Getter
public enum ReportType implements ValidateEnumsInterFace {

  /** 停留所一覧 */
  BUS_STOP_LIST("停留所一覧", "01"),
  /** 標柱一覧 */
  SIGN_POST_LIST("標柱一覧", "02"),
  /** 標柱停留所一覧 */
  SIGN_POST_STOP_LIST("標柱停留所一覧", "03"),
  /** 系統一覧 */
  SYSTEM_LIST("系統一覧", "04"),
  /** 便ダイヤ一覧 */
  BUS_SCHEDULE_LIST("便ダイヤ一覧", "05");

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
  ReportType(String text, String value) {
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
    for (ReportType v : values()) {
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
  public static ReportType getByValue(String value) {
    for (ReportType e : ReportType.values()) {
      if (e.getValue().equals(value))
        return e;
    }
    return null;
  }
}
