package maint.controller.operation.holidayCalendar;

import java.util.List;
import java.util.Map;
import maint.controller.BaseController;
import maint.dto.operation.holidayCalendars.HolidayCalendarInfoDto;
import maint.enums.Menu;
import maint.form.holidayCalendars.HolidayCalendarSearchForm;
import maint.message.CommonMessage;
import maint.service.operation.HolidayCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 祝日カレンダーAPI
 *
 * @author kohei.tateishi
 */
@RestController
@RequestMapping(value = "/api/operation/holidayCalendar")
public class HolidayCalendarApiController extends BaseController {

  /** 祝日カレンダーSERVICE */
  @Autowired
  HolidayCalendarService holidayCalendarService;

  /** メニュー情報 */
  private static final Menu NOW_MENU = Menu.HOLIDAY_CALENDAR;

  /**
   * 祝日カレンダー取得API
   *
   * @param form 祝日カレンダーフォーム
   * @param bindingResult BindingResult
   * @return 前月〜２ヶ月後の料金カレンダー情報
   */
  @PostMapping("/get-calendar")
  public ResponseEntity<?> getSystemCalendar(@RequestBody @Validated HolidayCalendarSearchForm form,
      BindingResult bindingResult) {
    // Validationチェック
    if (bindingResult.hasErrors()) {
      return new ResponseEntity<>(getMessage(CommonMessage.ERROR_INPUT.getMessage()),
          HttpStatus.BAD_REQUEST);
    }
    try {
      hasAccessPermission(NOW_MENU, false, false);
      return new ResponseEntity<Map<String, List<HolidayCalendarInfoDto>>>(
          holidayCalendarService.getHolidayCalendar(form.getTargetDate()), HttpStatus.OK);
    } catch (Exception e) {
      LOGGER.error("【祝日カレンダー取得API】エラー：" + e.getMessage());
      return new ResponseEntity<>(getMessage(CommonMessage.ERROR_GET_DATA.getMessage()),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
