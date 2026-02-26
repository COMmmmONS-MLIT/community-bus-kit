package maint.controller.planDaiya.chargeRevisions;

import static maint.utils.date.ExDateUtils.getCurrentDate;

import java.util.UUID;
import maint.controller.BaseController;
import maint.enums.Menu;
import maint.form.ChargeRevisions.ChargeRevisionsDetailForm;
import maint.message.CommonMessage;
import maint.message.ChargeRevisionsMessage;
import maint.service.planDaiya.ChargeRevisionService;
import maint.service.operation.LinesService;
import maint.utils.ExObjectUtils;
import maint.utils.date.ExDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 運賃改定詳細CONTROLLER
 *
 * @author g.ko
 */
@Controller
@RequestMapping("/planDaiya/chargeRevisions/detail")
@SessionAttributes("chargeRevisionsDetailForm")
public class ChargeRevisionsDetailController extends BaseController {

  /** 運賃改定詳細画面パス */
  private static final String DETAIL_PAGE = "planDaiya/chargeRevisions/detail";
  /** 運賃改定確認画面パス */
  private static final String CONFIRM_PAGE = "planDaiya/chargeRevisions/confirm";
  /** 運賃改定完了画面パス */
  private static final String COMPLETE_PAGE = "planDaiya/chargeRevisions/complete";
  /** 運賃改定完了画面URL */
  private static final String COMPLETE_URL = "planDaiya/chargeRevisions/detail/complete/";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.FARE_REVISION;

  /** 運賃改定SERVICE */
  @Autowired
  ChargeRevisionService chargeRevisionsService;

  /** 路線SERVICE */
  @Autowired
  LinesService linesService;

  /**
   * 詳細FORM設定
   *
   * @return 詳細FORM
   */
  @ModelAttribute("chargeRevisionsDetailForm")
  public ChargeRevisionsDetailForm setChargeRevisionsDetailForm() {
    return new ChargeRevisionsDetailForm();
  }

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    execCommonProc(model, NOW_MENU);
  }

  /**
   * 運賃改定詳細(新規登録)
   *
   * @param model Model
   * @param form 検索FORM
   * @return 詳細画面
   */
  @GetMapping("/")
  public String newBusStop(Model model,
      @ModelAttribute("ChargeRevisionsDetailForm") ChargeRevisionsDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);
    // 新規登録フォーム初期化
    form = new ChargeRevisionsDetailForm(form.getPage());
    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());
    // 共通処理
    commonProcess(model, form);

    return DETAIL_PAGE;
  }

  /**
   * 運賃改定詳細画面（戻る）
   *
   * @param model Model
   * @param form 詳細FORM
   * @return 詳細画面
   */
  @PostMapping("/back/")
  public String back(Model model,
      @ModelAttribute("chargeRevisionsDetailForm") ChargeRevisionsDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // セッション情報の整合性チェック
    form.isInvalidSession();

    // 共通処理
    commonProcess(model, form);

    return DETAIL_PAGE;
  }

  /**
   * 運賃改定確認画面
   *
   * @param model Model
   * @param form 詳細FORM
   * @param bindingResult BindingResult
   * @return 確認画面
   */
  @PostMapping("/confirm/")
  public String confirm(Model model,
      @Validated @ModelAttribute("chargeRevisionsDetailForm") ChargeRevisionsDetailForm form,
      BindingResult bindingResult) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // 既存データ設定
    form.setOriginFormData(
        (ChargeRevisionsDetailForm) model.asMap().get("chargeRevisionsDetailForm"));

    // セッション情報の整合性チェック
    form.isInvalidSession();
    // 共通Validationチェック
    commonValidationProcess(form, bindingResult);

    // 共通処理
    commonProcess(model, form);

    // Validationチェックでエラーがあった場合、詳細画面へ遷移
    if (!this.errorMsgList.isEmpty()) {
      return DETAIL_PAGE;
    }

    return CONFIRM_PAGE;
  }

  /**
   * 運賃改定登録、変更
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
      @Validated @ModelAttribute("chargeRevisionsDetailForm") ChargeRevisionsDetailForm form,
      BindingResult bindingResult, RedirectAttributes redirectAttributes,
      UriComponentsBuilder builder) {
    String attributeName = "chargeRevisionsDetailForm";
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // セッション情報の整合性チェック
    form.isInvalidSession();

    // 共通Validationチェック
    commonValidationProcess(form, bindingResult);

    // Validationチェックでエラーがあった場合、詳細画面へ遷移
    if (!this.errorMsgList.isEmpty()) {
      commonProcess(model, form);
      // エラー内容をModelに設定
      String className = bindingResult.getClass().getCanonicalName().replace("BeanProperty", "");
      model.addAttribute(className + "." + attributeName, bindingResult);
      return DETAIL_PAGE;
    }

    try {
      // 登録処理
      chargeRevisionsService.register(form);
    } catch (Exception e) {
      LOGGER.error("【運賃改定管理】登録失敗", e);
      this.setErrorMsg(CommonMessage.ERROR_REGISTER);
      // 共通処理
      commonProcess(model, form);
      return DETAIL_PAGE;
    }

    // 完了画面にリダイレクト
    return "redirect:" + builder.path(COMPLETE_URL).build().toUri();
  }

  /**
   * 運賃改定完了画面
   *
   * @param model Model
   * @param form 詳細FORM
   * @return 完了画面
   */
  @GetMapping("/complete/")
  public String complete(Model model,
      @ModelAttribute("chargeRevisionsDetailForm") ChargeRevisionsDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // セッション情報の整合性チェック
    form.isInvalidSession();

    this.setSuccessMsg(CommonMessage.SUCCESS_REGISTER);

    // セッションキーを破棄する
    form.setSessionKey("");

    return COMPLETE_PAGE;
  }

  /**
   * 共通Validation処理
   *
   * @param form 詳細FORM
   * @param bindingResult BindingResult
   */
  private void commonValidationProcess(ChargeRevisionsDetailForm form,
      BindingResult bindingResult) {

    // 運賃適用開始日チェック
    if (ExObjectUtils.isNotBlank(form.getServiceStartDate())) {
      // 本日日付以前の場合、エラー
      if (ExDateUtils.compareLocalDate(form.getServiceStartDate(), getCurrentDate()) <= 0) {
        bindingResult.addError(new FieldError(bindingResult.getObjectName(), "serviceStartDate",
            form.getServiceStartDate(), false, null, null,
            getMessage(ChargeRevisionsMessage.FARE_REVISION_INVALID_DATE.getMessage())));
      }

      // 路線の利用期間内ではない場合、エラー
      if (chargeRevisionsService.checkLinePeriod(form.getServiceStartDate(), form.getRosenId())) {
        bindingResult.addError(new FieldError(bindingResult.getObjectName(), "serviceStartDate",
            form.getServiceStartDate(), false, null, null,
            getMessage(ChargeRevisionsMessage.FARE_REVISION_INVALID_LINE_DATE.getMessage())));
      }

    }

    if (bindingResult.hasErrors()) {
      this.setErrorMsg(CommonMessage.ERROR_INPUT);
    }
  }

  /**
   * 共通処理
   *
   * @param model Model
   * @param form 詳細FORM
   */
  private void commonProcess(Model model, ChargeRevisionsDetailForm form) {
    // 運賃改定情報設定
    model.addAttribute("chargeRevisionsDetailForm", form);
    // 路線取得（SELECT用）
    model.addAttribute("lineList", linesService.getBusStopsForSelectOption());
  }
}
