package maint.controller.system.users;

import jakarta.validation.constraints.DecimalMin;
import maint.enums.UsageDivision;
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
      form.setViewUsageDivision(UsageDivision.ACTIVE.getValue());
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
    // 入力・権限チェック
    hasAccessPermission(NOW_MENU, false, false);
    if (bindingResult.hasErrors()) {
      setErrorMsg(CommonMessage.ERROR_INPUT);
    }

    // 利用区分を退職フラグとして検索するため、0／1入れ替えて検索用Formにセット
    crossMoveUsageDivision(form);

    // 検索処理
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
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // 利用区分を退職フラグとして検索するため、0／1入れ替えて検索用Formにセット
    crossMoveUsageDivision(form);

    // ページ切り替え
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

  /**
   * 画面表示用から検索用に利用区分のセットしなおし
   *
   * @param form  ユーザー検索FORM
   */
  private void crossMoveUsageDivision(UsersSearchForm form) {
    // ユーザ管理画面での利用区分はデータ上では退職フラグを参照としており、
    // 設定値が以下のようになっているため0／1を入れ替えてFormに設定している。
    // 利用区分：0(利用不可)／1(利用可)　⇔　退職フラグ：0(在職)／1(退職)
    if (form.getViewUsageDivision().equals(UsageDivision.DISABLED.getValue())) {
      form.setSearchUsageDivision(UsageDivision.ACTIVE.getValue());
    } else {
      form.setSearchUsageDivision(UsageDivision.DISABLED.getValue());
    }
  }
}
