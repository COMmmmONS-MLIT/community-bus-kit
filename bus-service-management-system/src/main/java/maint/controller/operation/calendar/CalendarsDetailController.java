package maint.controller.operation.calendar;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import maint.controller.BaseController;
import maint.dto.operation.calendars.CalendarCancelDateDto;
import maint.dto.operation.calendars.CalendarDesignatedDateDto;
import maint.enums.DispMode;
import maint.enums.Menu;
import maint.enums.WeekDesignatableFlag;
import maint.form.calendars.CalendarsDetailForm;
import maint.message.CalendarsMessage;
import maint.message.CommonMessage;
import maint.service.operation.CalendarsService;
import maint.util.date.ExDateFormat;
import maint.utils.ExObjectUtils;
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
 * 運行カレンダー詳細CONTROLLER
 *
 * @author g.ko
 */
@Controller
@RequestMapping("/operation/calendars/detail")
@SessionAttributes("calendarsDetailForm")
public class CalendarsDetailController extends BaseController {

  /** 運行カレンダー詳細画面パス */
  private static final String DETAIL_PAGE = "operation/calendars/detail";
  /** 運行カレンダー確認画面パス */
  private static final String CONFIRM_PAGE = "operation/calendars/confirm";
  /** 運行カレンダー完了画面パス */
  private static final String COMPLETE_PAGE = "operation/calendars/complete";
  /** 運行カレンダー完了画面URL */
  private static final String COMPLETE_URL = "operation/calendars/detail/complete/";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.OP_CALENDAR;

  /** 運行カレンダーSERVICE */
  @Autowired
  CalendarsService calendarsService;

  /**
   * 詳細FORM設定
   *
   * @return 詳細FORM
   */
  @ModelAttribute("calendarsDetailForm")
  public CalendarsDetailForm setCalendarsDetailForm() {
    return new CalendarsDetailForm();
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
   * 運行カレンダー詳細(新規登録)
   *
   * @param model Model
   * @param form  検索FORM
   * @return 詳細画面
   */
  @GetMapping("/")
  public String newCalendar(Model model,
      @ModelAttribute("calendarsDetailForm") CalendarsDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);
    // 新規登録フォーム初期化
    form = new CalendarsDetailForm(form.getPage());
    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());
    // 共通処理
    commonProcess(model, form);

    return DETAIL_PAGE;
  }

  /**
   * 運行カレンダー詳細(変更)
   *
   * @param model Model
   * @param form  検索FORM
   * @return 詳細画面
   */
  @PostMapping("/")
  public String loadCalendar(Model model,
      @ModelAttribute("calendarsDetailForm") CalendarsDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // 運行カレンダー情報取得
    form.setInfo(calendarsService.getCalendarByPk(form.getCalendarId()), form.getPage(),
        DispMode.UPDATE_MODE);
    // 運行カレンダー除外日設定
    form.setCalendarCancelDateList(
        calendarsService.getCancelDateByCalendarId(form.getCalendarId()));
    // 運行カレンダー指定日設定
    form.setCalendarDesignatedDateList(
        calendarsService.getDesignatedDateByCalendarId(form.getCalendarId()));
    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());
    // 共通処理
    commonProcess(model, form);

    return DETAIL_PAGE;
  }

  /**
   * 運行カレンダー詳細画面（戻る）
   *
   * @param model Model
   * @param form  詳細FORM
   * @return 詳細画面
   */
  @PostMapping("/back/")
  public String back(Model model, @ModelAttribute("calendarsDetailForm") CalendarsDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // セッション情報の整合性チェック
    form.isInvalidSession();

    // 共通処理
    commonProcess(model, form);

    return DETAIL_PAGE;
  }

  /**
   * 運行カレンダー確認画面(参照権限)
   *
   * @param model Model
   * @param form  詳細FORM
   * @return 確認画面
   */
  @RequestMapping("/content/")
  public String content(Model model,
      @ModelAttribute("calendarsDetailForm") CalendarsDetailForm form) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // 運行カレンダー情報取得
    form.setInfo(calendarsService.getCalendarByPk(form.getCalendarId()), form.getPage(),
        DispMode.CONTENT_MODE);
    // 運行カレンダー除外日設定
    form.setCalendarCancelDateList(
        calendarsService.getCancelDateByCalendarId(form.getCalendarId()));
    // 運行カレンダー指定日設定
    form.setCalendarDesignatedDateList(
        calendarsService.getDesignatedDateByCalendarId(form.getCalendarId()));
    // 複数タブ用にUUID設定
    form.setSessionKey(UUID.randomUUID().toString());
    // 共通処理
    commonProcess(model, form);

    return CONFIRM_PAGE;
  }

  /**
   * 運行カレンダー確認画面
   *
   * @param model         Model
   * @param form          詳細FORM
   * @param bindingResult BindingResult
   * @return 確認画面
   */
  @PostMapping("/confirm/")
  public String confirm(Model model,
      @Validated @ModelAttribute("calendarsDetailForm") CalendarsDetailForm form,
      BindingResult bindingResult) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // 既存データ設定
    form.setOriginFormData((CalendarsDetailForm) model.asMap().get("calendarsDetailForm"));

    // 削除された行のデータを除外（運行カレンダー除外日）
    if (ExObjectUtils.isNotBlank(form.getCalendarCancelDateList())) {
      form.setCalendarCancelDateList(
          form.getCalendarCancelDateList().stream().filter(Objects::nonNull)
              .filter(CalendarCancelDateDto::isKeep).collect(Collectors.toList()));
    }

    // 削除された行のデータを除外（運行カレンダー指定日）
    if (ExObjectUtils.isNotBlank(form.getCalendarDesignatedDateList())) {
      form.setCalendarDesignatedDateList(
          form.getCalendarDesignatedDateList().stream().filter(Objects::nonNull)
              .filter(CalendarDesignatedDateDto::isKeep).collect(Collectors.toList()));
    }

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
   * 運行カレンダー登録、変更
   *
   * @param model              Model
   * @param form               詳細FORM
   * @param bindingResult      BindingResult
   * @param redirectAttributes RedirectAttributes
   * @param builder            UriComponentsBuilder
   * @return 完了画面URL Redirect
   */
  @PostMapping("/register/")
  public String register(Model model,
      @Validated @ModelAttribute("calendarsDetailForm") CalendarsDetailForm form,
      BindingResult bindingResult, RedirectAttributes redirectAttributes,
      UriComponentsBuilder builder) {
    String attributeName = "calendarsDetailForm";
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

    // 登録、変更処理
    if (form.getDispMode().equals(DispMode.INSERT_MODE)) {
      try {
        // 登録処理
        calendarsService.insert(form);
      } catch (Exception e) {
        LOGGER.error("【運行カレンダー管理】登録失敗", e);
        this.setErrorMsg(CommonMessage.ERROR_REGISTER);
        // 共通処理
        commonProcess(model, form);
        return DETAIL_PAGE;
      }
    } else if (form.getDispMode().equals(DispMode.UPDATE_MODE)) {
      try {
        // 更新処理
        calendarsService.update(form);
      } catch (Exception e) {
        LOGGER.error("【運行カレンダー管理】更新失敗", e);
        this.setErrorMsg(CommonMessage.ERROR_UPDATE);
        // 共通処理
        commonProcess(model, form);
        return DETAIL_PAGE;
      }
    }

    // 完了画面にリダイレクト
    return "redirect:" + builder.path(COMPLETE_URL).build().toUri();
  }

  /**
   * 運行カレンダー完了画面
   *
   * @param model Model
   * @param form  詳細FORM
   * @return 完了画面
   */
  @GetMapping("/complete/")
  public String complete(Model model,
      @ModelAttribute("calendarsDetailForm") CalendarsDetailForm form) {
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
   * @param form          詳細FORM
   * @param bindingResult BindingResult
   */
  private void commonValidationProcess(CalendarsDetailForm form, BindingResult bindingResult) {

    // 曜日指定可否区分が「指定可」かつ、運行曜日が選択されてない場合エラー
    if (form.getWeekDesignatableFlag().equals(
        WeekDesignatableFlag.DESIGNATION_ENABLED.getValue()) && !form.isSunday() && !form.isMonday()
        && !form.isTuesday() && !form.isWednesday() && !form.isThursday() && !form.isFriday() && !form.isSaturday() && !form.isHoliday()) {
      bindingResult.addError(new FieldError(bindingResult.getObjectName(), "sunday",
          getMessage(CommonMessage.ERROR_REQUIRED.getMessage())));
    }

    // 運行除外日チェック
    calendarsService.checkCancelDate(form, bindingResult);

    // 運行指定日チェック
    calendarsService.checkDesignatedDate(form, bindingResult);

    // エラーメッセージ出力
    if (bindingResult.hasErrors()) {
      this.setErrorMsg(CommonMessage.ERROR_INPUT);
    }

    // 曜日指定可否区分が「指定可」かつ、除外日と指定日から重複する日が孫座する場合、エラーメッセージを出力
    if (form.getWeekDesignatableFlag().equals(
        WeekDesignatableFlag.DESIGNATION_ENABLED.getValue()) && calendarsService.hasOverlapBetweenCancelAndDesignatedDates(
        form)) {
      this.setErrorMsg(CalendarsMessage.ERROR_OVERLAP_CANCEL_AND_DESIGNATED.getMessage());
    }

    // 曜日指定可否区分が「指定不可」の場合
    if (form.getWeekDesignatableFlag()
        .equals(WeekDesignatableFlag.DESIGNATION_DISABLED.getValue())) {

      // 運行指定日取得
      List<String> designatedDates = form.getCalendarDesignatedDateList().stream().filter(Objects::nonNull)
          .filter(c -> !c.isDelete()).map(CalendarDesignatedDateDto::getOperationDesignatedDate)
          .filter(Objects::nonNull).map(String::trim).filter(s -> !s.isEmpty()).toList();

      // 運行指定日取得の中に未来日の日付が1つでもあるかどうか
      String today = LocalDate.now().format(ExDateFormat.yyMMdd);
      boolean hasFutureDesignatedDate =
          designatedDates.stream().anyMatch(date -> date.compareTo(today) > 0);

      // 運行指定日がない場合、エラーメッセージを出力
      if (ExObjectUtils.isBlank(designatedDates)) {
        this.setErrorMsg(CalendarsMessage.ERROR_REQUIRED_OPERATION_DESIGNATED_DATE.getMessage());
      } else if (!hasFutureDesignatedDate) {
        // 運行指定日に未来日がない場合、エラーメッセージを出力
        this.setErrorMsg(CalendarsMessage.ERROR_REQUIRED_FUTURE_OPERATION_DESIGNATED_DATE.getMessage());
      }
    }
  }

  /**
   * 共通処理
   *
   * @param model Model
   * @param form  詳細FORM
   */
  private void commonProcess(Model model, CalendarsDetailForm form) {
    // 運行カレンダー情報設定
    model.addAttribute("calendarsDetailForm", form);
  }
}
