package maint.enums;

import lombok.Getter;

/**
 * パスワード変更済みフラグ
 *
 * @author g.ko
 */
@Getter
public enum PwdChangedFlag implements ValidateEnumsInterFace {

  /** 変更前 */
  NOT_CHANGED("変更前", 0),
  /** 変更済 */
  CHANGED("変更済", 1),
  ;

  /**
   * コンストラクタ
   *
   * @param text  名称
   * @param value 値
   */
  PwdChangedFlag(String text, Integer value) {
    this.text = text;
    this.value = value;
  }

  /** 名称 */
  private final String text;

  /** 値 */
  private final Integer value;

}
