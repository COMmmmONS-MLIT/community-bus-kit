package maint.controller.operation.displayDaiyas;

import maint.controller.BaseController;
import maint.dto.operation.displayDaiyas.DisplayDaiyasDto;
import maint.dto.operation.displayDaiyas.DisplayTimelineResult;
import maint.enums.Menu;
import maint.form.displayDaiyas.DisplayDaiyasSearchForm;
import maint.message.CommonMessage;
import maint.service.operation.LineRoutesService;
import maint.service.operation.DisplayDaiyasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * 棒ダイヤ管理 controller
 *
 * @author y.kim
 */

@Controller
@Validated
@RequestMapping("/operation/displayDaiyas")
@SessionAttributes("displayDaiyasSearchForm")
public class DisplayDaiyasController extends BaseController {

  /** 一覧画面PATH */
  private static final String INDEX_PAGE = "operation/displayDaiyas/index";
  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.DIAGRAM_VIEW;

  /** 棒ダイヤSERVICE */
  @Autowired
  DisplayDaiyasService displayDaiyasService;

  /** 系統SERVICE */
  @Autowired
  LineRoutesService lineRoutesService;

  /**
   * 棒ダイヤ検索FORM設定
   *
   * @return 棒ダイヤ検索FORM
   */
  @ModelAttribute("displayDaiyasSearchForm")
  public DisplayDaiyasSearchForm setDisplayDaiyasSearchForm() {
    return new DisplayDaiyasSearchForm();
  }

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model) {
    execCommonProc(model, NOW_MENU);
  }

  /**
   * 棒ダイヤ一覧画面（初期表示）
   *
   * @param model Model
   * @return 一覧画面
   */
  @GetMapping("/")
  public String index(Model model) {

    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    commonProcess(model, new DisplayDaiyasSearchForm());
    return INDEX_PAGE;
  }

  /**
   * 棒ダイヤ管理一覧画面（検索）
   *
   * @param model         Model
   * @param form          棒ダイヤ検索FORM
   * @param bindingResult BindingResult
   * @return 棒ダイヤ管理一覧画面
   */
  @PostMapping("/")
  public String index(Model model, @Validated DisplayDaiyasSearchForm form,
      BindingResult bindingResult) {

    hasAccessPermission(NOW_MENU, false, false);

    if (bindingResult.hasErrors()) {
      setErrorMsg(CommonMessage.ERROR_INPUT);
      Long businessCorpId = getUserSession().getBusinessCorpId();
      // 路線一覧取得
      model.addAttribute("linesList", lineRoutesService.getSelectableLines(businessCorpId));
      return INDEX_PAGE;
    }
    form.setPage(1L);

    commonProcess(model, form);

    // ダイヤデータ取得
    List<DisplayDaiyasDto> data = displayDaiyasService.getDisplayDaiyas(form);

    // タイムライン情報取得
    DisplayTimelineResult timelineResult = displayDaiyasService.buildTimelineData(data);

    // モデルに追加
    model.addAttribute("grouped", timelineResult.getGrouped());
    model.addAttribute("colorsMap", timelineResult.getColorsMap());
    model.addAttribute("departureTimeLabelsMap", timelineResult.getDepartureTimeLabelsMap());
    model.addAttribute("arrivalTimeLabelsMap", timelineResult.getArrivalTimeLabelsMap());
    model.addAttribute("totalMinutes", timelineResult.getTotalMinutes());

    return INDEX_PAGE;
  }

  /**
   * 共通処理
   *
   * @param model  Model
   * @param form   棒ダイヤ検索FORM
   */
  private void commonProcess(Model model, DisplayDaiyasSearchForm form) {
    Long businessCorpId = getUserSession().getBusinessCorpId();

    // 路線一覧取得
    model.addAttribute("linesList", lineRoutesService.getSelectableLines(businessCorpId));

    model.addAttribute("displayDaiyasSearchForm", form);
  }
}
