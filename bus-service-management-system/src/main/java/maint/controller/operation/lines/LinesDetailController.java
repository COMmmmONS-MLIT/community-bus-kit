package maint.controller.operation.lines;

import maint.controller.BaseController;
import maint.dto.operation.lines.LineBusStopsDto;
import maint.enums.DispMode;
import maint.enums.Menu;
import maint.form.lines.LinesDetailForm;
import maint.message.CommonMessage;
import maint.service.baseInfo.BusStopsService;
import maint.service.operation.LinesService;
import maint.utils.ExObjectUtils;
import maint.utils.date.ExDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

/**
 * 路線管理詳細 Controller
 *
 * @author y.kim
 */

@Controller
@RequestMapping(value = "/operation/lines/detail")
@SessionAttributes("linesDetailForm")
public class LinesDetailController extends BaseController {

  /** 路線詳細画面PATH */
  private static final String DETAIL_PAGE = "operation/lines/detail";
  /** 路線確認画面PATH */
  private static final String CONFIRM_PAGE = "operation/lines/confirm";
  /** 路線完了画面PATH */
  private static final String COMPLETE_PAGE = "operation/lines/complete";
  /** 路線完了URL */
  private static final String COMPLETE_URL = "operation/lines/detail/complete/";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.LINES;

  /** 路線SERVICE */
  @Autowired
  LinesService linesService;

  /** 路線SERVICE */
  @Autowired
  BusStopsService busStopsService;

  /**
   * 路線詳細FORM設定
   *
   * @return 路線詳細FORM
   */
  @ModelAttribute("linesDetailForm")
  public LinesDetailForm setLinesDetailForm() {
    return new LinesDetailForm();
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
   * 路線詳細画面(新規登録)
   *
   * @param model Model
   * @param form  路線詳細FORM
   * @return 路線詳細画面
   */
  @GetMapping("/")
  public String newLine(Model model, @ModelAttribute("displayForm") LinesDetailForm form) {

    hasAccessPermission(NOW_MENU, true, false);

    form.setDispMode(DispMode.INSERT_MODE);
    form.setServiceStartDate(ExDateUtils.getCurrentDate());
    form.setServiceEndDate("9999-12-31");
    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());

    commonInitProcess(model, form);

    return DETAIL_PAGE;
  }

  /**
   * 路線詳細画面（変更）
   *
   * @param model Model
   * @param form  路線詳細FORM
   * @return 路線詳細画面
   */
  @PostMapping("/")
  public String loadLine(Model model, @ModelAttribute("displayForm") LinesDetailForm form) {

    hasAccessPermission(NOW_MENU, true, false);

    LinesDetailForm loadedForm = linesService.getLineByPkId(form.getRosenId());
    form.setAllPropertiesFrom(loadedForm);
    form.setDispMode(DispMode.UPDATE_MODE);

    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());

    isServiceStartDateChangeAllowed(model, form);

    commonInitProcess(model, form);

    return DETAIL_PAGE;
  }

  /**
   * 路線詳細画面(戻る)
   *
   * @param model Model
   * @param form  路線詳細FORM
   * @return 路線詳細画面
   */
  @PostMapping("/back/")
  public String back(Model model, @ModelAttribute("linesDetailForm") LinesDetailForm form) {

    hasAccessPermission(NOW_MENU, true, false);

    form.isInvalidSession();

    isServiceStartDateChangeAllowed(model, form);

    model.addAttribute("displayForm", form);

    return DETAIL_PAGE;
  }

  /**
   * 路線確認画面(参照路線)
   *
   * @param model Model
   * @param form  路線詳細FORM
   * @return 路線確認画面
   */
  @PostMapping("/content/")
  public String content(Model model, @ModelAttribute("displayForm") LinesDetailForm form) {

    hasAccessPermission(NOW_MENU, false, false);

    form = linesService.getLineByPkId(form.getRosenId());
    form.setDispMode(DispMode.CONTENT_MODE);

    commonInitProcess(model, form);

    return CONFIRM_PAGE;
  }

  /**
   * 路線確認画面
   *
   * @param model         Model
   * @param form          路線詳細FORM
   * @param bindingResult BindingResult
   * @return 路線確認画面
   */
  @PostMapping("/confirm/")
  public String confirm(Model model, @Validated @ModelAttribute("displayForm") LinesDetailForm form,
      BindingResult bindingResult) {

    // 未入力の不要な行削除
    if (form.getLineBusStopsList().size() != 1) {
      List<LineBusStopsDto> filteredList = form.getLineBusStopsList().stream()
          .filter(dto -> ExObjectUtils.isNotBlank(dto.getBusStopId()) && dto.getBusStopId() != 0)
          .toList();

      form.setLineBusStopsList(filteredList);
    }

    hasAccessPermission(NOW_MENU, true, false);

    LinesDetailForm orgForm = (LinesDetailForm) model.asMap().get("linesDetailForm");

    form.setOriginFormData(orgForm);
    // 期間Toが未入力の場合、9999/12/31で登録
    if (ExObjectUtils.isBlank(form.getServiceEndDate())) {
      form.setServiceEndDate("9999-12-31");
    }

    form.isInvalidSession();

    isServiceStartDateChangeAllowed(model, form);

    commonValidationProcess(form, bindingResult);

    if (!this.errorMsgList.isEmpty()) {
      return DETAIL_PAGE;
    }

    model.addAttribute("linesDetailForm", form);

    return CONFIRM_PAGE;
  }

  /**
   * 路線登録、変更
   *
   * @param model              Model
   * @param form               路線詳細FORM
   * @param bindingResult      BindingResult
   * @param redirectAttributes RedirectAttributes
   * @param builder            UriComponentsBuilder
   * @return 路線完了URL Redirect
   */
  @PostMapping("/register/")
  public String register(Model model,
      @Validated @ModelAttribute("linesDetailForm") LinesDetailForm form,
      BindingResult bindingResult, RedirectAttributes redirectAttributes,
      UriComponentsBuilder builder) {
    String attributeName = "displayForm";

    hasAccessPermission(NOW_MENU, true, false);

    LinesDetailForm orgForm = (LinesDetailForm) model.asMap().get("linesDetailForm");

    form.setOriginFormData(orgForm);

    form.isInvalidSession();

    commonValidationProcess(form, bindingResult);

    if (!this.errorMsgList.isEmpty()) {
      model.addAttribute(attributeName, form);
      // エラー内容をModelに設定
      String className = bindingResult.getClass().getCanonicalName().replace("BeanProperty", "");
      model.addAttribute(className + "." + attributeName, bindingResult);
      return DETAIL_PAGE;
    }

    try {
      // 利用終了日未設定の場合は 9999年12月31日を設定
      if (ExObjectUtils.isBlank(form.getServiceEndDate())) {
        form.setServiceEndDate("9999-12-31");
      }
      if (form.getDispMode().equals(DispMode.INSERT_MODE)) {
        linesService.insert(form);
      } else if (form.getDispMode().equals(DispMode.UPDATE_MODE)) {
        linesService.update(form);
      }
    } catch (Exception e) {
      model.addAttribute("errorMessage", form.getDispMode().equals(DispMode.INSERT_MODE) ?
          getMessage(CommonMessage.ERROR_REGISTER.getMessage()) :
          getMessage(CommonMessage.ERROR_UPDATE.getMessage()));
      form.setSessionKey("");
      return COMPLETE_PAGE;
    }

    model.addAttribute("linesDetailForm", form);

    // 完了画面にリダイレクト
    return "redirect:" + builder.path(COMPLETE_URL).build().toUri();
  }

  /**
   * 路線完了画面
   *
   * @param model Model
   * @param form  路線詳細FORM
   * @return 路線完了画面
   */
  @GetMapping("/complete/")
  public String complete(Model model, @ModelAttribute("linesDetailForm") LinesDetailForm form) {

    hasAccessPermission(NOW_MENU, true, false);

    form.isInvalidSession();

    this.setSuccessMsg(form.getDispMode().equals(DispMode.INSERT_MODE) ?
        CommonMessage.SUCCESS_REGISTER :
        CommonMessage.SUCCESS_UPDATE);

    form.setSessionKey("");

    return COMPLETE_PAGE;
  }

  /**
   * 共通初期化処理
   *
   * @param model Model
   * @param form  路線詳細FORM
   */
  private void commonInitProcess(Model model, LinesDetailForm form) {
    model.addAttribute("linesDetailForm", form);
  }

  /**
   * 共通Validation処理
   *
   * @param form          路線詳細FORM
   * @param bindingResult BindingResult
   */
  private void commonValidationProcess(LinesDetailForm form, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      this.setErrorMsg(CommonMessage.ERROR_INPUT);
    }

    // 停留所入力チェック
    if (ExObjectUtils.isEmpty(form.getLineBusStopsList()) || form.getLineBusStopsList().stream()
        .anyMatch(dto -> dto.getBusStopId() != null && dto.getBusStopId() == 0L)) {
      this.setErrorMsg("validation.lines.error.emptyBusStop");
    }

    if (DispMode.UPDATE_MODE.getValue()
        .equals(form.getDispMode().getValue()) && ExObjectUtils.isNotBlank(
        form.getServiceStartDate())) {
      // 系統利用期間チェック
      if (linesService.checkLineRouteByDateAndLine(form.getServiceStartDate(),
          ExObjectUtils.isBlank(form.getServiceEndDate()) ? "9999-12-31" : form.getServiceEndDate(),
          form.getRosenId())) {
        this.setErrorMsg("validation.lines.error.checkLineRoute");
      }
    }
  }

  /**
   * 利用期間開始の変更可否設定
   *
   * @param model Model
   * @param form  路線詳細FORM
   */
  private void isServiceStartDateChangeAllowed(Model model, LinesDetailForm form) {
    if (DispMode.UPDATE_MODE.equals(form.getDispMode()) && ExObjectUtils.isNotBlank(
        form.getServiceStartDate())) {
      String originServiceStartDate = linesService.getLineByPkId(form.getRosenId()).getServiceStartDate();
      boolean isStartDateNotPast = ExObjectUtils.isNotBlank(originServiceStartDate) && ExDateUtils.compareStringDate(ExDateUtils.getCurrentDate(), originServiceStartDate) <= 0;
      model.addAttribute("isServiceStartDateChangeAllowed",
          isStartDateNotPast && linesService.isServiceStartDateChangeAllowed(form.getRosenId()));
    }
  }
}
