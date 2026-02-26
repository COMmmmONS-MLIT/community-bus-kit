package maint.controller.planDaiya.dailyBusDaiyas;

import jakarta.validation.constraints.DecimalMin;
import maint.controller.BaseController;
import maint.dto.ListPager;
import maint.dto.operation.busDaiyas.BusServicesOptionDto;
import maint.dto.planDaiya.dailyBusDaiyas.SearchDailyBusDaiyasDto;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.form.dailyBusDaiyas.DailyBusDaiyasSearchForm;
import maint.message.CommonMessage;
import maint.service.operation.BusDaiyasService;
import maint.service.planDaiya.DailyBusDaiyasService;
import maint.service.operation.LinesService;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 日毎便ダイヤ管理CONTROLLER
 *
 * @author jeon
 */

@Controller
@Validated
@RequestMapping("/planDaiya/dailyBusDaiyas")
@SessionAttributes("dailyBusDaiyasSearchForm")
public class DailyBusDaiyasController extends BaseController {

  /** 一覧画面PATH */
  private static final String INDEX_PAGE = "planDaiya/dailyBusDaiyas/index";
  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.DAILY_TIMETABLE;
  /** 日毎便ダイヤSERVICE */
  @Autowired
  DailyBusDaiyasService dailyBusDaiyasService;
  /** 路線SERVICE */
  @Autowired
  LinesService linesService;
  /** 便SERVICE */
  @Autowired
  BusDaiyasService busDaiyasService;

  /**
   * 日毎便ダイヤ検索FORM設定
   *
   * @return 日毎便ダイヤ検索FORM
   */
  @ModelAttribute("dailyBusDaiyasSearchForm")
  public DailyBusDaiyasSearchForm setPermissionSearchForm() {
    return new DailyBusDaiyasSearchForm();
  }

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    execCommonProc(model, this.NOW_MENU);
  }

  /**
   * 日毎便ダイヤ管理一覧画面（初期表示）
   *
   * @param model Model
   * @param form  日毎便ダイヤ検索FORM
   * @return 一覧画面
   */
  @GetMapping("/")
  public String index(Model model, DailyBusDaiyasSearchForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {

    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new DailyBusDaiyasSearchForm();
    }
    //運休フラグ
    form.setSearchSalesCancelFlg(FlagValueEnums.FALSE.getValue());

    commonProcess(model, form, createListPager(model));
    return INDEX_PAGE;
  }

  /**
   * 日毎便ダイヤ管理一覧画面（検索）
   *
   * @param model         Model
   * @param form          日毎便ダイヤ検索FORM
   * @param bindingResult BindingResult
   * @return 日毎便ダイヤ管理一覧画面
   */
  @PostMapping("/")
  public String index(Model model, @Validated DailyBusDaiyasSearchForm form,
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
   * @param form  日毎便ダイヤ検索FORM
   * @return 日毎便ダイヤ管理一覧画面
   */
  @GetMapping("/{page}/")
  public String paging(Model model, @DecimalMin("1") @PathVariable("page") Long page,
      @ModelAttribute("dailyBusDaiyasSearchForm") DailyBusDaiyasSearchForm form) {

    hasAccessPermission(NOW_MENU, false, false);

    commonProcess(model, form, createListPager(model, page));

    return INDEX_PAGE;
  }

  /**
   * 画面表示項目などのModelへの設定
   *
   * @param model Model
   * @param form  日毎便ダイヤ検索FORM
   * @param pager ListPager
   */
  private void commonProcess(Model model, DailyBusDaiyasSearchForm form, ListPager pager) {
    // 日毎便ダイヤ管理一覧検索
    dailyBusDaiyasService.setDailyBusDaiyasList(form, pager);
    model.addAttribute("linesList",
        linesService.getAllLinesForSelectOption(getUserSession().getBusinessCorpId()));
    model.addAttribute("dailyBusDaiyasSearchForm", form);
    model.addAttribute("pager", pager);
    model.addAttribute("busServiceNameList", busDaiyasService.getBusServicesForSelectOption(
        new SearchDailyBusDaiyasDto(form, pager.getStartIndex())));
  }

  /**
   * 便オプション取得（SELECTボックス用）
   *
   * @param form  日毎便ダイヤ検索FORM
   * @return 便の選択肢DTOリスト
   */
  @GetMapping("/getBusServiceOption")
  @ResponseBody
  public List<BusServicesOptionDto> getBusServices(@Validated DailyBusDaiyasSearchForm form) {

    return busDaiyasService.getBusServicesForSelectOption(
        new SearchDailyBusDaiyasDto(form, form.getPage()));

  }
}

