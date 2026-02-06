package maint.controller.opeInfo.staffReports;

import maint.controller.BaseController;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.form.report.StaffReportExcelForm;
import maint.message.CommonMessage;
import maint.service.operation.LinesService;
import maint.service.opeInfo.StaffReportListService;
import maint.service.opeInfo.StaffReportService;
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
 * スターフ出力CONTROLLER
 *
 * @author 林竜生
 */

@Controller
@Validated
@RequestMapping("/opeInfo/staffReport")
@SessionAttributes("staffReportExcelForm")

public class StaffReportController extends BaseController {

  /** スターフ出力画面PATH */
  private static final String INDEX_PAGE = "opeInfo/staffReport/index";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.STAFF_REPORT;

  /** スターフSERVICE */
  @Autowired
  LinesService linesService;

  /** スターフSERVICE */
  @Autowired
  StaffReportService staffReportService;

  /** スターフSERVICE */
  @Autowired
  private StaffReportListService staffReportListService;

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model, @ModelAttribute("staffReportExcelForm")
  StaffReportExcelForm form) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    model.addAttribute("linesList", linesService.getAllLinesForSelectOption());
    model.addAttribute("serviceStartDateList", staffReportService.getServiceStartDateList());
    model.addAttribute("downloadFlag", hasDownloadPermission(NOW_MENU));
    execCommonProc(model, NOW_MENU);
  }

  /**
   * スターフ出力画面（初期表示）
   *
   * @param model Model
   * @param form スターフ出力FORM
   * @param initFlag スターフ出力フォーム初期化フラグ
   * @return スターフ出力画面
   */
  @GetMapping("/")
  public String index(Model model, StaffReportExcelForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);
    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new StaffReportExcelForm();
    }

    model.addAttribute("staffReportExcelForm", form);
    return INDEX_PAGE;
  }

  /**
   * EXCELダウンロード処理
   *
   * @param model Model
   * @param form スターフ出力FORM
   * @param bindingResult BindingResult
   */
  @Transactional
  @PostMapping("/downloadExcel")
  public Object downloadExcel(Model model,
      @Validated @ModelAttribute("staffReportExcelForm") StaffReportExcelForm form,
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
      LOGGER.info(">>> Calling staffReportListService.getStaffReportInfo");
      return staffReportListService.getStaffReportInfo(form);
    } catch (Exception e) {
      LOGGER.error("【スターフExcelダウンロード】Excelダウンロード時にエラー", e);
      setExcelDownloadErrorMessage("スターフ");
      return INDEX_PAGE;
    }
  }
}
