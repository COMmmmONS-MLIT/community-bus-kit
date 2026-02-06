package maint.controller.operation.busDaiyas;

import maint.controller.BaseController;
import maint.dto.operation.busDaiyas.BusDaiyasRegistedDto;
import maint.enums.DispMode;
import maint.enums.Menu;
import maint.form.busDaiyas.BusDaiyasDetailForm;
import maint.message.CommonMessage;
import maint.service.operation.BusDaiyasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

/**
 * 便ダイヤ詳細CONTROLLER
 *
 * @author jeon
 */
@Controller
@RequestMapping("/operation/busDaiyas/detail")
@SessionAttributes("busDaiyasDetailForm")
public class BusDaiyasDetailController extends BaseController {

  /** 便ダイヤ詳細画面パス */
  private static final String DETAIL_PAGE = "operation/busDaiyas/detail";

  /** 便ダイヤ確認画面パス */
  private static final String CONFIRM_PAGE = "operation/busDaiyas/confirm";

  /** 便ダイヤ完了画面パス */
  private static final String COMPLETE_PAGE = "operation/busDaiyas/complete";
  /** 便ダイヤ完了画面URL */
  private static final String COMPLETE_URL = "operation/busDaiyas/detail/complete/";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.TIMETABLE;

  /** 便ダイヤSERVICE */
  @Autowired
  BusDaiyasService busDaiyasService;


  /**
   * 詳細FORM設定（初期化）
   *
   * @return 新規の便ダイヤ詳細FORM
   */
  @ModelAttribute("busDaiyasDetailForm")
  public BusDaiyasDetailForm setBusDaiyasDetailForm() {
    return new BusDaiyasDetailForm();
  }

  /**
   * Model初期化処理（権限・共通情報の設定）
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    model.addAttribute("downloadFlag", hasDownloadPermission(NOW_MENU));
    execCommonProc(model, NOW_MENU);
  }

  /**
   * 共通初期化処理
   *
   * @param model Model
   * @param form  便ダイヤ詳細FORM
   */
  private void commonInitProcess(Model model, BusDaiyasDetailForm form) {
    model.addAttribute("displayForm", form);
    model.addAttribute("busDaiyasDetailForm", form);
    model.addAttribute("busDaiyasOptionList", busDaiyasService.getBusDaiyasOptionList(form));

    // 系統プルダウンの初期値設定
    model.addAttribute("busDaiyasLineRouteOptionList",
        busDaiyasService.getBusDaiyasLineRouteOption(form));
    // 運行カレンダープルダウンの初期値設定
    model.addAttribute("busDaiyasCalendarOptionList",
        busDaiyasService.getBusDaiyasCalendarOption(form));
    // 車両プルダウンの初期値設定
    model.addAttribute("busDaiyasVehicleOptionList",
        busDaiyasService.getBusDaiyasVehicleOption(form));
    // 営業所プルダウンの初期値設定
    model.addAttribute("busDaiyasOfficeOptionList",
        busDaiyasService.getBusDaiyasOfficeOption(form));
    // 路線停留所の初期値設定
    model.addAttribute("busDaiyasLineBusStopList", busDaiyasService.getBusDaiyasRosenBusStop(form));
    // 系統プルダウン変更時に使用する値の設定
    model.addAttribute("busDaiyasRosenBusStopList",
        busDaiyasService.getBusDaiyasRosenBusStop(form));
    // 系統プルダウン変更時に使用する値の設定
    model.addAttribute("busDaiyasLineRouteBusStopList",
        busDaiyasService.getBusDaiyasLineRouteBusStop(form));
  }

  /**
   * 共通Validation処理
   *
   * @param form          系統詳細FORM
   * @param bindingResult BindingResult
   */
  private void commonValidationProcess(BusDaiyasDetailForm form,
      BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      this.setErrorMsg(CommonMessage.ERROR_INPUT);
    }

    this.errorMsgList = this.busDaiyasService.validateBusDaiyasDetail(form);
  }

  /**
   * 便ダイヤ詳細（変更）表示
   *
   * @param model Model
   * @param form  便ダイヤ詳細FORM
   * @return 詳細画面パス
   */
  @PostMapping("/")
  public String BusDaiyasDetail(Model model,
      @ModelAttribute("displayForm") BusDaiyasDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);
    // 前画面の選択行の往路、復路区分保存
    Integer lineDetailServiceDivision = form.getLineDetailServiceDivision();

    form.setInfo(busDaiyasService.getBusDaiyasByPk(form), form.getPage(), DispMode.UPDATE_MODE);

    // 前画面の選択行の往路、復路区分設定
    form.setLineDetailServiceDivision(lineDetailServiceDivision);
    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());

    // 表示区分の初期表示設定
    form.setDisplayDivisionFlag(0);
    model.addAttribute("detailOptionFlag", 0);

    // 便ダイヤ情報画面項目設定
    commonInitProcess(model, form);
    return DETAIL_PAGE;
  }

  /**
   * 便ダイヤ詳細画面（戻る）
   *
   * @param model Model
   * @param form  便ダイヤ詳細FORM（セッション復元）
   * @return 詳細画面パス
   */
  @PostMapping("/back/")
  public String back(Model model, @ModelAttribute("busDaiyasDetailForm") BusDaiyasDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // セッション情報の整合性チェック
    Integer lineDetailServiceDivision = form.getLineDetailServiceDivision();
    BusDaiyasDetailForm orgForm = (BusDaiyasDetailForm) model.asMap().get("busDaiyasDetailForm");
    form.setOriginFormData(orgForm);
    form.isInvalidSession();
    model.addAttribute("displayForm", form);
    // 変更モードで再表示
    form.setInfo(busDaiyasService.getBusDaiyasByPk(form), form.getPage(), DispMode.UPDATE_MODE);
    // 前画面の選択行の往路、復路区分設定
    form.setLineDetailServiceDivision(lineDetailServiceDivision);
    // 画面項目の設定
    model.addAttribute("busDaiyasRegistedList", form.getBusDaiyasRegistedList());
    commonInitProcess(model, form);

    return DETAIL_PAGE;
  }

  /**
   * 便ダイヤ管理変更画面(登録済み便ダイヤ情報の取得と表示)
   *
   * @param model Model
   * @param form  便ダイヤ管理詳細FORM
   * @return 変更画面
   */
  @PostMapping("/disp/")
  public String disp(Model model, @ModelAttribute("displayForm") BusDaiyasDetailForm form) {
    // 前画面の選択行の往路、復路区分保存
    Integer lineDetailServiceDivision = form.getLineDetailServiceDivision();
    BusDaiyasDetailForm orgForm = (BusDaiyasDetailForm) model.asMap().get("busDaiyasDetailForm");
    form.setOriginFormData(orgForm);
    // セッション情報の整合性チェック
    form.isInvalidSession();

    // 変更モードで再表示
    form.setInfo(busDaiyasService.getBusDaiyasByPk(form), form.getPage(), DispMode.UPDATE_MODE);
    // 前画面の選択行の往路、復路区分設定
    form.setLineDetailServiceDivision(lineDetailServiceDivision);
    commonInitProcess(model, form);

    model.addAttribute("busDaiyasRegistedList", null);
    // 表示する情報を取得し、modelに設定
    if (form.getDisplayDivisionFlag() == 1) {
      List<BusDaiyasRegistedDto> addList = busDaiyasService.getBusDaiyasRegistedList(form);
      if (!addList.isEmpty()) {
        model.addAttribute("busDaiyasRegistedList", busDaiyasService.getBusDaiyasRegistedList(form));
      }
    }

    return DETAIL_PAGE;
  }

  /**
   * 便ダイヤ管理確認画面
   *
   * @param model Model
   * @param form  便ダイヤ管理詳細FORM
   * @return 確認画面
   */
  @PostMapping("/confirm/")
  public String confirm(Model model,
      @Validated @ModelAttribute("displayForm") BusDaiyasDetailForm form,
      BindingResult bindingResult) {

    // 前画面の選択行の往路、復路区分保存
    Integer lineDetailServiceDivision = form.getLineDetailServiceDivision();
    BusDaiyasDetailForm orgForm = (BusDaiyasDetailForm) model.asMap().get("busDaiyasDetailForm");
    form.setOriginFormData(orgForm);
    // セッション情報の整合性チェック
    form.isInvalidSession();

    form.setInfo(busDaiyasService.getBusDaiyasByPk(form), form.getPage(), DispMode.CONTENT_MODE);
    form.setLineDetailServiceDivision(lineDetailServiceDivision);
    for (BusDaiyasRegistedDto dto : form.getBusDaiyasRegistedList()) {
      dto.setDaiyaRevisionId(form.getDaiyaRevisionId());
    }
    model.addAttribute("busDaiyasRegistedList", form.getBusDaiyasRegistedList());
    commonInitProcess(model, form);

    // 入力チェック
    commonValidationProcess(form, bindingResult);
    if (!this.errorMsgList.isEmpty()) {
      // 入力チェックエラーがあれば、変更画面へ
      model.addAttribute("errorMessages", this.errorMsgList);
      form.setDispMode(DispMode.UPDATE_MODE);
      return DETAIL_PAGE;
    }

    return CONFIRM_PAGE;
  }

  /**
   * 便データ登録、変更
   *
   * @param model Model
   * @param form 詳細FORM
   * @param bindingResult BindingResult
   * @param redirectAttributes RedirectAttributes
   * @param builder UriComponentsBuilder
   * @return 完了画面URL Redirect
   */
  @PostMapping("/register/")
  public String register(Model model,
      @Validated @ModelAttribute("busDaiyasDetailForm") BusDaiyasDetailForm form,
      BindingResult bindingResult, RedirectAttributes redirectAttributes,
      UriComponentsBuilder builder) {
    String attributeName = "displayForm";
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    BusDaiyasDetailForm orgForm = (BusDaiyasDetailForm) model.asMap().get("busDaiyasDetailForm");
    form.setOriginFormData(orgForm);
    // セッション情報の整合性チェック
    form.isInvalidSession();

    // Validationチェックでエラーがあった場合、詳細画面へ遷移
    if (!this.errorMsgList.isEmpty()) {
      commonInitProcess(model, form);
      // エラー内容をModelに設定
      String className = bindingResult.getClass().getCanonicalName().replace("BeanProperty", "");
      model.addAttribute(className + "." + attributeName, bindingResult);
      return DETAIL_PAGE;
    }

    try {
      // 登録処理
      busDaiyasService.register(form);
    } catch (Exception e) {
      LOGGER.error("【便ダイヤ管理】登録失敗", e);
      this.setErrorMsg(CommonMessage.ERROR_REGISTER);
      // 共通処理
      commonInitProcess(model, form);
      return DETAIL_PAGE;
    }

    // 完了画面にリダイレクト
    return "redirect:" + builder.path(COMPLETE_URL).build().toUri();
  }

  /**
   * 便ダイヤ完了画面
   *
   * @param model Model
   * @param form 詳細FORM
   * @return 完了画面
   */
  @GetMapping("/complete/")
  public String complete(Model model,
      @ModelAttribute("busDaiyasDetailForm") BusDaiyasDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // セッション情報の整合性チェック
    form.isInvalidSession();

    this.setSuccessMsg(CommonMessage.SUCCESS_REGISTER);

    // セッションキーを破棄する
    form.setSessionKey("");

    return COMPLETE_PAGE;
  }
}
