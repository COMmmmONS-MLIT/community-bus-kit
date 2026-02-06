package maint.controller.baseInfo.busStopSignposts;

import jakarta.validation.constraints.DecimalMin;
import maint.controller.BaseController;
import maint.dto.ListPager;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.form.busStops.BusStopsSearchForm;
import maint.message.CommonMessage;
import maint.service.baseInfo.BusStopsService;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 停留所・標柱CONTROLLER
 *
 * @author k.takeuchi
 */

@Controller
@Validated
@RequestMapping("/baseInfo/busStopSignposts")
@SessionAttributes("busStopsSearchForm")
public class BusStopSignpostsController extends BaseController {

  /** 停留所・標柱画面パス */
  private static final String INDEX_PAGE = "baseInfo/busStopSignposts/index";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.STOP_MARKER;

  /** 停留所SERVICE */
  @Autowired
  BusStopsService busStopsService;

  /**
   * 詳細FORM設定
   *
   * @return 詳細FORM
   */
  @ModelAttribute("busStopsSearchForm")
  public BusStopsSearchForm setBusStopsSearchForm() {
    return new BusStopsSearchForm();
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
   * 停留所一覧画面（初期表示）
   *
   * @param model Model
   * @param form 停留所検索FORM
   * @param initFlag 検索フォーム初期化フラグ
   * @return 停留所一覧画面
   */
  @GetMapping("/")
  public String index(Model model, BusStopsSearchForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new BusStopsSearchForm();
    }

    commonProcess(model, form, createListPager(model));
    return INDEX_PAGE;
  }

  /**
   * 停留所一覧画面(検索)
   *
   * @param model Model
   * @param form 停留所検索FORM
   * @param bindingResult BindingResult
   * @return 停留所一覧画面
   */
  @PostMapping("/")
  public String search(Model model,
      @Validated @ModelAttribute("busStopsSearchForm") BusStopsSearchForm form,
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
   * @param page ページ
   * @param form 停留所検索FORM
   * @return 停留所一覧画面
   */
  @GetMapping(value = "/{page}/")
  public String paging(Model model, @DecimalMin("1") @PathVariable("page") Long page,
      @ModelAttribute("busStopsSearchForm") BusStopsSearchForm form) {
    hasAccessPermission(NOW_MENU, false, false);
    commonProcess(model, form, createListPager(model, page));
    return INDEX_PAGE;
  }

  /**
   * 共通処理
   *
   * @param model Model
   * @param form 停留所検索FORM
   * @param pager ListPager
   */
  private void commonProcess(Model model, BusStopsSearchForm form, ListPager pager) {
    // 停留所一覧検索
    busStopsService.getBusStops(form, pager);
    model.addAttribute("busStopsSearchForm", form);
    model.addAttribute("pager", pager);
  }
}
