package maint.config;

import maint.controller.TwoFactorAuthenticationFilter;
import maint.handler.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import jakarta.servlet.DispatcherType;
import maint.handler.CustomAuthenticationFailureHandler;
import maint.provider.CustomAuthenticationProvider;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher.withDefaults;

/**
 * WebSecurityMultiConfig
 *
 * @author administrator
 */
@Configuration
@EnableWebSecurity
public class WebSecurityMultiConfig {

  /** 独自認証 */
  @Autowired
  CustomAuthenticationProvider authenticationProvider;

  /** Spring Securityの認証成功時に呼ばれるハンドラクラス */
  @Autowired
  private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
  /**
   * security設定
   *
   * @param http HttpSecurity
   * @return SecurityFilterChain
   * @throws Exception
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    // コントローラで画面ファイル名をリターンし、ViewResolverが動作してページを遷移する場合、以下の設定必要
    http.authorizeHttpRequests(
        (authorizeHttpRequests) -> authorizeHttpRequests.dispatcherTypeMatchers(
            DispatcherType.FORWARD).permitAll());

    // 認証なしでのアクセス（CSRF有効)
    http.authorizeHttpRequests(
        (authorizeHttpRequests) -> authorizeHttpRequests.requestMatchers(
            "/css/**", "/fonts/**", "/img/**", "/js/**", "/page/**",
            "/auth/login", "/auth/login/error", "/auth/login/two-factor",
            "/auth/login/check-auth-code", "/auth/login/resend-auth-code",
            "/auth/session-timeout", "/error/*").permitAll());


    // 認証なしでのアクセス以外は、認証後でないとアクセス出来ない設定
    http.authorizeHttpRequests(
        (authorizeHttpRequests) -> authorizeHttpRequests.anyRequest().authenticated());

    // ログイン処理
    http.formLogin((formLogin) -> formLogin.loginPage("/auth/login")
        .defaultSuccessUrl("/auth/login/success", true)// ログイン成功
        .successHandler(customAuthenticationSuccessHandler)// ログイン成功
        .failureHandler(new CustomAuthenticationFailureHandler()).permitAll() // ログイン失敗
        .usernameParameter("email").passwordParameter("loginPass"));

    // ログアウト処理
    http.logout(logout -> logout
        .logoutRequestMatcher(withDefaults().matcher(HttpMethod.GET, "/auth/logout"))
        .logoutSuccessUrl("/auth/login") // ログアウト成功
        .invalidateHttpSession(true) // セッションを無効にする
        .permitAll()
    );

    // 二要素認証前は認証を確定しない
    http.addFilterBefore(new TwoFactorAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    // セッション切れの遷移先設定
    http.sessionManagement(
        (sessionManagement) -> sessionManagement.invalidSessionUrl("/auth/session-timeout"));

    return http.build();
  }

  /**
   * authenticationManager生成<br> Springの内部動作により、上記で作成したCustomAuthenticationProviderが自動的に設定される
   *
   * @param authenticationConfiguration AuthenticationConfiguration
   * @return AuthenticationManager
   * @throws Exception
   */
  @Bean
  AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

}
