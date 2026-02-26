package maint.controller.document.stopMaps;

import maint.controller.BaseController;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.form.distanceReport.DistanceReportForm;
import maint.form.report.StopMapExcelForm;
import maint.message.CommonMessage;
import maint.service.operation.LinesService;
import maint.service.document.StopMapListService;
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
 * 停留所付近図出力CONTROLLER
 *
 * @author 林竜生
 */

@Controller
@Validated
@RequestMapping("/document/stopMap")
@SessionAttributes("stopMapExcelForm")
public class StopMapController extends BaseController {

  /** 停留所付近図出力画面PATH */
  private static final String INDEX_PAGE = "document/stopMap/index";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.STOP_MAP;

  /** 路線SERVICE */
  @Autowired
  LinesService linesService;

  /** 停留所付近図エクセルSERVICE */
  @Autowired
  private StopMapListService stopMapListRegisterService;

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model, @ModelAttribute("stopMapExcelForm") StopMapExcelForm form) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    model.addAttribute("linesList", linesService.getAllLinesForSelectOption(getUserSession().getBusinessCorpId()));
    model.addAttribute("downloadFlag", hasDownloadPermission(NOW_MENU));
    execCommonProc(model, NOW_MENU);
  }

  /**
   * 停留所付近図出力画面（初期表示）
   *
   * @param model Model
   * @param form 停留所付近図出力FORM
   * @param initFlag 停留所付近図出力フォーム初期化フラグ
   * @return 停留所付近図出力画面
   */
  @GetMapping("/")
  public String index(Model model, StopMapExcelForm form,
      @RequestParam(name = "init", required = false) Integer initFlag) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);
    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new StopMapExcelForm();
    }

    initModelAttribute(model, form);
    return INDEX_PAGE;
  }

  /**
   * EXCELダウンロード処理
   *
   * @param model Model
   * @param form 停留所付近図出力FORM
   * @param bindingResult BindingResult
   */
  @Transactional
  @PostMapping("/downloadExcel")
  public Object downloadExcel(Model model,
      @Validated @ModelAttribute("stopMapExcelForm") StopMapExcelForm form,
      BindingResult bindingResult) {
    execCommonProc(model, this.NOW_MENU);

    // 権限チェック
    hasAccessPermission(NOW_MENU, false, true);

    // 入力チェック
    if (bindingResult.hasErrors()) {
      LOGGER.warn(">>> Validation errors: {}", bindingResult.getAllErrors());
      setErrorMsg(CommonMessage.ERROR_INPUT);
      initModelAttribute(model, form);
      return INDEX_PAGE;
    }
    try {
      // ダウンロード
      LOGGER.info(">>> Calling stopMapListService.getStopMapInfo");
      initModelAttribute(model, form);
      return stopMapListRegisterService.getStopMapInfo(form);
    } catch (Exception e) {
      LOGGER.error("【停留所付近図Excelダウンロード】Excelダウンロード時にエラー", e);
      setExcelDownloadErrorMessage("停留所付近図");
      initModelAttribute(model, form);
      return INDEX_PAGE;
    }
  }

  /**
   * 画面呼び出し前のModel初期設定
   *
   * @param model         Model
   * @param form          停留所付近図出力FORM
   */
  private void initModelAttribute(Model model, StopMapExcelForm form) {
    model.addAttribute("stopMapExcelForm", form);
    model.addAttribute("downloadFlag", hasDownloadPermission(NOW_MENU));
  }
}
