package maint.controller.system.users;

import java.util.UUID;

import maint.enums.*;
import maint.service.baseInfo.OperatingCompaniesService;
import maint.service.baseInfo.AgenciesService;
import maint.session.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import maint.controller.BaseController;
import maint.form.users.UsersDetailForm;
import maint.message.CommonMessage;
import maint.message.UsersMessage;
import maint.service.CommonService;
import maint.service.system.UsersService;
import maint.utils.ExObjectUtils;
import maint.utils.PasswordUtils;

/**
 * ユーザ詳細CONTROLLER
 *
 * @author ryuichi.kashiwai
 */
@Controller
@RequestMapping("/system/users/detail")
@SessionAttributes("usersDetailForm")
public class UsersDetailController extends BaseController {

  /** ユーザ詳細画面パス */
  private static final String DETAIL_PAGE = "system/users/detail";
  /** ユーザ確認画面パス */
  private static final String CONFIRM_PAGE = "system/users/confirm";
  /** ユーザ完了画面パス */
  private static final String COMPLETE_PAGE = "system/users/complete";
  /** ユーザ完了画面URL */
  private static final String COMPLETE_URL = "system/users/detail/complete/";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.ACCOUNT;

  /** 共通SERVICE */
  @Autowired
  CommonService commonService;

  /** ユーザ一SERVICE */
  @Autowired
  UsersService usersService;

  /** 事業者SERVICE */
  @Autowired
  AgenciesService agenciesService;

  /** 運行会社SERVICE */
  @Autowired
  OperatingCompaniesService operatingCompanyService;

  /**
   * 詳細FORM設定
   *
   * @return 詳細FORM
   */
  @ModelAttribute("usersDetailForm")
  public UsersDetailForm setUsersDetailForm() {
    return new UsersDetailForm();
  }

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    model.addAttribute("permRoles", commonService.getPermissionRolesList());
    execCommonProc(model, NOW_MENU);
  }

  /**
   * ユーザ詳細(新規登録)
   *
   * @param model Model
   * @param form  検索FORM
   * @return 詳細画面
   */
  @GetMapping("/")
  public String newUser(Model model, @ModelAttribute("usersDetailForm") UsersDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);
    UserSession session = getUserSession();
    // 新規登録フォーム初期化
    form = new UsersDetailForm(form.getPage(), session);
    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());
    // 共通処理
    commonProcess(model, form);

    return DETAIL_PAGE;
  }

  /**
   * ユーザ詳細(変更)
   *
   * @param model Model
   * @param form  検索FORM
   * @return 詳細画面
   */
  @PostMapping("/")
  public String loadUser(Model model, @ModelAttribute("usersDetailForm") UsersDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // ユーザー情報取得
    form.setInfo(usersService.getUserByPk(form.getUserId()), form.getPage(), DispMode.UPDATE_MODE);

    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());
    // 共通処理
    commonProcess(model, form);

    return DETAIL_PAGE;
  }

  /**
   * ユーザ詳細画面（戻る）
   *
   * @param model Model
   * @param form  詳細FORM
   * @return 詳細画面
   */
  @PostMapping("/back/")
  public String back(Model model, @ModelAttribute("usersDetailForm") UsersDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // セッション情報の整合性チェック
    form.isInvalidSession();

    // 共通処理
    commonProcess(model, form);

    return DETAIL_PAGE;
  }

  /**
   * ユーザ確認画面(参照権限)
   *
   * @param model Model
   * @param form  詳細FORM
   * @return 確認画面
   */
  @RequestMapping("/content/")
  public String content(Model model, @ModelAttribute("usersDetailForm") UsersDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());

    form.setInfo(usersService.getUserByPk(form.getUserId()), form.getPage(), DispMode.CONTENT_MODE);

    // 共通処理
    commonProcess(model, form);

    return CONFIRM_PAGE;
  }

  /**
   * ユーザ確認画面
   *
   * @param model         Model
   * @param form          詳細FORM
   * @param bindingResult BindingResult
   * @return 確認画面
   */
  @PostMapping("/confirm/")
  public String confirm(Model model, @Validated @ModelAttribute("usersDetailForm") UsersDetailForm form,
      BindingResult bindingResult) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // 既存データ設定
    form.setOriginFormData((UsersDetailForm) model.asMap().get("usersDetailForm"));

    // セッション情報の整合性チェック
    form.isInvalidSession();
    // 共通Validationチェック
    commonValidationProcess(form, bindingResult);

    // 共通処理
    commonProcess(model, form);

    // Validationチェックでエラーがあった場合、詳細画面へ遷移
    if (!this.errorMsgList.isEmpty()) {
      return DETAIL_PAGE;
    }

    return CONFIRM_PAGE;
  }

  /**
   * ユーザ登録、変更
   *
   * @param model              Model
   * @param form               詳細FORM
   * @param bindingResult      BindingResult
   * @param redirectAttributes RedirectAttributes
   * @param builder            UriComponentsBuilder
   * @return 完了画面URL Redirect
   */
  @PostMapping("/register/")
  public String register(Model model,
      @Validated @ModelAttribute("usersDetailForm") UsersDetailForm form,
      BindingResult bindingResult, RedirectAttributes redirectAttributes,
      UriComponentsBuilder builder) {
    String attributeName = "usersDetailForm";
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // セッション情報の整合性チェック
    form.isInvalidSession();

    // 共通Validationチェック
    commonValidationProcess(form, bindingResult);

    // Validationチェックでエラーがあった場合、詳細画面へ遷移
    if (!this.errorMsgList.isEmpty()) {
      commonProcess(model, form);
      // エラー内容をModelに設定
      String className = bindingResult.getClass().getCanonicalName().replace("BeanProperty", "");
      model.addAttribute(className + "." + attributeName, bindingResult);
      return DETAIL_PAGE;
    }

    // 登録、変更処理
    if (form.getDispMode().equals(DispMode.INSERT_MODE)) {
      try {
        // 登録処理
        usersService.insert(form);
      } catch (Exception e) {
        LOGGER.error("【アカウント管理】登録失敗", e);
        this.setErrorMsg(CommonMessage.ERROR_REGISTER);
        // 共通処理
        commonProcess(model, form);
        return DETAIL_PAGE;
      }
    } else if (form.getDispMode().equals(DispMode.UPDATE_MODE)) {
      try {
        // 更新処理
        usersService.update(form);
      } catch (Exception e) {
        LOGGER.error("【アカウント管理】更新失敗", e);
        this.setErrorMsg(CommonMessage.ERROR_UPDATE);
        // 共通処理
        commonProcess(model, form);
        return DETAIL_PAGE;
      }
    }

    // 完了画面にリダイレクト
    return "redirect:" + builder.path(COMPLETE_URL).build().toUri();
  }

  /**
   * ユーザ完了画面
   *
   * @param model Model
   * @param form  詳細FORM
   * @return ユーザ完了画面
   */
  @GetMapping("/complete/")
  public String complete(Model model, @ModelAttribute("usersDetailForm") UsersDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // セッション情報の整合性チェック
    form.isInvalidSession();

    this.setSuccessMsg(form.getDispMode().equals(DispMode.INSERT_MODE) ?
        CommonMessage.SUCCESS_REGISTER :
        CommonMessage.SUCCESS_UPDATE);

    // セッションキーを破棄する
    form.setSessionKey("");

    return COMPLETE_PAGE;
  }

  /**
   * 共通Validation処理
   *
   * @param form          詳細FORM
   * @param bindingResult BindingResult
   */
  private void commonValidationProcess(UsersDetailForm form, BindingResult bindingResult) {

    // ログインID重複チェック
    if (RetirementFlag.EMPLOYED.getValue().equals(form.getUsageDivision())) {
      if (usersService.countUsers(form.getUserId(), form.getEmail())) {
        bindingResult.addError(new FieldError("email", "email",
            getMessage(UsersMessage.ERROR_USERS_EMAIL_DUPLE.getMessage())));
      }
    }

    // パスワードとログインID同一禁止
    if (ExObjectUtils.isNotBlank(form.getPassword()) && PasswordUtils.equalsToEmail(
        form.getPassword(), form.getEmail())) {
      bindingResult.addError(
          new FieldError("password", "password", form.getPassword(), false, null, null,
              getMessage(UsersMessage.ERROR_USERS_PASS_EQUAL_ID.getMessage())));
    }

    if (bindingResult.hasErrors()) {
      this.setErrorMsg(CommonMessage.ERROR_INPUT);
    }
  }

  /**
   * 共通処理
   *
   * @param model Model
   * @param form ユーザー詳細FORM
   */
  private void commonProcess(Model model, UsersDetailForm form) {
    // ユーザ情報設定
    model.addAttribute("usersDetailForm", form);
    // 事業者取得（SELECT用）
    model.addAttribute("agencyList", agenciesService.getAllAgenciesForSelectOption());
    // 運行会社取得（SELECT用）
    model.addAttribute("operatingCompanyList",
        operatingCompanyService.getAllOperatingCompaniesForSelectOption());
    // セッション情報（アカウント区分）設定
    model.addAttribute("accountDivision", getUserSession().getAccountDivision());
    // セッション情報（事業者ID）設定
    model.addAttribute("businessCorpId", getUserSession().getBusinessCorpId());
    model.addAttribute("isAgencyWithOperatingCompany",
        getUserSession().getAccountDivision().equals(AccountDivision.AGENCY.getValue())
            && ExObjectUtils.isNotBlank(getUserSession().getOperatingCompanyId()));
  }
}
