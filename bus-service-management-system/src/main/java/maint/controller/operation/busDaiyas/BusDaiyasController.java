package maint.controller.operation.busDaiyas;

import jakarta.validation.constraints.DecimalMin;
import maint.controller.BaseController;
import maint.dto.ListPager;
import maint.enums.FlagValueEnums;
import maint.enums.Menu;
import maint.enums.LineDetailServiceDivision;
import maint.form.busDaiyas.BusDaiyasSearchForm;
import maint.message.CommonMessage;
import maint.service.operation.BusDaiyasService;
import maint.service.operation.LinesService;
import maint.utils.ExObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 便ダイヤ管理CONTROLLER
 *
 * @author jeon
 */
@Controller
@Validated
@RequestMapping("/operation/busDaiyas")
@SessionAttributes("busDaiyasSearchForm")
public class BusDaiyasController extends BaseController {

  /** 一覧画面PATH */
  private static final String INDEX_PAGE = "operation/busDaiyas/index";
  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.TIMETABLE;

  /** 便ダイヤSERVICE */
  @Autowired
  BusDaiyasService busDaiyasService;

  /** 路線SERVICE */
  @Autowired
  LinesService linesService;

  /**
   * 便ダイヤ検索FORM設定（初期化）
   *
   * @return 新規の便ダイヤ検索FORM
   */
  @ModelAttribute("busDaiyasSearchForm")
  public BusDaiyasSearchForm setPermissionSearchForm() {
    return new BusDaiyasSearchForm();
  }

  /**
   * Model初期化処理（権限・共通情報の設定）
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    execCommonProc(model, NOW_MENU);
  }

  /**
   * 便ダイヤ管理一覧 初期表示（検索条件の初期化対応）
   *
   * @param model Model
   * @param form  便ダイヤ検索FORM
   * @param initFlag 初期化フラグ（1: 初期化、null/0: 維持）
   * @return 一覧画面パス
   */
  @GetMapping("/")
  public String index(Model model, BusDaiyasSearchForm form, @RequestParam(name = "init", required = false) Integer initFlag) {

    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // フォーム初期化処理
    if (ExObjectUtils.isNotBlank(initFlag) && FlagValueEnums.TRUE.getValue().equals(initFlag)) {
      form = new BusDaiyasSearchForm();
      form.setSearchLineDetailServiceDivisionFlg(LineDetailServiceDivision.OUTBOUND_TRIP.getValue());
    }

    commonProcess(model, form, createListPager(model));
    return INDEX_PAGE;
  }

  /**
   * 便ダイヤ管理一覧 検索実行
   *
   * @param model Model
   * @param form  便ダイヤ検索FORM
   * @param bindingResult 入力検証結果
   * @return 一覧画面パス（エラー時はエラーメッセージ付きで返却）
   */
  @PostMapping("/")
  public String index(Model model, @Validated BusDaiyasSearchForm form, BindingResult bindingResult) {

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
   * 便ダイヤ管理一覧 ページング
   *
   * @param model Model
   * @param page  ページ番号
   * @param form  便ダイヤ検索FORM
   * @return 一覧画面パス
   */
  @GetMapping("/{page}/")
  public String paging(Model model, @DecimalMin("1") @PathVariable("page") Long page, @ModelAttribute("busDaiyasSearchForm") BusDaiyasSearchForm form) {

    hasAccessPermission(NOW_MENU, false, false);

    commonProcess(model, form, createListPager(model, page));

    return INDEX_PAGE;
  }

  /**
   * 共通処理：一覧取得とモデル設定
   *
   * @param model Model
   * @param form  便ダイヤ検索FORM
   * @param pager ページャ
   */
  private void commonProcess(Model model, BusDaiyasSearchForm form, ListPager pager) {
    // 便ダイヤ管理一覧検索
    busDaiyasService.setBusDaiyasList(form, pager);
    model.addAttribute("linesList", linesService.getAllLinesForSelectOption(getUserSession().getBusinessCorpId()));
    model.addAttribute("busDaiyasSearchForm", form);
    model.addAttribute("pager", pager);
  }
}
