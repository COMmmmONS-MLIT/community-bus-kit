package maint.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ログインメッセージ
 *
 * @author masaya.tanaka
 */
@Getter
@AllArgsConstructor
public enum AdminAuthErrorMessage {

  /** BLANK_INPUT */
  BLANK_INPUT("未入力", "message.error.auth.blank"),
  /** LOGIN_FAIL */
  LOGIN_FAIL("認証失敗", "message.error.auth.login"),
  /** OTHER */
  OTHER("その他", "message.error.auth.other"),
  /** LOCK */
  LOCK("ロック", "message.error.auth.lock"),
  /** NOT_USE */
  NOT_USE("利用不可", "message.error.auth.not.use"),
  /** INVALID_PASSWORD */
  PASSREG("パスワードエラー", "message.error.auth.password"),
  /** NOT EXIST */
  NOT_EXIST("存在しないユーザ", "message.error.auth.not.exist");

  /** 概要 */
  private final String overview;
  /** メッセージ */
  private final String message;

}
