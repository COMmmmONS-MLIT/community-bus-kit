package maint.enums;

import lombok.Getter;

/**
 * ユーザ向けメール区分ENUM
 *
 * @author administrator
 */
@Getter
public enum UserMailDivisionEnums {

  /** ログイン情報通知メール */
  USER_REGISTER("01", "【管理WEB】ユーザ登録のお知らせ", "userRegister.ftl"),
  /** パスワード再発行メール */
  USER_PASSWORD_REISSUE("02", "【管理WEB】パスワード再発行のお知らせ", "userPassReissue.ftl"),
  ;

  /**
   * コンストラクタ
   *
   * @param value       値
   * @param subject     メールタイトル
   * @param ftlFileName ファイル名
   */
  UserMailDivisionEnums(String value, String subject, String ftlFileName) {
    this.value = value;
    this.subject = subject;
    this.ftlFileName = ftlFileName;
  }

  /** メール送信区分 */
  private final String value;
  /** タイトル */
  private final String subject;
  /** FTL ファイル名 */
  private final String ftlFileName;

  /**
   * 値からEnum取得
   *
   * @param value 値
   * @return Enum
   */
  public static UserMailDivisionEnums getByValue(String value) {
    for (UserMailDivisionEnums e : values()) {
      if (e.getValue().equals(value)) {
        return e;
      }
    }
    return null;
  }

}
