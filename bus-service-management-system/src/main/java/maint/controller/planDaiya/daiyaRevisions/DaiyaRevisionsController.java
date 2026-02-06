package maint.controller.planDaiya.daiyaRevisions;

import jakarta.validation.constraints.DecimalMin;
import maint.controller.BaseController;
import maint.dto.ListPager;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.form.daiyaRevisions.DaiyaRevisionsSearchForm;
import maint.message.CommonMessage;
import maint.service.planDaiya.DaiyaRevisionsService;
import maint.service.operation.LinesService;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 便ダイヤ改正管理CONTROLLER
 *
 * @author jeon
 */

@Controller
@Validated
@RequestMapping("/planDaiya/daiyaRevisions")
@SessionAttributes("daiyaRevisionsSearchForm")
public class DaiyaRevisionsController extends BaseController {

  /** 一覧画面PATH */
  private static final String INDEX_PAGE = "planDaiya/daiyaRevisions/index";
  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.TIMETABLE_REVISION;

  /** 便ダイヤ改正SERVICE */
  @Autowired
  DaiyaRevisionsService daiyaRevisionsService;

  /** 路線SERVICE */
  @Autowired
  LinesService linesService;

  /**
   * 便ダイヤ改正検索FORM設定
   *
   * @return 便ダイヤ改正検索FORM
   */
  @ModelAttribute("daiyaRevisionsSearchForm")
  public DaiyaRevisionsSearchForm setPermissionSearchForm() {
    return new DaiyaRevisionsSearchForm();
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
   * 便ダイヤ改正管理一覧画面（初期表示）
   *
   * @param model Model
   * @param form  便ダイヤ改正検索FORM
   * @return 一覧画面
   */
  @GetMapping("/")
  public String index(Model model, DaiyaRevisionsSearchForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {

    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new DaiyaRevisionsSearchForm();
    }

    commonProcess(model, form, createListPager(model));
    return INDEX_PAGE;
  }

  /**
   * 便ダイヤ改正管理一覧画面（検索）
   *
   * @param model         Model
   * @param form          便ダイヤ改正検索FORM
   * @param bindingResult BindingResult
   * @return 便ダイヤ改正管理一覧画面
   */
  @PostMapping("/")
  public String index(Model model, @Validated DaiyaRevisionsSearchForm form,
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
   * @param form  便ダイヤ改正検索FORM
   * @return 便ダイヤ改正管理一覧画面
   */
  @GetMapping("/{page}/")
  public String paging(Model model, @DecimalMin("1") @PathVariable("page") Long page,
      @ModelAttribute("daiyaRevisionsSearchForm") DaiyaRevisionsSearchForm form) {

    hasAccessPermission(NOW_MENU, false, false);

    commonProcess(model, form, createListPager(model, page));

    return INDEX_PAGE;
  }

  /**
   * @param model  Model
   * @param form   便ダイヤ改正検索FORM
   * @param pager  ListPager
   */
  private void commonProcess(Model model, DaiyaRevisionsSearchForm form, ListPager pager) {
    // 便ダイヤ改正管理一覧検索
    daiyaRevisionsService.setDaiyaRevisionsList(form, pager);
    model.addAttribute("linesList", linesService.getAllLinesForSelectOption(getUserSession().getBusinessCorpId()));
    model.addAttribute("daiyaRevisionsSearchForm", form);
    model.addAttribute("pager", pager);

  }
}

