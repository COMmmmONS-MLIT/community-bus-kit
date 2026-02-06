package maint.controller.operation.lineRoutes;


import jakarta.validation.constraints.DecimalMin;
import maint.controller.BaseController;
import maint.dto.ListPager;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.form.lineRoutes.LineRoutesSearchForm;
import maint.message.CommonMessage;
import maint.service.operation.LineRoutesService;
import maint.session.UserSession;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系統管理（一覧）Controller
 *
 * @author y.kim
 */

@Controller
@Validated
@RequestMapping(value = "/operation/lineRoutes")
@SessionAttributes("lineRoutesSearchForm")
public class lineRoutesController extends BaseController {

  /** 一覧画面PATH */
  private static final String INDEX_PAGE = "operation/lineRoutes/index";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.SYSTEM_LINE;

  /** 系統SERVICE */
  @Autowired
  LineRoutesService lineRoutesService;

  /**
   * 系統検索FORM設定
   *
   * @return 系統検索FORM
   */
  @ModelAttribute("lineRoutesSearchForm")
  public LineRoutesSearchForm setLinesSearchForm() {
    return new LineRoutesSearchForm();
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
   * 系統一覧画面（GET）
   *
   * @param model    Model
   * @param form     系統検索form
   * @param initFlag 検索フォーム初期化フラグ
   * @return 系統一覧画面
   */
  @GetMapping("/")
  public String index(Model model, LineRoutesSearchForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {

    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new LineRoutesSearchForm();
    }
    // 初期表示（利用中）
    form.setSearchAvailableFlag(1);

    commonProcess(model, form, createListPager(model));

    return INDEX_PAGE;
  }

  /**
   * 系統一覧画面（検索）
   *
   * @param model         Model
   * @param form          系統検索FORM
   * @param bindingResult BindingResult
   * @return 系統一覧画面
   */
  @PostMapping("/")
  public String index(Model model, @Validated LineRoutesSearchForm form,
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
   * @param form  系統検索FORM
   * @return 系統一覧画面
   */
  @GetMapping("/{page}/")
  public String paging(Model model, @DecimalMin("1") @PathVariable("page") Long page,
      @ModelAttribute("lineRoutesSearchForm") LineRoutesSearchForm form) {

    hasAccessPermission(NOW_MENU, false, false);

    commonProcess(model, form, createListPager(model, page));

    return INDEX_PAGE;
  }

  /**
   * 共通処理
   *
   * @param model Model
   * @param form  系統検索FORM
   * @param pager ListPager
   */
  private void commonProcess(Model model, LineRoutesSearchForm form, ListPager pager) {
    UserSession userSession = getUserSession();
    Long businessCorpId = userSession.getBusinessCorpId();
    form.setBusinessCorpId(businessCorpId);

    // 路線一覧 (SELECT用)
    model.addAttribute("selectableLines", lineRoutesService.getSelectableLines(businessCorpId));

    // 系統一覧検索
    lineRoutesService.getLineRouteList(form, pager);
    model.addAttribute("lineRoutesSearchForm", form);
    model.addAttribute("pager", pager);
  }

}
