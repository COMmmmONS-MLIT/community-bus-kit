package maint.controller.plan.fareReport;

import maint.controller.BaseController;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.form.fareReport.FareReportListsExcelForm;
import maint.message.CommonMessage;
import maint.service.plan.FareReportListRegisterService;
import maint.service.plan.FareReportListsService;
import maint.service.operation.LinesService;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 運賃表出力CONTROLLER
 *
 * @author JEKAL.H
 */

@Controller
@Validated
@RequestMapping("/plan/fareReport")
@SessionAttributes("fareReportListsExcelForm")
public class FareReportListsController extends BaseController {

  /**
   * 運賃表出力画面
   */
  private static final String INDEX_PAGE = "plan/fareReport/index";

  /**
   * メニュー情報
   */
  private static final Menu NOW_MENU = Menu.FARE_TABLE;

  /**
   * 路線SERVICE
   */
  @Autowired
  LinesService linesService;

  /**
   * 運賃表出力SERVICE
   */
  @Autowired
  FareReportListsService fareReportListsService;

  @Autowired
  private FareReportListRegisterService fareReportListRegisterService;

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model,
      @ModelAttribute("fareReportListsExcelForm") FareReportListsExcelForm form) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    model.addAttribute("linesList", linesService.getAllLinesForSelectOption());
    model.addAttribute("chargeRevisionDateList", fareReportListsService.getFareReportListsSelectOption());
    model.addAttribute("downloadFlag", hasDownloadPermission(NOW_MENU));

    execCommonProc(model, NOW_MENU);
  }

  /**
   * 運賃表出力画面（初期表示）
   *
   * @param model    Model
   * @param form     運賃表出力FORM
   * @param initFlag 運賃表出力フォーム初期化フラグ
   * @return 運賃表出力画面
   */
  @GetMapping("/")
  public String index(Model model, FareReportListsExcelForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);
    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new FareReportListsExcelForm();
    }

    model.addAttribute("fareReportListsExcelForm", form);
    return INDEX_PAGE;
  }

  /**
   * EXCELダウンロード処理
   *
   * @param model         Model
   * @param form          運賃表出力FORM
   * @param bindingResult BindingResult
   */
  @Transactional
  @PostMapping("/downloadExcel")
  public Object downloadExcel(Model model,
      @Validated @ModelAttribute("fareReportListsExcelForm") FareReportListsExcelForm form,
      BindingResult bindingResult) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, true);
    execCommonProc(model, this.NOW_MENU);

    // 入力チェック
    if (bindingResult.hasErrors()) {
      setErrorMsg(CommonMessage.ERROR_INPUT);
      return INDEX_PAGE;
    }
    try {
      // ダウンロード
      return fareReportListRegisterService.getFareReportInfo(form);
    } catch (Exception e) {
      LOGGER.error("【運賃表Excelダウンロード】Excelダウンロード時にエラー", e);
      setExcelDownloadErrorMessage("運賃表");
      return INDEX_PAGE;
    }
  }
}
