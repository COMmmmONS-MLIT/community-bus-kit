package maint.controller.export.lists;

import maint.controller.BaseController;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.enums.ReportType;
import maint.form.report.ReportListsExcelForm;
import maint.message.CommonMessage;
import maint.service.operation.LinesService;
import maint.service.export.ReportListsService;
import maint.service.export.ReportListExcelDownloadService;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 一覧表出力CONTROLLER
 *
 * @author JEKAL.H
 */

@Controller
@Validated
@RequestMapping("/export/lists")
@SessionAttributes("reportListsExcelForm")
public class ListsController extends BaseController {

  /**
   * 一覧表出力画面パス
   */
  private static final String INDEX_PAGE = "export/lists/index";

  /**
   * メニュー情報
   */
  private static final Menu NOW_MENU = Menu.REPORT_LIST;

  /**
   * 路線SERVICE
   */
  @Autowired
  LinesService linesService;

  /**
   * 一覧表出力SERVICE
   */
  @Autowired
  ReportListsService reportListsService;

  /**
   * 一覧表出力EXCELダウンロードSERVICE
   */
  @Autowired
  private ReportListExcelDownloadService reportListExcelDownloadService;

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model,
      @ModelAttribute("ReportListsExcelForm") ReportListsExcelForm form) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    model.addAttribute("linesList", linesService.getAllLinesForSelectOption());
    model.addAttribute("reportType", ReportType.values());
    model.addAttribute("daiyaDateList", reportListsService.getAllLinesForSelectOption(form));
    model.addAttribute("downloadFlag", hasDownloadPermission(NOW_MENU));
    execCommonProc(model, NOW_MENU);
  }

  /**
   * 一覧表出力画面（初期表示）
   *
   * @param model    Model
   * @param form     一覧表出力FORM
   * @param initFlag 一覧表出力フォーム初期化フラグ
   * @return 一覧表出力画面
   */
  @GetMapping("/")
  public String index(Model model, ReportListsExcelForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);
    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new ReportListsExcelForm();
    }
    model.addAttribute("reportListsExcelForm", form);
    return INDEX_PAGE;
  }

  /**
   * EXCELダウンロード処理
   *
   * @param model         Model
   * @param form          一覧表出力FORM
   * @param bindingResult BindingResult
   */
  @Transactional
  @PostMapping("/downloadExcel")
  public Object downloadExcel(Model model,
      @Validated @ModelAttribute("reportListsExcelForm") ReportListsExcelForm form,
      BindingResult bindingResult) {

    // 権限チェック
    hasAccessPermission(NOW_MENU, false, true);
    execCommonProc(model, this.NOW_MENU);

    // 入力チェック
    if (bindingResult.hasErrors()) {
      // 出力対象ごとエラーメッセージ表示
      setErrorMsg(CommonMessage.ERROR_INPUT);
      return INDEX_PAGE;
    }
    try {
      // ダウンロード
      return reportListExcelDownloadService.getReportRegisterInfo(form);
    } catch (Exception e) {
      LOGGER.error("【一覧表Excelダウンロード】Excelダウンロード時にエラー", e);
      setExcelDownloadErrorMessage(ReportType.getTextByValue(form.getReportType()));
      return INDEX_PAGE;
    }
  }
}
