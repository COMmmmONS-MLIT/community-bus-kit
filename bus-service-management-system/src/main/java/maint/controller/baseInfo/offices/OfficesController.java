package maint.controller.baseInfo.offices;

import jakarta.validation.constraints.DecimalMin;
import maint.controller.BaseController;
import maint.dto.ListPager;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.enums.UsageDivision;
import maint.form.offices.OfficesSearchForm;
import maint.message.CommonMessage;
import maint.service.baseInfo.OperatingCompaniesService;
import maint.service.baseInfo.OfficesService;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 営業所管理CONTROLLER
 *
 * @author jeon
 */

@Controller
@Validated
@RequestMapping("/baseInfo/offices")
@SessionAttributes("officesSearchForm")
public class OfficesController extends BaseController {

  /** 一覧画面PATH */
  private static final String INDEX_PAGE = "baseInfo/offices/index";
  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.OFFICE;

  /** 営業所管理SERVICE */
  @Autowired
  OfficesService officesService;
  /** 運行会社SERVICE */
  @Autowired
  OperatingCompaniesService operatingCompanyService;

  /**
   * 営業所管理検索FORM設定
   *
   * @return 営業所管理検索FORM
   */
  @ModelAttribute("officesSearchForm")
  public OfficesSearchForm setPermissionSearchForm() {
    return new OfficesSearchForm();
  }

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    model.addAttribute("operatingCompanies",operatingCompanyService.getOperatingCompaniesForSelectOption(getUserSession().getBusinessCorpId()));
    execCommonProc(model, this.NOW_MENU);
  }

  /**
   * 営業所一覧画面（初期表示）
   *
   * @param model Model
   * @param form  営業所管理検索FORM
   * @return 営業所一覧画面
   */
  @GetMapping("/")
  public String index(Model model, OfficesSearchForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {

    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new OfficesSearchForm();
    }

    form.setSearchAvailableFlag(UsageDivision.ACTIVE.getValue());
    commonProcess(model, form, createListPager(model));
    return INDEX_PAGE;
  }

  /**
   * 営業所一覧画面（検索）
   *
   * @param model         Model
   * @param form          営業所管理検索FORM
   * @param bindingResult BindingResult
   * @return 営業所一覧画面
   */
  @PostMapping("/")
  public String index(Model model, @Validated OfficesSearchForm form, BindingResult bindingResult) {

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
   * @param form  営業所管理検索FORM
   * @return 営業所一覧画面
   */
  @GetMapping("/{page}/")
  public String paging(Model model, @DecimalMin("1") @PathVariable("page") Long page,
      @ModelAttribute("officesSearchForm") OfficesSearchForm form) {

    hasAccessPermission(NOW_MENU, false, false);

    commonProcess(model, form, createListPager(model, page));

    return INDEX_PAGE;
  }

  /**
   * @param model  Model
   * @param form   営業所管理検索FORM
   * @param pager  ListPager
   */
  private void commonProcess(Model model, OfficesSearchForm form, ListPager pager) {
    // 営業所一覧検索
    officesService.setOfficesList(form, pager);
    model.addAttribute("officesSearchForm", form);
    model.addAttribute("operatingCompanyList",operatingCompanyService.getOperatingCompaniesForSelectOption(getUserSession().getBusinessCorpId()));
    model.addAttribute("pager", pager);
  }
}

