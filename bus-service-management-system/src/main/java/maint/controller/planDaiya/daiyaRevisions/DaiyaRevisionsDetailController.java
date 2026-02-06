package maint.controller.planDaiya.daiyaRevisions;

import maint.controller.BaseController;
import maint.enums.DispMode;
import maint.enums.Menu;
import maint.form.daiyaRevisions.DaiyaRevisionsDetailForm;
import maint.message.CommonMessage;
import maint.message.DaiyaRevisionsErrorMessage;
import maint.service.planDaiya.DaiyaRevisionsService;
import maint.service.operation.LinesService;
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
 * 便ダイヤ改正詳細CONTROLLER
 *
 * @author jeon
 */
@Controller
@RequestMapping("/planDaiya/daiyaRevisions/detail")
@SessionAttributes("daiyaRevisionsDetailForm")
public class DaiyaRevisionsDetailController extends BaseController {

  /** 便ダイヤ改正詳細画面パス */
  private static final String DETAIL_PAGE = "planDaiya/daiyaRevisions/detail";
  /** 便ダイヤ改正確認画面パス */
  private static final String CONFIRM_PAGE = "planDaiya/daiyaRevisions/confirm";
  /** 便ダイヤ改正完了画面パス */
  private static final String COMPLETE_PAGE = "planDaiya/daiyaRevisions/complete";
  /** 便ダイヤ改正完了画面URL */
  private static final String COMPLETE_URL = "planDaiya/daiyaRevisions/detail/complete/";
  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.TIMETABLE_REVISION;

  /** 便ダイヤ改正SERVICE */
  @Autowired
  DaiyaRevisionsService daiyaRevisionsService;

  /** 路線SERVICE */
  @Autowired
  LinesService linesService;

  /**
   * 詳細FORM設定
   *
   * @return 便ダイヤ改正詳細FORM
   */
  @ModelAttribute("daiyaRevisionsDetailForm")
  public DaiyaRevisionsDetailForm setDaiyaRevisionsDetailForm() {
    return new DaiyaRevisionsDetailForm();
  }

  /**
   * Model初期化処理
   *
   * @param model Model
   */
  @ModelAttribute
  public void initModel(Model model) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    model.addAttribute("linesList", linesService.getAllLinesForSelectOption(getUserSession().getBusinessCorpId()));
    execCommonProc(model, this.NOW_MENU);
  }

  /**
   * 共通初期化処理
   *
   * @param model Model
   * @param form  便ダイヤ改正詳細FORM
   */
  private void commonInitProcess(Model model, DaiyaRevisionsDetailForm form) {
    model.addAttribute("daiyaRevisionsDetailForm", form);
  }

  /**
   * 便ダイヤ改正詳細(新規登録)
   *
   * @param model Model
   * @param form  便ダイヤ改正詳細FORM
   * @return 詳細画面
   */
  @GetMapping("/")
  public String initDaiyaRevisionDetail(Model model, @ModelAttribute("displayForm") DaiyaRevisionsDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    form.setDispMode(DispMode.INSERT_MODE);

    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());
    // 便ダイヤ改正情報設定
    model.addAttribute("daiyaRevisionsDetailForm", form);

    return DETAIL_PAGE;
  }

  /**
   * 便ダイヤ改正詳細画面（戻る）
   *
   * @param model Model
   * @param form  便ダイヤ改正詳細FORM
   * @return 詳細画面
   */
  @PostMapping("/back/")
  public String back(Model model,
      @ModelAttribute("daiyaRevisionsDetailForm") DaiyaRevisionsDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // セッション情報の整合性チェック
    form.isInvalidSession();
    model.addAttribute("displayForm", form);

    return DETAIL_PAGE;
  }

  /**
   * 便ダイヤ改正確認画面(参照権限)
   *
   * @param model Model
   * @param form  便ダイヤ改正詳細FORM
   * @return 確認画面
   */
  @PostMapping("/content/")
  public String content(Model model, @ModelAttribute("displayForm") DaiyaRevisionsDetailForm form) {

    hasAccessPermission(NOW_MENU, false, false);

    form.setInfo(daiyaRevisionsService.getDaiyaRevisionsByPk(form.getDaiyaRevisionId()),
        form.getPage(), DispMode.CONTENT_MODE);
    commonInitProcess(model, form);
    return CONFIRM_PAGE;
  }

  /**
   * 便ダイヤ改正確認画面
   *
   * @param model         Model
   * @param form          便ダイヤ改正詳細FORM
   * @param bindingResult BindingResult
   * @return 確認画面
   */
  @PostMapping("/confirm/")
  public String confirm(Model model,
      @Validated @ModelAttribute("displayForm") DaiyaRevisionsDetailForm form,
      BindingResult bindingResult) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // 既存データ設定
    form.setOriginFormData(
        (DaiyaRevisionsDetailForm) model.asMap().get("daiyaRevisionsDetailForm"));
    // セッション情報の整合性チェック
    form.isInvalidSession();
    // 共通Validationチェック
    commonValidationProcess(form, bindingResult);

    // Validationチェックでエラーがあった場合、詳細画面へ遷移
    if (!this.errorMsgList.isEmpty()) {
      return DETAIL_PAGE;
    }
    model.addAttribute("daiyaRevisionsDetailForm", form);
    return CONFIRM_PAGE;
  }

  /**
   * 便ダイヤ改正登録、変更
   *
   * @param model              Model
   * @param form               便ダイヤ改正詳細FORM
   * @param bindingResult      BindingResult
   * @param redirectAttributes RedirectAttributes
   * @param builder            UriComponentsBuilder
   * @return 完了画面URL Redirect
   */
  @PostMapping("/register/")
  public String register(Model model,
      @Validated @ModelAttribute("daiyaRevisionsDetailForm") DaiyaRevisionsDetailForm form,
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

    if (form.getDispMode().equals(DispMode.INSERT_MODE)) {
      try {
        daiyaRevisionsService.insert(form);
        setSuccessMsg(CommonMessage.SUCCESS_REGISTER);
      } catch (Exception e) {
        LOGGER.error("【便ダイヤ改正管理】登録失敗", e);
        commonInitProcess(model, form);
        setErrorMsg(CommonMessage.ERROR_REGISTER);
        return CONFIRM_PAGE;
      }
    }

    commonInitProcess(model, form);

    // 完了画面にリダイレクト
    return "redirect:" + builder.path(COMPLETE_URL).build().toUri();
  }

  /**
   * 便ダイヤ改正完了画面
   *
   * @param model Model
   * @param form  便ダイヤ改正詳細FORM
   * @return 便ダイヤ改正完了画面
   */
  @GetMapping("/complete/")
  public String complete(Model model,
      @ModelAttribute("daiyaRevisionsDetailForm") DaiyaRevisionsDetailForm form) {
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
   * @param form          便ダイヤ改正詳細FORM
   * @param bindingResult BindingResult
   */
  private void commonValidationProcess(DaiyaRevisionsDetailForm form, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      this.setErrorMsg(CommonMessage.ERROR_INPUT);
    }

    // 路線の利用期間内チェック
    if (!daiyaRevisionsService.countRosenAvailableOnServiceStartDate(form)) {
      this.setErrorMsg(DaiyaRevisionsErrorMessage.ERROR_ROUTE_PERIOD.getMessage());
    }
  }
}
