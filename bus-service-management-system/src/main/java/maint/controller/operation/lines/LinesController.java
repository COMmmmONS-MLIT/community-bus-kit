package maint.controller.operation.lines;


import jakarta.validation.constraints.DecimalMin;
import maint.controller.BaseController;
import maint.dto.ListPager;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.form.lines.LinesSearchForm;
import maint.message.CommonMessage;
import maint.service.operation.LinesService;
import maint.session.UserSession;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 路線管理（一覧）Controller
 *
 * @author y.kim
 */

@Controller
@Validated
@RequestMapping(value = "/operation/lines")
@SessionAttributes("linesSearchForm")
public class LinesController extends BaseController {

  /** 一覧画面PATH */
  private static final String INDEX_PAGE = "operation/lines/index";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.LINES;

  /** 路線SERVICE */
  @Autowired
  LinesService linesService;

  /**
   * 路線検索FORM設定
   *
   * @return 路線検索FORM
   */
  @ModelAttribute("linesSearchForm")
  public LinesSearchForm setLinesSearchForm() {
    return new LinesSearchForm();
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
   * 路線一覧画面（GET）
   *
   * @param model    Model
   * @param form     路線検索form
   * @param initFlag 検索フォーム初期化フラグ
   * @return 路線一覧画面
   */
  @GetMapping("/")
  public String index(Model model, LinesSearchForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {

    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new LinesSearchForm();
    }
    // 初期表示（利用中）
    form.setSearchAvailableFlag(1);

    commonProcess(model, form, createListPager(model));

    return INDEX_PAGE;
  }

  /**
   * 路線一覧画面（検索）
   *
   * @param model         Model
   * @param form          路線検索FORM
   * @param bindingResult BindingResult
   * @return 路線一覧画面
   */
  @PostMapping("/")
  public String index(Model model, @Validated LinesSearchForm form,
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
   * @param form  路線検索FORM
   * @return 路線一覧画面
   */
  @GetMapping("/{page}/")
  public String paging(Model model, @DecimalMin("1") @PathVariable("page") Long page,
      @ModelAttribute("linesSearchForm") LinesSearchForm form) {

    hasAccessPermission(NOW_MENU, false, false);

    commonProcess(model, form, createListPager(model, page));

    return INDEX_PAGE;
  }

  /**
   * 共通処理
   *
   * @param model Model
   * @param form  路線検索FORM
   * @param pager ListPager
   */
  private void commonProcess(Model model, LinesSearchForm form, ListPager pager) {
    UserSession userSession = getUserSession();
    form.setBusinessCorpId(userSession.getBusinessCorpId());

    // 路線一覧検索
    linesService.getLineList(form, pager);
    model.addAttribute("linesSearchForm", form);
    model.addAttribute("pager", pager);
  }

}
