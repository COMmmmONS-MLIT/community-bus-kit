package maint.controller.document.distanceReport;

import maint.controller.BaseController;
import maint.enums.Menu;
import maint.form.distanceReport.DistanceReportForm;
import maint.message.CommonMessage;
import maint.service.operation.LineRoutesService;
import maint.service.document.DistanceReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * キロ程表出力CONTROLLER
 *
 * @author y.kim
 */

@Controller
@Validated
@RequestMapping("/document/distanceReport")
public class DistanceReportController extends BaseController {

  /**
   * キロ程表出力画面パス
   */
  private static final String INDEX_PAGE = "document/distanceReport/index";

  /**
   * メニュー情報
   */
  private static final Menu NOW_MENU = Menu.DISTANCE_REPORT;

  /**
   * 系統SERVICE
   */
  @Autowired
  LineRoutesService lineRoutesService;

  /**
   * キロ程表出力SERVICE
   */
  @Autowired
  DistanceReportService distanceReportService;

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model) {
    execCommonProc(model, NOW_MENU);
    model.addAttribute("linesList",
        lineRoutesService.getSelectableLines(getUserSession().getBusinessCorpId()));
  }

  /**
   * キロ程表出力画面（初期表示）
   *
   * @param model    Model
   * @param form     キロ程表出力FORM
   * @return キロ程表出力画面
   */
  @GetMapping("/")
  public String index(Model model,
      @ModelAttribute("distanceReportForm") DistanceReportForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);
    initModelAttribute(model, form);
    return INDEX_PAGE;
  }

  /**
   * EXCELダウンロード処理
   *
   * @param model         Model
   * @param form          キロ程表出力FORM
   * @param bindingResult BindingResult
   */
  @PostMapping("/downloadExcel")
  public Object downloadExcel(Model model,
      @Validated @ModelAttribute("distanceReportForm") DistanceReportForm form,
      BindingResult bindingResult) {

    // 権限チェック
    hasAccessPermission(NOW_MENU, false, true);
    execCommonProc(model, this.NOW_MENU);

    // 入力チェック
    if (bindingResult.hasErrors()) {
      setErrorMsg(CommonMessage.ERROR_INPUT);
      initModelAttribute(model, form);
      return INDEX_PAGE;
    }

    try {
      // ダウンロード
      form.setBusinessCorpId(getUserSession().getBusinessCorpId());
      return distanceReportService.downLoadExcel(form);
    } catch (Exception e) {
      LOGGER.error("【キロ程表Excelダウンロード】Excelダウンロード時にエラー", e);
      setExcelDownloadErrorMessage("キロ程表");
      initModelAttribute(model, form);
      return INDEX_PAGE;
    }
  }

  /**
   * 画面呼び出し前のModel初期設定
   *
   * @param model         Model
   * @param form          キロ程表出力FORM
   */
  private void initModelAttribute(Model model, DistanceReportForm form) {
    model.addAttribute("distanceReportForm", form);
    model.addAttribute("downloadFlag", hasDownloadPermission(NOW_MENU));
  }
}
