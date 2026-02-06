package maint.controller.system.permissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import maint.controller.BaseController;
import maint.enums.DispMode;
import maint.enums.Menu;
import maint.form.permissions.PermissionsDetailForm;
import maint.message.CommonMessage;
import maint.message.PermissionsMessage;
import maint.service.system.PermissionService;

import java.util.UUID;

/**
 * 権限管理詳細 Controller
 *
 * @author y.kim
 */

@Controller
@RequestMapping(value = "/system/permissions/detail")
@SessionAttributes("permissionsDetailForm")
public class PermissionsDetailController extends BaseController {

  /** 権限詳細画面PATH */
  private static final String DETAIL_PAGE = "system/permissions/detail";
  /** 権限確認画面PATH */
  private static final String CONFIRM_PAGE = "system/permissions/confirm";
  /** 権限完了画面PATH */
  private static final String COMPLETE_PAGE = "system/permissions/complete";
  /** 権限完了URL */
  private static final String COMPLETE_URL = "system/permissions/detail/complete/";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.PERMISSION;

  /** 権限SERVICE */
  @Autowired
  PermissionService permissionsService;

  /**
   * 権限詳細FORM設定
   *
   * @return 権限詳細FORM
   */
  @ModelAttribute("permissionsDetailForm")
  public PermissionsDetailForm setPermissionDetailForm() {
    return new PermissionsDetailForm();
  }

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    execCommonProc(model ,NOW_MENU);
  }

  /**
   * 権限詳細画面(新規登録)
   *
   * @param model Model
   * @param form  権限詳細FORM
   * @return 権限詳細画面
   */
  @GetMapping("/")
  public String newPermission(Model model,
      @ModelAttribute("displayForm") PermissionsDetailForm form) {

    hasAccessPermission(NOW_MENU, true, false);

    form.setDispMode(DispMode.INSERT_MODE);
    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());

    commonInitProcess(model, form);

    return DETAIL_PAGE;
  }

  /**
   * 権限詳細画面（変更）
   *
   * @param model Model
   * @param form  権限詳細FORM
   * @return 権限詳細画面
   */
  @PostMapping("/")
  public String loadPermission(Model model,
      @ModelAttribute("displayForm") PermissionsDetailForm form) {

    hasAccessPermission(NOW_MENU, true, false);

    form.setDispMode(DispMode.UPDATE_MODE);
    permissionsService.setPermissionName(form);

    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());

    commonInitProcess(model, form);

    return DETAIL_PAGE;
  }

  /**
   * 権限詳細画面(戻る)
   *
   * @param model Model
   * @param form  権限詳細FORM
   * @return 権限詳細画面
   */
  @PostMapping("/back/")
  public String back(Model model,
      @ModelAttribute("permissionsDetailForm") PermissionsDetailForm form) {

    hasAccessPermission(NOW_MENU, true, false);

    form.isInvalidSession();

    model.addAttribute("permissionsList", form.getTopMenuList());
    model.addAttribute("displayForm", form);

    return DETAIL_PAGE;
  }

  /**
   * 権限確認画面(参照権限)
   *
   * @param model Model
   * @param form  権限詳細FORM
   * @return 権限確認画面
   */
  @PostMapping("/content/")
  public String content(Model model, @ModelAttribute("displayForm") PermissionsDetailForm form) {

    hasAccessPermission(NOW_MENU, false, false);

    form.setDispMode(DispMode.CONTENT_MODE);
    permissionsService.setPermissionName(form);

    commonInitProcess(model, form);

    return CONFIRM_PAGE;
  }

  /**
   * 権限確認画面
   *
   * @param model         Model
   * @param form          権限詳細FORM
   * @param bindingResult BindingResult
   * @return 権限確認画面
   */
  @PostMapping("/confirm/")
  public String confirm(Model model,
      @Validated @ModelAttribute("displayForm") PermissionsDetailForm form,
      BindingResult bindingResult) {

    hasAccessPermission(NOW_MENU, true, false);

    PermissionsDetailForm orgForm =
        (PermissionsDetailForm) model.asMap().get("permissionsDetailForm");

    form.setOriginFormData(orgForm);
    form.isInvalidSession();

    commonValidationProcess(form, bindingResult);
    model.addAttribute("permissionsList", form.getTopMenuList());

    if (!this.errorMsgList.isEmpty()) {
      return DETAIL_PAGE;
    }

    model.addAttribute("permissionsDetailForm", form);

    return CONFIRM_PAGE;
  }

  /**
   * 権限登録、変更
   *
   * @param model              Model
   * @param form               権限詳細FORM
   * @param bindingResult      BindingResult
   * @param redirectAttributes RedirectAttributes
   * @param builder            UriComponentsBuilder
   * @return 権限完了URL Redirect
   */
  @PostMapping("/register/")
  public String register(Model model,
      @Validated @ModelAttribute("permissionsDetailForm") PermissionsDetailForm form,
      BindingResult bindingResult, RedirectAttributes redirectAttributes,
      UriComponentsBuilder builder) {
    String attributeName = "displayForm";

    hasAccessPermission(NOW_MENU, true, false);

    form.isInvalidSession();

    commonValidationProcess(form, bindingResult);

    if (!this.errorMsgList.isEmpty()) {
      model.addAttribute("permissionsList", form.getTopMenuList());
      model.addAttribute(attributeName, form);
      // エラー内容をModelに設定
      String className = bindingResult.getClass().getCanonicalName().replace("BeanProperty", "");
      model.addAttribute(className + "." + attributeName, bindingResult);
      return DETAIL_PAGE;
    }

    try {
      if (form.getDispMode().equals(DispMode.INSERT_MODE)) {
        permissionsService.insert(form);
      } else if (form.getDispMode().equals(DispMode.UPDATE_MODE)) {
        permissionsService.update(form);
      }
    } catch (Exception e) {
      model.addAttribute("errorMessage", form.getDispMode().equals(DispMode.INSERT_MODE) ?
          getMessage(CommonMessage.ERROR_REGISTER.getMessage()) :
          getMessage(CommonMessage.ERROR_UPDATE.getMessage()));
      form.setSessionKey("");
      return COMPLETE_PAGE;
    }

    model.addAttribute("permissionsDetailForm", form);

    // 完了画面にリダイレクト
    return "redirect:" + builder.path(COMPLETE_URL).build().toUri();
  }

  /**
   * 権限完了画面
   *
   * @param model Model
   * @param form  権限詳細FORM
   * @return 権限完了画面
   */
  @GetMapping("/complete/")
  public String complete(Model model,
      @ModelAttribute("permissionsDetailForm") PermissionsDetailForm form) {

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
   * @param form  権限詳細FORM
   */
  private void commonInitProcess(Model model, PermissionsDetailForm form) {
    model.addAttribute("permissionsDetailForm", form);
    model.addAttribute("permissionsList", permissionsService.getPermissionMenuList(form));
  }

  /**
   * 共通Validation処理
   *
   * @param form          権限詳細FORM
   * @param bindingResult BindingResult
   */
  private void commonValidationProcess(PermissionsDetailForm form, BindingResult bindingResult) {

    // 権限リスト選択有無チェック（最小1つ選択）
    if (permissionsService.isInvalidPermission(form)) {
      this.setErrorMsg(PermissionsMessage.ERROR_NOT_SELECTED_PERMISSIONS.getMessage());
    }

    // 権限名重複チェック
    if (permissionsService.isDuplePermRoleName(form)) {
      bindingResult.addError(new FieldError("permRoleName", "permRoleName",
          getMessage(PermissionsMessage.ERROR_PERMISSIONS_NAME_DUPLE.getMessage())));
    }

    if (bindingResult.hasErrors()) {
      this.setErrorMsg(CommonMessage.ERROR_INPUT);
    }

  }
}

