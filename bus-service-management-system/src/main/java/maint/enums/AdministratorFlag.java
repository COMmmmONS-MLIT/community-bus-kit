package maint.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 管理者フラグ（0：一般、1：管理者）
 *
 * @author administrator
 */
@Getter
@AllArgsConstructor
public enum AdministratorFlag implements ValidateEnumsInterFace {

  /** 一般 */
  NORMAL(0, "一般"),
  /** 管理者 */
  ADMIN(1, "管理者");

  /** 値 */
  private final Integer value;

  /** 名称 */
  private final String text;

  /**
   * 値から名称を取得
   *
   * @param value 値
   * @return 名称
   */
  public static String getTextByValue(Integer value) {
    for (AdministratorFlag v : values()) {
      if (v.getValue().equals(value)) {
        return v.getText();
      }
    }
    return "";
  }

}
