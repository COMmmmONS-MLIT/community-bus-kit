package maint.exception;

import org.springframework.security.core.AuthenticationException;
import maint.message.AdminAuthErrorMessage;

/**
 * 管理者認証Exception
 *
 * @author administrator
 */
public class AdminAuthException extends AuthenticationException {

  /** Serial Version UID */
  private static final long serialVersionUID = -2761462357657431715L;

  /** 認証エラーメッセージ */
  private final AdminAuthErrorMessage adminAuthError;

  /**
   * コンストラクタ
   *
   * @param msg            メッセージ
   * @param adminAuthError 認証エラーメッセージ
   */
  public AdminAuthException(String msg, AdminAuthErrorMessage adminAuthError) {
    super(msg);
    this.adminAuthError = adminAuthError;
  }

  /**
   * 認証エラーメッセージ取得
   *
   * @return 認証エラーメッセージ
   */
  public AdminAuthErrorMessage getAdminAuthError() {
    return adminAuthError;
  }

}
