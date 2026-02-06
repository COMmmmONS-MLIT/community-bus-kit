package maint.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import maint.controller.ErrorController.StatusCode;
import maint.enums.message.MessageEnums;
import maint.form.LoginForm;
import maint.form.users.TwoFactorForm;
import maint.service.AuthService;
import maint.session.UserSession;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * ログイン画面コントローラ
 *
 * @author administrator
 */
@Controller
@RequestMapping(value = "/auth")
@SessionAttributes(names = "loginForm")
public class AuthController extends BaseController {

  /** ログイン画面PATH */
  private static final String LOGIN_PAGE_PATH = "auth/index";

  /** 2要素認証画面PATH */
  private static final String TWO_FACTOR_PAGE_PATH = "auth/two-factor-auth";

  /** ログインSERVICE */
  @Autowired
  private AuthService authService;

  /**
   * ログインフォーム作成
   *
   * @param loginForm ログインフォーム
   * @return ログインフォーム
   */
  @ModelAttribute("loginForm")
  public LoginForm setRequestForm(LoginForm loginForm) {
    return loginForm;
  }

  /**
   * ログイン画面初期表示
   *
   * @param model Model
   * @return ログイン画面
   */
  @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
  public String index(Model model) {
    model.addAttribute("loginForm", new LoginForm());
    // ログイン画面を表示する
    return LOGIN_PAGE_PATH;
  }

  /**
   * ログイン失敗
   *
   * @param model Model
   * @param form  ログインフォーム
   * @return ログイン画面
   */
  @RequestMapping(value = "/login/error", method = {RequestMethod.GET, RequestMethod.POST})
  public String loginError(Model model, @ModelAttribute("loginForm") LoginForm form) {
    execCommonProc(model, true);
    if (ExObjectUtils.isNotBlank(form.getErrorMessageKey())) {
      this.setErrorMsg(form.getErrorMessageKey());
      form.setErrorMessageKey(null);
    }
    model.addAttribute("loginForm", form);
    // ログイン画面を表示する
    return LOGIN_PAGE_PATH;
  }


  /**
   * 2要素認証画面
   *
   * @param model Model
   * @param form 2要素認証フォーム
   * @return 2要素認証画面
   */
  @GetMapping(value = "/login/two-factor")
  public String twoFactor(Model model, @ModelAttribute("twoFactorForm") TwoFactorForm form) {
    model.addAttribute("twoFactorForm", form);
    execCommonProc(model, true);
    return TWO_FACTOR_PAGE_PATH;
  }

  /**
   * 認証
   *
   * @param model Model
   * @param form 2要素認証フォーム
   * @param bindingResult BindingResult
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @param builder UriComponentsBuilder
   * @return 2要素認証画面
   */
  @PostMapping(value = "/login/check-auth-code")
  public String checkOtp(Model model,
      @Validated @ModelAttribute("twoFactorForm") TwoFactorForm form, BindingResult bindingResult,
      HttpServletRequest request, HttpServletResponse response, UriComponentsBuilder builder) {

    // 入力チェック
    if (bindingResult.hasErrors()) {
      setErrorMsg(MessageEnums.C00002);
      execCommonProc(model, true);
      return TWO_FACTOR_PAGE_PATH;
    }

    UserSession userSession = getUserSession();

    // 有効な認証コードであるか確認
    if (!authService.isValidAuthCode(userSession.getUserId(), form.getAuthCode())) {
      execCommonProc(model, true);
      setErrorMsg(MessageEnums.AM01003);
      return TWO_FACTOR_PAGE_PATH;
    }

    // 管理者認証情報更新
    try {
      authService.updateUserTwoFactorAuthInfo(userSession, request, response);
    } catch (Exception e) {
      LOGGER.error("【2要素認証】認証失敗", e);
      execCommonProc(model, true);
      setErrorMsg(MessageEnums.AM01003);
      return TWO_FACTOR_PAGE_PATH;
    }

    return "redirect:" + builder.path("/auth/login/success").build().toUri();
  }

  /**
   * 再送信
   *
   * @param model Model
   * @param form 2要素認証フォーム
   * @param request HttpServletRequest
   * @return 2要素認証画面
   */
  @PostMapping(value = "/login/resend-auth-code")
  public String resendOtp(Model model, @ModelAttribute("twoFactorForm") TwoFactorForm form,
      HttpServletRequest request) {

    UserSession userSession = getUserSession();
    try {
      authService.resendAuthCode(userSession);
    } catch (Exception e) {
      LOGGER.error("【2要素認証】コード再送信失敗", e);
      setErrorMsg(MessageEnums.C00014);
      return TWO_FACTOR_PAGE_PATH;
    }

    return TWO_FACTOR_PAGE_PATH;
  }


  /**
   * ログイン成功時の遷移
   *
   * @param model   Model
   * @param status  SessionStatus
   * @param builder UriComponentsBuilder
   * @return トップ画面
   */
  @RequestMapping(value = "/login/success", method = {RequestMethod.GET, RequestMethod.POST})
  public String login(Model model, SessionStatus status, UriComponentsBuilder builder) {
    // セッションのログインフォームを破棄
    status.setComplete();
    return "redirect:" + builder.path("/").build().toUri();
  }

  /**
   * セッション切れ
   *
   * @param model   Model
   * @param status  SessionStatus
   * @param builder UriComponentsBuilder
   * @return セッション切れ画面
   */
  @GetMapping("/session-timeout")
  public String sessionTimeout(Model model, SessionStatus status, UriComponentsBuilder builder) {
    return "redirect:" + UriComponentsBuilder.fromUriString(StatusCode.LOGIN_TIME_OUT.getPath())
        .build().toUri();
  }

}
