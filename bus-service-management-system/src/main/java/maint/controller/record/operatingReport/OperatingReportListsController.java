package maint.controller.record.operatingReport;

import maint.controller.BaseController;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.form.operatingReport.OperatingReportListsExcelForm;
import maint.message.CommonMessage;
import maint.service.record.OperatingReportListRegisterService;
import maint.service.record.OperatingReportListsService;
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
 * 運行回数出力CONTROLLER
 *
 * @author JEKAL.H
 */

@Controller
@Validated
@RequestMapping("/record/operatingReport")
@SessionAttributes("operatingReportExcelForm")
public class OperatingReportListsController extends BaseController {

  /**
   * 運行回数出力画面パス
   */
  private static final String INDEX_PAGE = "record/operatingReport/index";

  /**
   * メニュー情報
   */
  private static final Menu NOW_MENU = Menu.RUN_COUNT;

  /**
   * 路線SERVICE
   */
  @Autowired
  LinesService linesService;

  /**
   * 運行回数出力SERVICE
   */
  @Autowired
  OperatingReportListsService operatingReportListsService;

  @Autowired
  private OperatingReportListRegisterService operatingReportListRegisterService;

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model,
      @ModelAttribute("operatingReportListsExcelForm") OperatingReportListsExcelForm form) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    model.addAttribute("linesList", linesService.getAllLinesForSelectOption());
    model.addAttribute("daiyaDateList",
        operatingReportListsService.getOperatingReportListsSelectOption());
    model.addAttribute("downloadFlag", hasDownloadPermission(NOW_MENU));
    execCommonProc(model, NOW_MENU);
  }

  /**
   * 運行回数出力画面（初期表示）
   *
   * @param model    Model
   * @param form     運行回数出力FORM
   * @param initFlag 運行回数出力フォーム初期化フラグ
   * @return 運行回数出力画面
   */
  @GetMapping("/")
  public String index(Model model, OperatingReportListsExcelForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);
    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new OperatingReportListsExcelForm();
    }
    // システム日付(yyyy-MM)で初期化（未設定時のみ）
    if (form.getServiceStartMonth() == null || form.getServiceStartMonth().isBlank()
        || form.getServiceEndMonth() == null || form.getServiceEndMonth().isBlank()) {
      String nowYm = java.time.YearMonth.now()
          .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
      if (form.getServiceStartMonth() == null || form.getServiceStartMonth().isBlank()) {
        form.setServiceStartMonth(nowYm);
      }
      if (form.getServiceEndMonth() == null || form.getServiceEndMonth().isBlank()) {
        form.setServiceEndMonth(nowYm);
      }
    }

    model.addAttribute("operatingReportListsExcelForm", form);
    return INDEX_PAGE;
  }

  /**
   * EXCELダウンロード処理
   *
   * @param model         Model
   * @param form          運行回数出力FORM
   * @param bindingResult BindingResult
   */
  @Transactional
  @PostMapping("/downloadExcel")
  public Object downloadExcel(Model model,
      @Validated @ModelAttribute("operatingReportListsExcelForm")
      OperatingReportListsExcelForm form, BindingResult bindingResult) {
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
      return operatingReportListRegisterService.getOperatingReportDaiyasRegisterInfo(form);
    } catch (Exception e) {
      LOGGER.error("【運行回数Excelダウンロード】Excelダウンロード時にエラー", e);
      setExcelDownloadErrorMessage("運行回数");
      return INDEX_PAGE;
    }
  }
}
