package maint.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import maint.exception.AdminAuthException;
import maint.form.LoginForm;
import maint.utils.ExObjectUtils;

/**
 * Spring Securityの認証失敗時に呼ばれるハンドラクラス
 *
 * @author administrator
 */
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  /** ログインフォーム名 */
  private static final String LOGIN_FORM = "loginForm";

  /** ログイン画面のパス */
  private static final String LOGIN_PAGE_PATH = "/auth/login/";

  /**
   * 認証失敗
   *
   * @param request   HttpServletRequest
   * @param response  HttpServletResponse
   * @param exception AuthenticationException
   * @throws IOException, ServletException
   */
  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException {

    // セッションからログインフォームを取得
    HttpSession session = request.getSession(false);

    // 空なら問答無用でログイン画面にリダイレクト
    if (ExObjectUtils.isBlank(session)) {
      response.sendRedirect(request.getContextPath() + LOGIN_PAGE_PATH);
      return;
    }
    // ログインIDとメッセージ設定用のForm生成
    LoginForm loginForm = new LoginForm();

    // 独自例外の場合、エラーに応じたメッセージを設定
    if (exception instanceof AdminAuthException) {
      // エラーメッセージKeyを設定
      loginForm.setErrorMessageKey(
          ((AdminAuthException) exception).getAdminAuthError().getMessage());
    } else {
      // 違う場合は、その他のエラーメッセージKeyを設定
      loginForm.setErrorMessageKey("message.error.internal.server.error");
    }
    // ログインフォームをセッションに追加して、エラー画面にリダイレクト
    session.setAttribute(LOGIN_FORM, loginForm);
    response.sendRedirect(request.getContextPath() + LOGIN_PAGE_PATH + "error");
  }
}
