package maint.controller.planDaiya.vehicles;

import jakarta.validation.constraints.DecimalMin;
import maint.controller.BaseController;
import maint.dto.ListPager;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.enums.UsageDivision;
import maint.form.vehicles.VehiclesSearchForm;
import maint.message.CommonMessage;
import maint.service.baseInfo.OperatingCompaniesService;
import maint.service.planDaiya.VehiclesService;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 車両管理CONTROLLER
 *
 * @author jeon
 */

@Controller
@Validated
@RequestMapping("/planDaiya/vehicles")
@SessionAttributes("vehiclesSearchForm")
public class VehiclesController extends BaseController {

  /** 一覧画面PATH */
  private static final String INDEX_PAGE = "planDaiya/vehicles/index";
  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.VEHICLE;

  /** 車両管理SERVICE */
  @Autowired
  VehiclesService vehiclesService;

  /** 運行会社SERVICE */
  @Autowired
  OperatingCompaniesService operatingCompanyService;

  /**
   * 車両検索FORM設定
   *
   * @return 車両検索FORM
   */
  @ModelAttribute("vehiclesSearchForm")
  public VehiclesSearchForm setPermissionSearchForm() {
    return new VehiclesSearchForm();
  }

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    execCommonProc(model,this.NOW_MENU);
  }

  /**
   * 車両管理一覧画面（初期表示）
   *
   * @param model Model
   * @param form  車両検索FORM
   * @return 一覧画面
   */
  @GetMapping("/")
  public String index(Model model, VehiclesSearchForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {

    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new VehiclesSearchForm();
    }

    form.setSearchAvailableFlag(UsageDivision.ACTIVE.getValue());
    commonProcess(model, form, createListPager(model));
    return INDEX_PAGE;
  }

  /**
   * 車両管理一覧画面（検索）
   *
   * @param model         Model
   * @param form          車両検索FORM
   * @param bindingResult BindingResult
   * @return 車両管理一覧画面
   */
  @PostMapping("/")
  public String index(Model model, @Validated VehiclesSearchForm form,
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
   * @param form  車両検索FORM
   * @return 車両管理一覧画面
   */
  @GetMapping("/{page}/")
  public String paging(Model model, @DecimalMin("1") @PathVariable("page") Long page,
      @ModelAttribute("vehiclesSearchForm") VehiclesSearchForm form) {

    hasAccessPermission(NOW_MENU, false, false);

    commonProcess(model, form, createListPager(model, page));

    return INDEX_PAGE;
  }

  /**
   * @param model  Model
   * @param form   車両検索FORM
   * @param pager  ListPager
   */
  private void commonProcess(Model model, VehiclesSearchForm form, ListPager pager) {
    // 車両管理一覧検索
    vehiclesService.setVehiclesList(form, pager);
    model.addAttribute("operatingCompanyList",operatingCompanyService.getOperatingCompaniesForSelectOption(getUserSession().getBusinessCorpId()));
    model.addAttribute("vehiclesSearchForm", form);
    model.addAttribute("pager", pager);

  }
}

