package maint.handler;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import maint.service.AuthService;
import maint.session.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Spring Securityの認証成功時に呼ばれるハンドラクラス
 *
 * @author administrator
 */
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  /** ロガー */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /** ログインSERVICE */
  @Autowired
  private AuthService authService;

  /** 2要素認証URL */
  private static final String TWO_FACTOR_URL = "/auth/login/two-factor";

  /** デフォルトURL */
  private static final String DEFAULT_URL = "/auth/login/success";

  /**
   * 認証成功時の処理
   *
   * @param request   HttpServletRequest
   * @param response  HttpServletResponse
   * @throws IOException, ServletException
   */
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    // セッション取得
    UserSession userSession = (UserSession) authentication.getPrincipal();

    // 2要素認証が必要であるかチェック
    if (authService.isNeedTwoFactorAuth(userSession)) {

      // 6桁英数字の認証コード生成
      String authCode = authService.createRandomAlphanumeric(6);

      try {
        authService.sendTwoFactorMail(userSession, authCode);
      } catch (Exception e) {
        // メッセージ送信失敗時にはシステム管理者にメール送信して正常ログインとして管理画面TOPに遷移
        LOGGER.error("【管理者認証】2要素認証メール送信失敗");
        userSession.setAuthenticatedFlag(true);
        authService.writeAutheticationUserSession(userSession, request, response);
        super.setDefaultTargetUrl(DEFAULT_URL);
        super.onAuthenticationSuccess(request, response, authentication);
        return;
      }

      // 既存履歴データ削除及び履歴登録
      authService.updateTwoFactorHistory(userSession, authCode);

      // 2要素認証画面に遷移
      response.sendRedirect(TWO_FACTOR_URL);
    } else {
      // 2要素認証済みの場合
      // セッションの2要素認証済みフラグをTRUEに上書きする
      userSession.setAuthenticatedFlag(true);
      authService.writeAutheticationUserSession(userSession, request, response);
      // HOME画面に遷移
      super.setDefaultTargetUrl(DEFAULT_URL);
      super.onAuthenticationSuccess(request, response, authentication);
    }
  }

}
