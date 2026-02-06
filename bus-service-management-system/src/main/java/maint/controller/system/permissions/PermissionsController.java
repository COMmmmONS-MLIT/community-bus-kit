package maint.controller.system.permissions;


import jakarta.validation.constraints.DecimalMin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import maint.controller.BaseController;
import maint.dto.ListPager;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.form.permissions.PermissionsDetailForm;
import maint.form.permissions.PermissionsSearchForm;
import maint.message.CommonMessage;
import maint.message.PermissionsMessage;
import maint.service.system.PermissionService;
import maint.utils.ExObjectUtils;
import maint.form.ValidationGroups.Delete;

/**
 * 権限管理（一覧）Controller
 *
 * @author y.kim
 */

@Controller
@Validated
@RequestMapping(value = "/system/permissions")
@SessionAttributes("permissionsSearchForm")
public class PermissionsController extends BaseController {

  /** 一覧画面URL */
  private static final String INDEX_URL = "system/permissions/";
  /** 一覧画面PATH */
  private static final String INDEX_PAGE = "system/permissions/index";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.PERMISSION;

  /** 権限SERVICE */
  @Autowired
  PermissionService permissionService;

  /**
   * 権限検索FORM設定
   *
   * @return 権限検索FORM
   */
  @ModelAttribute("permissionsSearchForm")
  public PermissionsSearchForm setPermissionSearchForm() {
    return new PermissionsSearchForm();
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
   * 権限一覧画面（GET）
   *
   * @param model    Model
   * @param form     権限検索form
   * @param initFlag 検索フォーム初期化フラグ
   * @return 権限一覧画面
   */
  @GetMapping("/")
  public String index(Model model, PermissionsSearchForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {

    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new PermissionsSearchForm();
    }

    commonProcess(model, form, createListPager(model));

    // 完了メッセージ確認
    if (model.containsAttribute("deleteResultFlag")) {
      // 完了メッセージのセット
      setSuccessMsg(CommonMessage.SUCCESS_DELETE);
    }

    return INDEX_PAGE;
  }

  /**
   * 権限一覧画面（検索）
   *
   * @param model         Model
   * @param form          権限検索FORM
   * @param bindingResult BindingResult
   * @return 権限一覧画面
   */
  @PostMapping("/")
  public String index(Model model, @Validated PermissionsSearchForm form,
      BindingResult bindingResult) {

    hasAccessPermission(NOW_MENU, false, false);

    if (bindingResult.hasErrors()) {
      setErrorMsg(CommonMessage.ERROR_INPUT);
      return INDEX_PAGE;
    }
    form.setPage(1L);

    commonProcess(model, form, createListPager(model));

    return INDEX_PAGE;
  }

  /**
   * 削除処理
   *
   * @param model              Model
   * @param form               権限詳細FORM
   * @param searchForm         権限検索FORM
   * @param bindingResult      BindingResult
   * @param redirectAttributes RedirectAttributes
   * @param builder            UriComponentsBuilder
   * @return 権限一覧画面
   */
  @PostMapping("/delete/")
  public String delete(Model model, @Validated(Delete.class) PermissionsDetailForm form,
      PermissionsSearchForm searchForm, BindingResult bindingResult,
      RedirectAttributes redirectAttributes, UriComponentsBuilder builder) {

    hasAccessPermission(NOW_MENU, true, false);

    if (bindingResult.hasErrors()) {
      this.setErrorMsg(CommonMessage.ERROR_INPUT);

      commonProcess(model, searchForm, createListPager(model));

      return INDEX_PAGE;
    }

    // 該当権限を使用しているユーザが存在する場合、削除不可
    if (permissionService.isUsedPermissionRoles(form)) {
      this.setErrorMsg(PermissionsMessage.ERROR_USED_PERMISSIONS.getMessage());

      commonProcess(model, searchForm, createListPager(model));

      return INDEX_PAGE;
    }

    // 削除処理
    permissionService.delete(form);

    // 完了ページに成功メッセージを渡す
    redirectAttributes.addFlashAttribute("deleteResultFlag", true);

    // 完了画面にリダイレクト
    return "redirect:" + builder.path(INDEX_URL).build().toUri();
  }

  /**
   * ページング
   *
   * @param model Model
   * @param page  pageIndex
   * @param form  権限検索FORM
   * @return 権限一覧画面
   */
  @GetMapping("/{page}/")
  public String paging(Model model, @DecimalMin("1") @PathVariable("page") Long page,
      @ModelAttribute("permissionsSearchForm") PermissionsSearchForm form) {

    hasAccessPermission(NOW_MENU, false, false);

    commonProcess(model, form, createListPager(model, page));

    return INDEX_PAGE;
  }

  /**
   * 共通処理
   *
   * @param model Model
   * @param form  権限検索FORM
   * @param pager ListPager
   */
  private void commonProcess(Model model, PermissionsSearchForm form, ListPager pager) {
    // 権限一覧検索
    permissionService.getPermissionList(form, pager);
    model.addAttribute("permissionsSearchForm", form);
    model.addAttribute("pager", pager);
  }

}
