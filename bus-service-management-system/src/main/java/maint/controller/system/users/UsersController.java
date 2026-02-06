package maint.controller.system.users;

import jakarta.validation.constraints.DecimalMin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import maint.controller.BaseController;
import maint.dto.ListPager;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.form.users.UsersSearchForm;
import maint.message.CommonMessage;
import maint.service.system.UsersService;
import maint.utils.ExObjectUtils;

/**
 * ユーザーCONTROLLER
 *
 * @author ryuichi.kashiwai
 */

@Controller
@Validated
@RequestMapping("/system/users")
@SessionAttributes("usersSearchForm")
public class UsersController extends BaseController {

  /** ユーザ画面パス */
  private static final String INDEX_PAGE = "system/users/index";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.ACCOUNT;

  /** ユーザ一SERVICE */
  @Autowired
  UsersService usersService;

  /**
   * 詳細FORM設定
   *
   * @return 詳細FORM
   */
  @ModelAttribute("usersSearchForm")
  public UsersSearchForm setUsersSearchForm() {
    return new UsersSearchForm();
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
   * ユーザ一覧画面（初期表示）
   *
   * @param model    Model
   * @param form     ユーザ検索FORM
   * @param initFlag 検索フォーム初期化フラグ
   * @return 管理ユーザ一覧画面
   */
  @GetMapping("/")
  public String index(Model model, UsersSearchForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new UsersSearchForm();
    }
    commonProcess(model, form, createListPager(model));
    return INDEX_PAGE;
  }

  /**
   * ユーザ一覧画面(検索)
   *
   * @param model         Model
   * @param form          ユーザ一検索FORM
   * @param bindingResult BindingResult
   * @return 管理ユーザ一覧画面
   */
  @PostMapping("/")
  public String search(Model model, @Validated @ModelAttribute("usersSearchForm") UsersSearchForm form,
      BindingResult bindingResult) {
    hasAccessPermission(NOW_MENU, false, false);
    if (bindingResult.hasErrors()) {
      setErrorMsg(CommonMessage.ERROR_INPUT);
    }
    form.setPage(1L);
    commonProcess(model, form, createListPager(model));
    return INDEX_PAGE;
  }

  /**
   * ページング
   *
   * @param model Model
   * @param page  ページ
   * @param form  ユーザ一検索FORM
   * @return 管理ユーザ一覧画面
   */
  @GetMapping(value = "/{page}/")
  public String paging(Model model, @DecimalMin("1") @PathVariable("page") Long page,
      @ModelAttribute("usersSearchForm") UsersSearchForm form) {
    hasAccessPermission(NOW_MENU, false, false);
    commonProcess(model, form, createListPager(model, page));
    return INDEX_PAGE;
  }

  /**
   * 共通処理
   *
   * @param model Model
   * @param form  ユーザー検索FORM
   * @param pager ListPager
   */
  private void commonProcess(Model model, UsersSearchForm form, ListPager pager) {
    // 管理ユーザ一覧検索
    usersService.getUsers(form, pager);
    model.addAttribute("usersSearchForm", form);
    model.addAttribute("pager", pager);
  }
}
