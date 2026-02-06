package maint.enums;

import lombok.Getter;

/**
 * メール区分ENUM
 *
 * @author sangsoo.lee
 */
@Getter
public enum MailDivisionEnums {

  /** 管理者2要素認証メール送信 */
  USERS_SEND_TWO_FACTOR("【コミュニティバス管理システム】２要素認証メール", "UsersTwoFactorMail.ftl"),
  ;

  /**
   * コンストラクタ
   *
   * @param subject     メールタイトル
   * @param ftlFileName ファイル名
   */
  MailDivisionEnums(String subject, String ftlFileName) {
    this.subject = subject;
    this.ftlFileName = ftlFileName;
  }

  /** タイトル */
  private final String subject;
  /** FTL ファイル名 */
  private final String ftlFileName;

}
