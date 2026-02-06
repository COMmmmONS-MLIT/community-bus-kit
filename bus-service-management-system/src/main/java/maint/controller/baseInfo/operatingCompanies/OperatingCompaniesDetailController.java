package maint.controller.baseInfo.operatingCompanies;

import maint.controller.BaseController;
import maint.enums.DispMode;
import maint.enums.Menu;
import maint.enums.UsageDivision;
import maint.form.operatingCompanies.OperatingCompaniesDetailForm;
import maint.message.CommonMessage;
import maint.service.baseInfo.OperatingCompaniesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

/**
 * 運行会社詳細CONTROLLER
 *
 * @author jeon
 */
@Controller
@RequestMapping("/baseInfo/operatingCompanies/detail")
@SessionAttributes("operatingCompaniesDetailForm")
public class OperatingCompaniesDetailController extends BaseController {

  /** 運行会社詳細画面パス */
  private static final String DETAIL_PAGE = "baseInfo/operatingCompanies/detail";
  /** 運行会社確認画面パス */
  private static final String CONFIRM_PAGE = "baseInfo/operatingCompanies/confirm";
  /** 運行会社完了画面パス */
  private static final String COMPLETE_PAGE = "baseInfo/operatingCompanies/complete";
  /** 運行会社完了画面URL */
  private static final String COMPLETE_URL = "baseInfo/operatingCompanies/detail/complete/";
  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.OP_COMPANY;

  /** 運行会社SERVICE */
  @Autowired
  OperatingCompaniesService operatingCompaniesService;

  /**
   * 詳細FORM設定
   *
   * @return 運行会社詳細FORM
   */
  @ModelAttribute("operatingCompaniesDetailForm")
  public OperatingCompaniesDetailForm setOperatingCompaniesDetailForm() {
    return new OperatingCompaniesDetailForm();
  }

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    execCommonProc(model,NOW_MENU);
  }

  /**
   * 共通初期化処理
   *
   * @param model Model
   * @param form  運行会社詳細FORM
   */
  private void commonInitProcess(Model model, OperatingCompaniesDetailForm form) {
    model.addAttribute("operatingCompaniesDetailForm", form);
  }
  /**
   * 運行会社詳細(新規登録)
   *
   * @param model Model
   * @param form  運行会社詳細FORM
   * @return 詳細画面
   */
  @GetMapping("/")
  public String newUser(Model model,
      @ModelAttribute("displayForm") OperatingCompaniesDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    form.setDispMode(DispMode.INSERT_MODE);
    // 利用区分フラグ
    form.setAvailableFlag(UsageDivision.ACTIVE.getValue());
    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());
    // セッションから事業者名を設定
    form.setBusinessCorpName(getUserSession().getBusinessCorpName());
    // 運行会社情報設定
    model.addAttribute("operatingCompaniesDetailForm", form);

    return DETAIL_PAGE;
  }

  /**
   * 運行会社詳細(変更)
   *
   * @param model Model
   * @param form  運行会社詳細FORM
   * @return 詳細画面
   */
  @PostMapping("/")
  public String loadUser(Model model,
      @ModelAttribute("displayForm") OperatingCompaniesDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    form.setInfo(operatingCompaniesService.getOperatingCompaniesByPk(form.getOperatingCompanyId()),
        form.getPage(), DispMode.UPDATE_MODE);
    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());
    // 運行会社情報設定
    model.addAttribute("operatingCompaniesDetailForm", form);

    return DETAIL_PAGE;
  }



  /**
   * 運行会社詳細画面（戻る）
   *
   * @param model Model
   * @param form  運行会社詳細FORM
   * @return 詳細画面
   */
  @PostMapping("/back/")
  public String back(Model model,
      @ModelAttribute("operatingCompaniesDetailForm") OperatingCompaniesDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // セッション情報の整合性チェック
    form.isInvalidSession();
    model.addAttribute("displayForm", form);

    return DETAIL_PAGE;
  }

  /**
   * 運行会社確認画面(参照権限)
   *
   * @param model Model
   * @param form  運行会社詳細FORM
   * @return 確認画面
   */
  @PostMapping("/content/")
  public String content(Model model, @ModelAttribute("displayForm") OperatingCompaniesDetailForm form) {

    hasAccessPermission(NOW_MENU, false, false);

    form.setInfo(operatingCompaniesService.getOperatingCompaniesByPk(form.getOperatingCompanyId()),
        form.getPage(), DispMode.CONTENT_MODE);

    commonInitProcess(model, form);
    return CONFIRM_PAGE;
  }
  /**
   * 運行会社確認画面
   *
   * @param model         Model
   * @param form          運行会社詳細FORM
   * @param bindingResult BindingResult
   * @return 確認画面
   */
  @PostMapping("/confirm/")
  public String confirm(Model model,
      @Validated @ModelAttribute("displayForm") OperatingCompaniesDetailForm form,
      BindingResult bindingResult) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // 既存データ設定
    form.setOriginFormData(
        (OperatingCompaniesDetailForm) model.asMap().get("operatingCompaniesDetailForm"));
    // セッション情報の整合性チェック
    form.isInvalidSession();
    // 共通Validationチェック
    commonValidationProcess(form, bindingResult);

    // Validationチェックでエラーがあった場合、詳細画面へ遷移
    if (!this.errorMsgList.isEmpty()) {
      return DETAIL_PAGE;
    }
    model.addAttribute("operatingCompaniesDetailForm", form);
    return CONFIRM_PAGE;
  }

  /**
   * 運行会社登録、変更
   *
   * @param model              Model
   * @param form               運行会社詳細FORM
   * @param bindingResult      BindingResult
   * @param redirectAttributes RedirectAttributes
   * @param builder            UriComponentsBuilder
   * @return 完了画面URL Redirect
   */
  @PostMapping("/register/")
  public String register(Model model,
      @Validated @ModelAttribute("operatingCompaniesDetailForm") OperatingCompaniesDetailForm form,
      BindingResult bindingResult, RedirectAttributes redirectAttributes,
      UriComponentsBuilder builder) {
    String attributeName = "displayForm";

    hasAccessPermission(NOW_MENU, true, false);
    // セッション情報の整合性チェック
    form.isInvalidSession();

    commonValidationProcess(form, bindingResult);

    if (!this.errorMsgList.isEmpty()) {
      model.addAttribute(attributeName, form);
      // エラー内容をModelに設定
      String className = bindingResult.getClass().getCanonicalName().replace("BeanProperty", "");
      model.addAttribute(className + "." + attributeName, bindingResult);
      return DETAIL_PAGE;
    }

    if (form.getDispMode().equals(DispMode.INSERT_MODE)) {
      try {
        operatingCompaniesService.insert(form);
        setSuccessMsg(CommonMessage.SUCCESS_REGISTER);
      } catch (Exception e) {
        LOGGER.error("【運行会社管理】登録失敗", e);
        commonInitProcess(model, form);
        setErrorMsg(CommonMessage.ERROR_REGISTER);
        return CONFIRM_PAGE;
      }
    } else if (form.getDispMode().equals(DispMode.UPDATE_MODE)) {
      try {
        operatingCompaniesService.update(form);
        setSuccessMsg(CommonMessage.SUCCESS_UPDATE);
      } catch (Exception e) {
        LOGGER.error("【運行会社管理】更新失敗", e);
        commonInitProcess(model, form);
        setErrorMsg(CommonMessage.ERROR_UPDATE);
        return CONFIRM_PAGE;
      }
    }

    model.addAttribute("operatingCompaniesDetailForm", form);

    // 完了画面にリダイレクト
    return "redirect:" + builder.path(COMPLETE_URL).build().toUri();
  }

  /**
   * 運行会社完了画面
   *
   * @param model Model
   * @param form  運行会社詳細FORM
   * @return 運行会社完了画面
   */
  @GetMapping("/complete/")
  public String complete(Model model,
      @ModelAttribute("operatingCompaniesDetailForm") OperatingCompaniesDetailForm form) {
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
   * @param form          運行会社詳細FORM
   * @param bindingResult BindingResult
   */
  private void commonValidationProcess(OperatingCompaniesDetailForm form,
      BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      this.setErrorMsg(CommonMessage.ERROR_INPUT);
    }
  }

}
