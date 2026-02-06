package maint.controller.operation.holidayCalendar;

import java.util.List;
import java.util.Map;
import maint.controller.BaseController;
import maint.dto.operation.holidayCalendars.HolidayCalendarInfoDto;
import maint.enums.Menu;
import maint.message.CommonMessage;
import maint.service.operation.HolidayCalendarService;
import maint.utils.date.ExDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 祝日カレンダーCONTROLLER
 *
 * @author g.ko
 */
@Controller
@RequestMapping(value = "/operation/holidayCalendar/")
public class HolidayCalendarController extends BaseController {

  /** 祝日カレンダー画面パス */
  private static final String DETAIL_PAGE = "operation/holidayCalendars/detail";

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.HOLIDAY_CALENDAR;

  /** 祝日カレンダーSERVICE */
  @Autowired
  HolidayCalendarService holidayCalendarService;

  /**
   * 祝日カレンダー画面
   *
   * @param model Model
   * @return 祝日カレンダー画面
   */
  @GetMapping(value = "/")
  public String init(Model model) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, false, false);

    // 共通処理
    commonProcess(model);

    return DETAIL_PAGE;
  }

  /**
   * 祝日カレンダー登録
   *
   * @param model Model
   * @param file CSVファイル
   * @return 完了画面URL Redirect
   */
  @PostMapping("/register/")
  public String register(Model model, @RequestParam("file") MultipartFile file) {
    // 権限チェック
    hasAccessPermission(NOW_MENU, true, false);

    // ファイル入力チェック
    List<String> errors = holidayCalendarService.validateHolidayCsv(file);
    if (!errors.isEmpty()) {
      commonProcess(model);
      this.setErrorMsgList(errors);
      return DETAIL_PAGE;
    }

    try {
      // 登録処理
      holidayCalendarService.register(file);
    } catch (Exception e) {
      LOGGER.error("【祝日カレンダー管理】登録失敗", e);
      commonProcess(model);
      this.setErrorMsg(CommonMessage.ERROR_REGISTER);
      return DETAIL_PAGE;
    }

    commonProcess(model);
    this.setSuccessMsg(CommonMessage.SUCCESS_REGISTER);

    return DETAIL_PAGE;
  }

  /**
   * 共通処理
   *
   * @param model Model
   */
  public void commonProcess(Model model) {
    model.addAttribute("writeFlag", hasWritePermission(NOW_MENU));
    execCommonProc(model, NOW_MENU);
    setSystemCalendar(model);
  }

  /**
   * 祝日カレンダーを設定
   *
   * @param model Model
   */
  private void setSystemCalendar(Model model) {
    Map<String, List<HolidayCalendarInfoDto>> holidayCalendarInfoDtoMap =
        holidayCalendarService.getHolidayCalendar(ExDateUtils.getCurrentDate());
    model.addAttribute("lastMonth", holidayCalendarInfoDtoMap.get("lastMonth"));
    model.addAttribute("currentMonth", holidayCalendarInfoDtoMap.get("currentMonth"));
    model.addAttribute("nextMonth", holidayCalendarInfoDtoMap.get("nextMonth"));
    model.addAttribute("twoMonthsLater", holidayCalendarInfoDtoMap.get("twoMonthsLater"));
  }

}
