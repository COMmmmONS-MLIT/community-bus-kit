package maint.controller;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import maint.session.UserSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 二要素認証フィルター
 * 二要素認証が必要なユーザーが認証を完了していない場合、二要素認証画面にリダイレクトする
 */
public class TwoFactorAuthenticationFilter extends OncePerRequestFilter {

  /** ログイン画面の遷移先パス */
  private static final String LOGIN_URL = "/auth/login";

  /**
   * リクエストに対する二要素認証フィルタリング処理
   *
   * @param request 判定対象のリクエストURI
   * @param response HttpServletResponse
   * @param filterChain 次のフィルタまたは最終リソースを呼び出すためのフィルタチェーン
   * @throws ServletException サーブレット処理中に発生する一般的な例外
   * @throws IOException ファイル読み込み例外
   */
  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull FilterChain filterChain) throws ServletException, IOException {

    // ログインページや二要素認証関連のページ、パブリックリソースへのアクセスは常に許可
    if (request.getRequestURI().startsWith(LOGIN_URL) ||
        isPublicResource(request.getRequestURI())) {
      filterChain.doFilter(request, response);
      return;
    }

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    // 認証済みユーザーの場合
    if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserSession) {
      UserSession userSession = (UserSession) auth.getPrincipal();

      // 二要素認証が必要かつ未認証の場合
      if (!userSession.isAuthenticatedFlag()) {
        // ログイン画面へ
        response.sendRedirect(LOGIN_URL);
        return;
      }

      // 認証完了しているので続行
      filterChain.doFilter(request, response);
    } else {
      // 認証情報がない場合はログイン画面へ
      response.sendRedirect(LOGIN_URL);
    }
  }

  /**
   * パスが公開リソースであるかどうかのチェック
   *
   * @param uri 判定対象のリクエストURI
   * @return 公開資源である場合：true、公開リソースでない場合；false
   */
  private boolean isPublicResource(String uri) {
    return uri.startsWith("/css/") ||
        uri.startsWith("/fonts/") ||
        uri.startsWith("/img/") ||
        uri.startsWith("/js/") ||
        uri.startsWith("/page/") ||
        uri.startsWith("/error/");
  }
}
