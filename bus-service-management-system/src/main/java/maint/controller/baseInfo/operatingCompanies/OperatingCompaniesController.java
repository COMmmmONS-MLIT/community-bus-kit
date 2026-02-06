package maint.controller.baseInfo.operatingCompanies;

import jakarta.validation.constraints.DecimalMin;
import maint.controller.BaseController;
import maint.dto.ListPager;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.enums.UsageDivision;
import maint.form.operatingCompanies.OperatingCompaniesSearchForm;
import maint.message.CommonMessage;
import maint.service.baseInfo.OperatingCompaniesService;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 運行会社管理CONTROLLER
 *
 * @author jeon
 */

@Controller
@Validated
@RequestMapping("/baseInfo/operatingCompanies")
@SessionAttributes("operatingCompaniesSearchForm")
public class OperatingCompaniesController extends BaseController {

  /** 一覧画面PATH */
  private static final String INDEX_PAGE = "baseInfo/operatingCompanies/index";
  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.OP_COMPANY;

  /** 運行会社管理SERVICE */
  @Autowired
  OperatingCompaniesService operatingCompaniesService;

  /**
   * 運行会社検索FORM設定
   *
   * @return 運行会社検索FORM
   */
  @ModelAttribute("operatingCompaniesSearchForm")
  public OperatingCompaniesSearchForm setPermissionSearchForm() {
    return new OperatingCompaniesSearchForm();
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
   * 運行会社管理一覧画面（初期表示）
   *
   * @param model Model
   * @param form 運行会社検索FORM
   * @return 一覧画面
   */
  @GetMapping("/")
  public String index(Model model, OperatingCompaniesSearchForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {

    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new OperatingCompaniesSearchForm();
    }

    form.setSearchAvailableFlag(UsageDivision.ACTIVE.getValue());
    commonProcess(model, form, createListPager(model));
    return INDEX_PAGE;
  }

  /**
   * 運行会社管理一覧画面（検索）
   *
   * @param model         Model
   * @param form          運行会社検索FORM
   * @param bindingResult BindingResult
   * @return 運行会社管理一覧画面
   */
  @PostMapping("/")
  public String index(Model model, @Validated OperatingCompaniesSearchForm form,
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
   * @param form  運行会社検索FORM
   * @return 運行会社管理一覧画面
   */
  @GetMapping("/{page}/")
  public String paging(Model model, @DecimalMin("1") @PathVariable("page") Long page,
      @ModelAttribute("operatingCompaniesSearchForm") OperatingCompaniesSearchForm form) {

    hasAccessPermission(NOW_MENU, false, false);

    commonProcess(model, form, createListPager(model, page));

    return INDEX_PAGE;
  }

  /**
   * @param model  Model
   * @param form   運行会社検索FORM
   * @param pager  ListPager
   */
  private void commonProcess(Model model, OperatingCompaniesSearchForm form, ListPager pager) {
    // 運行会社管理一覧検索
    operatingCompaniesService.setOperatingCompaniesList(form, pager);
    model.addAttribute("operatingCompaniesSearchForm", form);
    model.addAttribute("pager", pager);
  }
}

