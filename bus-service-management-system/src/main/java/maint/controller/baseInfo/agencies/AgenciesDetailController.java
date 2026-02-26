package maint.controller.baseInfo.agencies;

import maint.controller.BaseController;
import maint.enums.AccountDivision;
import maint.enums.DispMode;
import maint.enums.Menu;
import maint.enums.UsageDivision;
import maint.exception.PermissionException;
import maint.form.agencies.AgenciesDetailForm;
import maint.message.CommonMessage;
import maint.service.baseInfo.AgenciesService;
import maint.session.UserSession;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.validation.FieldError;

import java.util.UUID;

/**
 * 事業者管理詳細 Controller
 *
 * @author y.kim
 */

@Controller
@RequestMapping(value = "/baseInfo/agencies/detail")
@SessionAttributes("agenciesDetailForm")
public class AgenciesDetailController extends BaseController {

  /** 事業者詳細画面PATH */
  private static final String DETAIL_PAGE = "baseInfo/agencies/detail";
  /** 事業者確認画面PATH */
  private static final String CONFIRM_PAGE = "baseInfo/agencies/confirm";
  /** 事業者完了画面PATH */
  private static final String COMPLETE_PAGE = "baseInfo/agencies/complete";
  /** 事業者完了URL */
  private static final String COMPLETE_URL = "baseInfo/agencies/detail/complete/";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.AGENCY;

  /** 事業者SERVICE */
  @Autowired
  AgenciesService agenciesService;

  /**
   * 事業者詳細FORM設定
   *
   * @return 事業者詳細FORM
   */
  @ModelAttribute("agenciesDetailForm")
  public AgenciesDetailForm setAgenciesDetailForm() {
    return new AgenciesDetailForm();
  }

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    execCommonProc(model, NOW_MENU);
  }

  /**
   * 事業者詳細画面(新規登録)
   *
   * @param model Model
   * @param form  事業者詳細FORM
   * @return 事業者詳細画面
   */
  @GetMapping("/")
  public String newAgency(Model model, @ModelAttribute("displayForm") AgenciesDetailForm form) {
    UserSession userSession = getUserSession();
    String accountDivision = userSession.getAccountDivision();
    // システム管理者ではないアカウントからのアクセスの場合、権限エラーの例外をスローする(URL直打ち対策)
    if (!AccountDivision.SYSTEM_ADMIN.getValue().equals(accountDivision)) {
      throw new PermissionException(NOW_MENU.getName() + "への権限がありません。");
    }

    hasAccessPermission(NOW_MENU, true, false);

    form.setDispMode(DispMode.INSERT_MODE);
    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());

    commonInitProcess(model, form);

    return DETAIL_PAGE;
  }

  /**
   * 事業者詳細画面（変更）
   *
   * @param model Model
   * @param form  事業者詳細FORM
   * @return 事業者詳細画面
   */
  @PostMapping("/")
  public String loadAgency(Model model,
      @ModelAttribute("displayForm") AgenciesDetailForm form) {

    hasAccessPermission(NOW_MENU, true, false);

    form.setDispMode(DispMode.UPDATE_MODE);
    form.setAgenciesEntity(agenciesService.getAgencyByPkId(form.getBusinessCorpId()));

    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());

    commonInitProcess(model, form);

    return DETAIL_PAGE;
  }

  /**
   * 事業者詳細画面(戻る)
   *
   * @param model Model
   * @param form  事業者詳細FORM
   * @return 事業者詳細画面
   */
  @PostMapping("/back/")
  public String back(Model model, @ModelAttribute("agenciesDetailForm") AgenciesDetailForm form) {

    hasAccessPermission(NOW_MENU, true, false);

    form.isInvalidSession();

    model.addAttribute("displayForm", form);

    return DETAIL_PAGE;
  }

  /**
   * 事業者確認画面(参照事業者)
   *
   * @param model Model
   * @param form  事業者詳細FORM
   * @return 事業者確認画面
   */
  @PostMapping("/content/")
  public String content(Model model, @ModelAttribute("displayForm") AgenciesDetailForm form) {

    hasAccessPermission(NOW_MENU, false, false);

    form.setDispMode(DispMode.CONTENT_MODE);
    form.setAgenciesEntity(agenciesService.getAgencyByPkId(form.getBusinessCorpId()));

    commonInitProcess(model, form);

    return CONFIRM_PAGE;
  }

  /**
   * 事業者確認画面
   *
   * @param model         Model
   * @param form          事業者詳細FORM
   * @param bindingResult BindingResult
   * @return 事業者確認画面
   */
  @PostMapping("/confirm/")
  public String confirm(Model model,
      @Validated @ModelAttribute("displayForm") AgenciesDetailForm form,
      BindingResult bindingResult) {

    hasAccessPermission(NOW_MENU, true, false);

    AgenciesDetailForm orgForm = (AgenciesDetailForm) model.asMap().get("agenciesDetailForm");

    form.setOriginFormData(orgForm);

    form.isInvalidSession();

    commonValidationProcess(form, bindingResult);

    if (!this.errorMsgList.isEmpty()) {
      return DETAIL_PAGE;
    }

    model.addAttribute("agenciesDetailForm", form);

    return CONFIRM_PAGE;
  }

  /**
   * 事業者登録、変更
   *
   * @param model              Model
   * @param form               事業者詳細FORM
   * @param bindingResult      BindingResult
   * @param redirectAttributes RedirectAttributes
   * @param builder            UriComponentsBuilder
   * @return 事業者完了URL Redirect
   */
  @PostMapping("/register/")
  public String register(Model model,
      @Validated @ModelAttribute("agenciesDetailForm") AgenciesDetailForm form,
      BindingResult bindingResult, RedirectAttributes redirectAttributes,
      UriComponentsBuilder builder) {
    String attributeName = "displayForm";

    hasAccessPermission(NOW_MENU, true, false);

    AgenciesDetailForm orgForm = (AgenciesDetailForm) model.asMap().get("agenciesDetailForm");

    form.setOriginFormData(orgForm);

    form.isInvalidSession();

    commonValidationProcess(form, bindingResult);

    if (!this.errorMsgList.isEmpty()) {
      model.addAttribute(attributeName, form);
      // エラー内容をModelに設定
      String className = bindingResult.getClass().getCanonicalName().replace("BeanProperty", "");
      model.addAttribute(className + "." + attributeName, bindingResult);
      return DETAIL_PAGE;
    }

    try {
      if (form.getDispMode().equals(DispMode.INSERT_MODE)) {
        agenciesService.insert(form);
      } else if (form.getDispMode().equals(DispMode.UPDATE_MODE)) {
        agenciesService.update(form);
      }
    } catch (Exception e) {
      model.addAttribute("errorMessage", form.getDispMode().equals(DispMode.INSERT_MODE) ?
          getMessage(CommonMessage.ERROR_REGISTER.getMessage()) :
              getMessage(CommonMessage.ERROR_UPDATE.getMessage()));
      form.setSessionKey("");
      return COMPLETE_PAGE;
    }

    model.addAttribute("agenciesDetailForm", form);

    // 完了画面にリダイレクト
    return "redirect:" + builder.path(COMPLETE_URL).build().toUri();
  }

  /**
   * 事業者完了画面
   *
   * @param model Model
   * @param form  事業者詳細FORM
   * @return 事業者完了画面
   */
  @GetMapping("/complete/")
  public String complete(Model model,
      @ModelAttribute("agenciesDetailForm") AgenciesDetailForm form) {

    hasAccessPermission(NOW_MENU, true, false);

    form.isInvalidSession();

    this.setSuccessMsg(form.getDispMode().equals(DispMode.INSERT_MODE) ?
        CommonMessage.SUCCESS_REGISTER :
        CommonMessage.SUCCESS_UPDATE);

    form.setSessionKey("");

    return COMPLETE_PAGE;
  }

  /**
   * 共通初期化処理
   *
   * @param model Model
   * @param form  事業者詳細FORM
   */
  private void commonInitProcess(Model model, AgenciesDetailForm form) {
    model.addAttribute("agenciesDetailForm", form);
  }

  /**
   * 共通Validation処理
   *
   * @param form          事業者詳細FORM
   * @param bindingResult BindingResult
   */
  private void commonValidationProcess(AgenciesDetailForm form, BindingResult bindingResult) {

    // 法人番号重複チェック
    if (agenciesService.isDupleOperationCompanyNo(form)) {
      bindingResult.addError(new FieldError(bindingResult.getObjectName(), "operationCompanyNo",
          getMessage("validation.agency.duplicateOperationCompanyNo")));
    }
    // 提供組織連絡先チェック
    if (ExObjectUtils.isBlank(form.getProviderOrganizationContactEmail()) && ExObjectUtils.isBlank(form.getProviderOrganizationContactUrl())) {
      bindingResult.addError(new FieldError(bindingResult.getObjectName(), "providerOrganizationContactEmail",
          getMessage("validation.agency.error.providerOrganizationContact")));
      bindingResult.addError(new FieldError(bindingResult.getObjectName(), "providerOrganizationContactUrl",
          getMessage("validation.agency.error.providerOrganizationContact")));
    }

    // 事業者利用不可変更チェック
    if (DispMode.UPDATE_MODE.equals(form.getDispMode()) && UsageDivision.DISABLED.getValue()
        .equals(form.getAvailableFlag()) && agenciesService.isUsedAgency(
        form.getBusinessCorpId())) {
      bindingResult.addError(new FieldError(bindingResult.getObjectName(), "availableFlag",
          getMessage("validation.agency.disableNotAllowedForUserAffiliation")));
    }

    if (bindingResult.hasErrors()) {
      this.setErrorMsg(CommonMessage.ERROR_INPUT);
    }

  }
}
