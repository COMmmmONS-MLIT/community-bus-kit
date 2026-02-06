package maint.controller.baseInfo.agencies;


import jakarta.validation.constraints.DecimalMin;
import maint.controller.BaseController;
import maint.dto.ListPager;
import maint.enums.AccountDivision;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.form.agencies.AgenciesSearchForm;
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

/**
 * 事業者管理（一覧）Controller
 *
 * @author y.kim
 */

@Controller
@Validated
@RequestMapping(value = "/baseInfo/agencies")
@SessionAttributes("agenciesSearchForm")
public class AgenciesController extends BaseController {

  /** 一覧画面PATH */
  private static final String INDEX_PAGE = "baseInfo/agencies/index";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.AGENCY;

  /** 事業者SERVICE */
  @Autowired
  AgenciesService agenciesService;

  /**
   * 事業者検索FORM設定
   *
   * @return 事業者検索FORM
   */
  @ModelAttribute("agenciesSearchForm")
  public AgenciesSearchForm setAgenciesSearchForm() {
    return new AgenciesSearchForm();
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
   * 事業者一覧画面（GET）
   *
   * @param model    Model
   * @param form     事業者検索form
   * @param initFlag 検索フォーム初期化フラグ
   * @return 事業者一覧画面
   */
  @GetMapping("/")
  public String index(Model model, AgenciesSearchForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {

    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new AgenciesSearchForm();
    }
    // 初期表示（利用中）
    form.setSearchAvailableFlag(1);

    commonProcess(model, form, createListPager(model));

    return INDEX_PAGE;
  }

  /**
   * 事業者一覧画面（検索）
   *
   * @param model         Model
   * @param form          事業者検索FORM
   * @param bindingResult BindingResult
   * @return 事業者一覧画面
   */
  @PostMapping("/")
  public String index(Model model, @Validated AgenciesSearchForm form,
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
   * ページング
   *
   * @param model Model
   * @param page  pageIndex
   * @param form  事業者検索FORM
   * @return 事業者一覧画面
   */
  @GetMapping("/{page}/")
  public String paging(Model model, @DecimalMin("1") @PathVariable("page") Long page,
      @ModelAttribute("agenciesSearchForm") AgenciesSearchForm form) {

    hasAccessPermission(NOW_MENU, false, false);

    commonProcess(model, form, createListPager(model, page));

    return INDEX_PAGE;
  }

  /**
   * 共通処理
   *
   * @param model Model
   * @param form  事業者検索FORM
   * @param pager ListPager
   */
  private void commonProcess(Model model, AgenciesSearchForm form, ListPager pager) {
    UserSession userSession = getUserSession();
    String accountDivision = userSession.getAccountDivision();
    // システム管理者ではない、事業者IDに紐づく結果のみを表示
    if (!AccountDivision.SYSTEM_ADMIN.getValue().equals(accountDivision)) {
      form.setBusinessCorpId(userSession.getBusinessCorpId());
    }
    // 事業者一覧検索
    agenciesService.getAgencyList(form, pager);
    // 登録ボタン表示制御用アカウント区分
    model.addAttribute("accountDivision", accountDivision);
    model.addAttribute("agenciesSearchForm", form);
    model.addAttribute("pager", pager);
  }

}
