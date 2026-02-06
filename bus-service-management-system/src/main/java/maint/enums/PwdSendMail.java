package maint.enums;

import lombok.Getter;

/**
 * パスワードメール送信フラグ
 *
 * @author ryuichi.kashiwai
 */
@Getter
public enum PwdSendMail implements ValidateEnumsInterFace {

  /** メール送信 */
  SEND_MAIL("メール送信", 1),
  /** 直接入力 */
  NOT_SEND_MAIL("直接入力", 0),
  ;

  /**
   * コンストラクタ
   *
   * @param text  名称
   * @param value 値
   */
  PwdSendMail(String text, Integer value) {
    this.text = text;
    this.value = value;
  }

  /** 名称 */
  private final String text;

  /** 値 */
  private final Integer value;

}
