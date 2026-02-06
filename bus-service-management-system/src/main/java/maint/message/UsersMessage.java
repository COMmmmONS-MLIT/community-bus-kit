package maint.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 管理者メッセージ
 *
 * @author administrator
 */
@Getter
@AllArgsConstructor
public enum UsersMessage {

  /** FAIL_NOT_INPUT_PASSWORD */
  FAIL_NOT_INPUT_PASSWORD("パスワード未入力エラー", "message.error.not.input.pass"),
  /** FAIL_INPUT_CURRENT_PASSWORD */
  FAIL_INPUT_CURRENT_PASSWORD("現パスワード入力形式不正", "message.error.current.pass.input"),
  /** FAIL_INPUT_NEW_PASSWORD */
  FAIL_INPUT_NEW_PASSWORD("新パスワード入力形式不正", "message.error.new.pass.input"),
  /** FAIL_INVALID_PASSWORD */
  FAIL_INVALID_PASSWORD("入力パスワード不正", "message.error.pass.invalid"),
  /** FAIL_NOT_CHANGE_PASSWORD */
  FAIL_NOT_CHANGE_PASSWORD("変更パスワード不正", "message.error.pass.not.changed"),
  /** SUCCESS_CHANGE_NEW_PASSWORD */
  SUCCESS_CHANGE_NEW_PASSWORD("パスワード変更成功", "message.success.change.new.password"),
  /** ERROR_USERS_EMAIL_DUPLE */
  ERROR_USERS_EMAIL_DUPLE("Email重複エラー", "message.error.users.email.duple"),
  /** ERROR_USERS_PASS_EQUAL_ID */
  ERROR_USERS_PASS_EQUAL_ID("パスワードとログインID同一禁止",
      "message.error.users.password.equal.email");

  /** 概要 */
  private final String overview;
  /** メッセージ */
  private final String message;

}
