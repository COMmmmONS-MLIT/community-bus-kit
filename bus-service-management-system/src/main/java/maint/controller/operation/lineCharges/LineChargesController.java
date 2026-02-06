package maint.controller.operation.lineCharges;

import jakarta.validation.constraints.DecimalMin;
import maint.controller.BaseController;
import maint.dto.ListPager;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.form.lineCharges.LineChargeSearchForm;
import maint.message.CommonMessage;
import maint.service.operation.LineChargesService;
import maint.service.operation.LinesService;
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
 * 運賃CONTROLLER
 *
 * @author g.ko
 */

@Controller
@Validated
@RequestMapping("/operation/lineCharges")
@SessionAttributes("lineChargeSearchForm")
public class LineChargesController extends BaseController {

  /** 運賃画面パス */
  private static final String INDEX_PAGE = "operation/lineCharges/index";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.FARE;

  /** 運賃SERVICE */
  @Autowired
  LineChargesService lineChargesService;

  /** 路線SERVICE */
  @Autowired
  LinesService linesService;

  /**
   * 詳細FORM設定
   *
   * @return 詳細FORM
   */
  @ModelAttribute("lineChargeSearchForm")
  public LineChargeSearchForm setLineChargeSearchForm() {
    return new LineChargeSearchForm();
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
   * 運賃一覧画面（初期表示）
   *
   * @param model Model
   * @param form 運賃検索FORM
   * @param initFlag 検索フォーム初期化フラグ
   * @return 運賃一覧画面
   */
  @GetMapping("/")
  public String index(Model model, LineChargeSearchForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new LineChargeSearchForm();
    }
    commonProcess(model, form, createListPager(model));
    return INDEX_PAGE;
  }

  /**
   * 運賃一覧画面(検索)
   *
   * @param model Model
   * @param form 運賃検索FORM
   * @param bindingResult BindingResult
   * @return 運賃一覧画面
   */
  @PostMapping("/")
  public String search(Model model,
      @Validated @ModelAttribute("lineChargeSearchForm") LineChargeSearchForm form,
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
   * @param form 運賃検索FORM
   * @return 運賃一覧画面
   */
  @GetMapping(value = "/{page}/")
  public String paging(Model model, @DecimalMin("1") @PathVariable("page") Long page,
      @ModelAttribute("lineChargeSearchForm") LineChargeSearchForm form) {
    hasAccessPermission(NOW_MENU, false, false);
    commonProcess(model, form, createListPager(model, page));
    return INDEX_PAGE;
  }

  /**
   * 共通処理
   *
   * @param model Model
   * @param form 運賃検索FORM
   * @param pager ListPager
   */
  private void commonProcess(Model model, LineChargeSearchForm form, ListPager pager) {
    // 運賃一覧検索
    lineChargesService.getLineCharges(form, pager);
    // 路線取得（SELECT用）
    model.addAttribute("lineList", linesService.getBusStopsForSelectOption());
    model.addAttribute("lineChargeSearchForm", form);
    model.addAttribute("pager", pager);
  }
}
