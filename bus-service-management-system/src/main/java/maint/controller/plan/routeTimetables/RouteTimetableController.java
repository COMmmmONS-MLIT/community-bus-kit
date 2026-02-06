package maint.controller.plan.routeTimetables;

import maint.controller.BaseController;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.form.report.RouteTimetableExcelForm;
import maint.message.CommonMessage;
import maint.service.operation.LinesService;
import maint.service.plan.RouteTimetableListService;
import maint.service.plan.RouteTimetableService;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;


/**
 * 路線時刻表出力CONTROLLER
 *
 * @author 林竜生
 */

@Controller
@Validated
@RequestMapping("/plan/routeTimetable")
@SessionAttributes("routeTimetableExcelForm")

public class RouteTimetableController extends BaseController {

  /** 路線時刻表出力画面PATH */
  private static final String INDEX_PAGE = "plan/routeTimetable/index";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.ROUTE_TIMETABLE;

  /** 路線SERVICE */
  @Autowired
  LinesService linesService;

  /** 路線時刻表SERVICE */
  @Autowired
  RouteTimetableService routeTimetableService;

  /** 路線時刻表出力SERVICE */
  @Autowired
  private RouteTimetableListService routeTimetableListService;

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model, @ModelAttribute("routeTimetableExcelForm")
  RouteTimetableExcelForm form) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    model.addAttribute("linesList", linesService.getAllLinesForSelectOption());
    model.addAttribute("serviceStartDateList", routeTimetableService.getServiceStartDateList());
    model.addAttribute("downloadFlag", hasDownloadPermission(NOW_MENU));
    execCommonProc(model, NOW_MENU);
  }

  /**
   * 路線時刻表出力画面（初期表示）
   *
   * @param model Model
   * @param form 路線時刻表出力FORM
   * @param initFlag 路線時刻表出力フォーム初期化フラグ
   * @return 路線時刻表出力画面
   */
  @GetMapping("/")
  public String index(Model model, RouteTimetableExcelForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);
    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new RouteTimetableExcelForm();
    }

    model.addAttribute("routeTimetableExcelForm", form);
    return INDEX_PAGE;
  }

  /**
   * EXCELダウンロード処理
   *
   * @param model Model
   * @param form 路線時刻表出力FORM
   * @param bindingResult BindingResult
   */
  @Transactional
  @PostMapping("/downloadExcel")
  public Object downloadExcel(Model model,
      @Validated @ModelAttribute("routeTimetableExcelForm") RouteTimetableExcelForm form,
      BindingResult bindingResult) {

    // 権限チェック
    hasAccessPermission(NOW_MENU, false, true);

    execCommonProc(model, this.NOW_MENU);

    // 入力チェック
    if (bindingResult.hasErrors()) {
      LOGGER.warn(">>> Validation errors: {}", bindingResult.getAllErrors());
      setErrorMsg(CommonMessage.ERROR_INPUT);
      return INDEX_PAGE;
    }
    try {
      // ダウンロード
      LOGGER.info(">>> Calling routeTimetableListService.getRouteTimetableInfo");
      return routeTimetableListService.getRouteTimetableInfo(form);
    } catch (Exception e) {
      LOGGER.error("【路線時刻表Excelダウンロード】Excelダウンロード時にエラー", e);
      setExcelDownloadErrorMessage("路線時刻表");
      return INDEX_PAGE;
    }
  }
}
