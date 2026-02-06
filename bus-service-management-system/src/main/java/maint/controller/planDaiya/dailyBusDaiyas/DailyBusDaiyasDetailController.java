package maint.controller.planDaiya.dailyBusDaiyas;

import maint.controller.BaseController;
import maint.enums.DispMode;
import maint.enums.Menu;
import maint.form.dailyBusDaiyas.DailyBusDaiyasDetailForm;
import maint.message.CommonMessage;
import maint.message.DailyBusDaiyasMessage;
import maint.service.planDaiya.VehiclesService;
import maint.service.planDaiya.DailyBusDaiyasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

/**
 * 日毎便ダイヤ詳細CONTROLLER
 *
 * @author jeon
 */
@Controller
@RequestMapping("/planDaiya/dailyBusDaiyas/detail")
@SessionAttributes("dailyBusDaiyasDetailForm")
public class DailyBusDaiyasDetailController extends BaseController {

  /** 日毎便ダイヤ詳細画面パス */
  private static final String DETAIL_PAGE = "planDaiya/dailyBusDaiyas/detail";
  /** 日毎便ダイヤ確認画面パス */
  private static final String CONFIRM_PAGE = "planDaiya/dailyBusDaiyas/confirm";
  /** 日毎便ダイヤ完了画面パス */
  private static final String COMPLETE_PAGE = "planDaiya/dailyBusDaiyas/complete";
  /** 日毎便ダイヤ完了画面URL */
  private static final String COMPLETE_URL = "planDaiya/dailyBusDaiyas/detail/complete/";
  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.DAILY_TIMETABLE;

  /** 日毎便ダイヤSERVICE */
  @Autowired
  DailyBusDaiyasService dailyBusDaiyasService;

  /** 車両SERVICE */
  @Autowired
  VehiclesService vehiclesService;

  /**
   * 詳細FORM設定
   *
   * @return 日毎便ダイヤ詳細FORM
   */
  @ModelAttribute("dailyBusDaiyasDetailForm")
  public DailyBusDaiyasDetailForm setDailyBusDaiyasDetailForm() {
    return new DailyBusDaiyasDetailForm();
  }

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    execCommonProc(model, this.NOW_MENU);
  }

  /**
   * 共通初期化処理
   *
   * @param model Model
   * @param form  日毎便ダイヤ詳細FORM
   */
  private void commonInitProcess(Model model, DailyBusDaiyasDetailForm form) {
    model.addAttribute("dailyBusDaiyasDetailForm", form);
    model.addAttribute("lineRouteBusStopsList", dailyBusDaiyasService.getlineRouteBusStopsList(form));
    model.addAttribute("dailyDaiyaOperationHistoriesList", dailyBusDaiyasService.getDailyDaiyaOperationHistories(form));
    model.addAttribute("vehicleList", vehiclesService.getAllLinesForSelectOption(getUserSession().getBusinessCorpId()));
  }

  /**
   * 日毎便ダイヤ詳細(変更)
   *
   * @param model Model
   * @param form  日毎便ダイヤ詳細FORM
   * @return 詳細画面
   */
  @PostMapping("/")
  public String initDailyBusDaiyasDetail(Model model, @ModelAttribute("displayForm") DailyBusDaiyasDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    form.setInfo(dailyBusDaiyasService.getDailyBusDaiyasByPk(form), form.getPage(), DispMode.UPDATE_MODE);

    // 複数タブ用にUUID設定
     form.setSessionKey(UUID.randomUUID().toString());

     commonInitProcess(model, form);

    return DETAIL_PAGE;
  }

  /**
   * 日毎便ダイヤ詳細画面（戻る）
   *
   * @param model Model
   * @param form  日毎便ダイヤ詳細FORM
   * @return 詳細画面
   */
  @PostMapping("/back/")
  public String back(Model model,
      @ModelAttribute("dailyBusDaiyasDetailForm") DailyBusDaiyasDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // セッション情報の整合性チェック
    form.isInvalidSession();
    model.addAttribute("displayForm", form);
    model.addAttribute("lineRouteBusStopsList", dailyBusDaiyasService.getlineRouteBusStopsList(form));
    model.addAttribute("dailyDaiyaOperationHistoriesList", dailyBusDaiyasService.getDailyDaiyaOperationHistories(form));
    model.addAttribute("vehicleList", vehiclesService.getAllLinesForSelectOption(getUserSession().getBusinessCorpId()));

    return DETAIL_PAGE;
  }

  /**
   * 日毎便ダイヤ確認画面(参照権限)
   *
   * @param model Model
   * @param form  日毎便ダイヤ詳細FORM
   * @return 確認画面
   */
  @PostMapping("/content/")
  public String content(Model model, @ModelAttribute("displayForm") DailyBusDaiyasDetailForm form) {

    form.setInfo(dailyBusDaiyasService.getDailyBusDaiyasByPk(form),form.getPage(), DispMode.CONTENT_MODE);
    commonInitProcess(model, form);

    return CONFIRM_PAGE;
  }

  /**
   * 日毎便ダイヤ確認画面
   *
   * @param model         Model
   * @param form          日毎便ダイヤ詳細FORM
   * @param bindingResult BindingResult
   * @return 確認画面
   */
  @PostMapping("/confirm/")
  public String confirm(Model model,
      @Validated @ModelAttribute("displayForm") DailyBusDaiyasDetailForm form,
      BindingResult bindingResult) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // 既存データ設定
    form.setOriginFormData((DailyBusDaiyasDetailForm) model.asMap().get("dailyBusDaiyasDetailForm"));
    // セッション情報の整合性チェック
    form.isInvalidSession();
    // 共通Validationチェック
    commonValidationProcess(form, bindingResult);

    // Validationチェックでエラーがあった場合、詳細画面へ遷移
    if (!this.errorMsgList.isEmpty()) {
      return DETAIL_PAGE;
    }

    commonInitProcess(model, form);

    return CONFIRM_PAGE;
  }


  /**
   * 日毎便ダイヤ登録、変更
   *
   * @param model              Model
   * @param form               日毎便ダイヤ詳細FORM
   * @param bindingResult      BindingResult
   * @param redirectAttributes RedirectAttributes
   * @param builder            UriComponentsBuilder
   * @return 完了画面URL Redirect
   */
  @PostMapping("/register/")
  public String register(Model model,
      @Validated @ModelAttribute("dailyBusDaiyasDetailForm") DailyBusDaiyasDetailForm form,
      BindingResult bindingResult, RedirectAttributes redirectAttributes,
      UriComponentsBuilder builder) {
    String attributeName = "displayForm";

    hasAccessPermission(NOW_MENU, true, false);
    // セッション情報の整合性チェック
    form.isInvalidSession();

    commonValidationProcess(form, bindingResult);

    if (!this.errorMsgList.isEmpty()) {
      model.addAttribute(attributeName, form);
      // エラー内容をModelに設定
      String className = bindingResult.getClass().getCanonicalName().replace("BeanProperty", "");
      model.addAttribute(className + "." + attributeName, bindingResult);
      return DETAIL_PAGE;
    }

    if (form.getDispMode().equals(DispMode.UPDATE_MODE)) {
      try {
        dailyBusDaiyasService.update(form);
        setSuccessMsg(CommonMessage.SUCCESS_UPDATE);
      } catch (Exception e) {
        LOGGER.error("【日毎便ダイヤ管理】更新失敗", e);
        commonInitProcess(model, form);
        setErrorMsg(CommonMessage.ERROR_UPDATE);
        return CONFIRM_PAGE;
      }
    }

    commonInitProcess(model, form);

    // 完了画面にリダイレクト
    return "redirect:" + builder.path(COMPLETE_URL).build().toUri();
  }

  /**
   * 日毎便ダイヤ完了画面
   *
   * @param model Model
   * @param form  日毎便ダイヤ詳細FORM
   * @return 日毎便ダイヤ完了画面
   */
  @GetMapping("/complete/")
  public String complete(Model model,
      @ModelAttribute("dailyBusDaiyasDetailForm") DailyBusDaiyasDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // セッション情報の整合性チェック
    form.isInvalidSession();

    this.setSuccessMsg(form.getDispMode().equals(DispMode.INSERT_MODE) ?
        CommonMessage.SUCCESS_REGISTER :
        CommonMessage.SUCCESS_UPDATE);

    // セッションキーを破棄する
    form.setSessionKey("");

    return COMPLETE_PAGE;
  }

  /**
   * 共通Validation処理
   *
   * @param form          日毎便ダイヤ詳細FORM
   * @param bindingResult BindingResult
   */
  private void commonValidationProcess(DailyBusDaiyasDetailForm form, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      this.setErrorMsg(CommonMessage.ERROR_INPUT);
    }

    if(!dailyBusDaiyasService.existsDailyBusDaiyasByPk(form)){
      this.setErrorMsg(DailyBusDaiyasMessage.ERROR_NOT_SELECTED_DAILYBUSDAIYAS.getMessage());
    }
  }

}
