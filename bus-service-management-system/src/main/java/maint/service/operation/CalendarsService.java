package maint.service.operation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import maint.dto.ListPager;
import maint.dto.operation.calendars.*;
import maint.dto.operation.holidayCalendars.BusServiceInfoDto;
import maint.entity.CalendarCancelDatesEntity;
import maint.entity.CalendarDesignatedDatesEntity;
import maint.entity.CalendarDetailsEntity;
import maint.entity.CalendarsEntity;
import maint.enums.DayWeekType;
import maint.enums.WeekDesignatableFlag;
import maint.exception.CommonDbException;
import maint.form.calendars.CalendarsDetailForm;
import maint.form.calendars.CalendarsSearchForm;
import maint.mapper.*;
import maint.message.CalendarsMessage;
import maint.service.BaseService;
import maint.service.planDaiya.DailyBusDaiyasService;
import maint.utils.ExObjectUtils;
import maint.utils.date.ExDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * 運行カレンダーSERVICE
 *
 * @author g.ko
 */
@Service
public class CalendarsService extends BaseService {

  /** 運行カレンダーMAPPER */
  @Autowired
  CalendarsMapper calendarsMapper;

  /** 運行カレンダー詳細MAPPER */
  @Autowired
  CalendarDetailsMapper calendarDetailsMapper;

  /** 運行カレンダー除外日設定MAPPER */
  @Autowired
  CalendarCancelDatesMapper calendarCancelDatesMapper;

  /** 運行カレンダー指定日設定MAPPER */
  @Autowired
  CalendarDesignatedDatesMapper calendarDesignatedDatesMapper;

  /** 便MAPPER */
  @Autowired
  BusServicesMapper busServicesMapper;

  /** 日毎便ダイヤSERVICE */
  @Autowired
  DailyBusDaiyasService dailyBusDaiyasService;

  /**
   * 運行カレンダー一覧取得
   *
   * @param form 検索FORM
   * @param pager ListPager
   */
  public void getCalendars(CalendarsSearchForm form, ListPager pager) {
    SearchCalendarsDto dto = new SearchCalendarsDto(form, pager.getStartIndex(), getUserSession());

    // 件数取得
    pager.setTotalListSize(calendarsMapper.countCalendarsBySearch(dto));
    if (pager.getTotalListSize().compareTo(0L) <= 0) {
      pager.setList(java.util.Collections.emptyList());
      return;
    }

    // 一覧取得
    List<CalendarDto> calendarList = calendarsMapper.getCalendarsBySearch(dto);
    List<CalendarDto> calendarResultList = new java.util.ArrayList<>(calendarList.size());

    // 明細情報を反映
    for (CalendarDto calendar : calendarList) {
      applyDayWeekFlags(calendar);
      calendarResultList.add(calendar);
    }

    pager.setList(calendarResultList);
  }

  /**
   * 運行カレンダー情報取得
   *
   * @param id PK
   * @return 運行カレンダー情報
   */
  public CalendarDto getCalendarByPk(Long id) {
    // 運行カレンダー情報取得
    CalendarDto calendarDto = calendarsMapper.getCalendarByPk(id);

    // 明細情報を反映
    applyDayWeekFlags(calendarDto);

    return calendarDto;
  }

  /**
   * 運行除外日チェック
   *
   * @param form 運行カレンダー詳細FORM
   * @param bindingResult BindingResult
   */
  public void checkCancelDate(CalendarsDetailForm form, BindingResult bindingResult) {
    // 曜日指定可否区分が「指定不可」の場合、チェック対象外とする
    if (form.getWeekDesignatableFlag()
        .equals(WeekDesignatableFlag.DESIGNATION_DISABLED.getValue())) {
      return;
    }

    if (ExObjectUtils.isNotBlank(form.getCalendarCancelDateList())) {

      String today = ExDateUtils.getCurrentDate();

      for (int i = 0; i < form.getCalendarCancelDateList().size(); i++) {
        CalendarCancelDateDto row = form.getCalendarCancelDateList().get(i);

        if (ExObjectUtils.isBlank(row)) {
          continue;
        }

        // 既存のデータ、または削除にチェック入れ、または日付未入力の場合、チェック対処外とする
        if (ExObjectUtils.isNotBlank(row.getCalendarCancelDateId()) || row.isDelete()
            || ExObjectUtils.isBlank(row.getOperationCancelDate())) {
          continue;
        }

        String fieldPath = "calendarCancelDateList[" + i + "].operationCancelDate";
        String value = row.getOperationCancelDate();

        // 形式が「yymmdd」ではない場合、エラー
        if (!value.matches("\\d{6}")) {
          bindingResult.addError(
              new FieldError(bindingResult.getObjectName(), fieldPath, value, false, null, null,
                  getMessage(CalendarsMessage.ERROR_DATE_FORMAT.getMessage())));
          continue;
        }

        // 不正な日付の場合、エラー
        if (ExObjectUtils.isBlank(toIsoDateFromYyMMdd(value))) {
          bindingResult.addError(
              new FieldError(bindingResult.getObjectName(), fieldPath, value, false, null, null,
                  getMessage(CalendarsMessage.ERROR_DATE_FORMAT.getMessage())));
          continue;
        }

        // 過去日の日付の場合、エラー
        if (ExDateUtils.compareLocalDate(toIsoDateFromYyMMdd(value), today) < 0) {
          bindingResult.addError(
              new FieldError(bindingResult.getObjectName(), fieldPath, value, false, null, null,
                  getMessage(CalendarsMessage.ERROR_PAST_DATE.getMessage())));
        }

      }
    }
  }

  /**
   * 運行指定日チェック
   *
   * @param form 運行カレンダー詳細FORM
   * @param bindingResult BindingResult
   */
  public void checkDesignatedDate(CalendarsDetailForm form, BindingResult bindingResult) {
    if (ExObjectUtils.isNotBlank(form.getCalendarDesignatedDateList())) {

      String today = ExDateUtils.getCurrentDate();

      for (int i = 0; i < form.getCalendarDesignatedDateList().size(); i++) {
        CalendarDesignatedDateDto row = form.getCalendarDesignatedDateList().get(i);
        if (ExObjectUtils.isBlank(row)) {
          continue;
        }

        // 既存のデータ、または削除にチェック入れ、または日付未入力の場合、チェック対処外とする
        if (ExObjectUtils.isNotBlank(row.getCalendarDesignatedDateId()) || row.isDelete()
            || ExObjectUtils.isBlank(row.getOperationDesignatedDate())) {
          continue;
        }

        String fieldPath = "calendarDesignatedDateList[" + i + "].operationDesignatedDate";
        String value = row.getOperationDesignatedDate();

        // 形式が「yymmdd」ではない場合、エラー
        if (!value.matches("\\d{6}")) {
          bindingResult.addError(
              new FieldError(bindingResult.getObjectName(), fieldPath, value, false, null, null,
                  getMessage(CalendarsMessage.ERROR_DATE_FORMAT.getMessage())));
          continue;
        }

        // 不正な日付の場合、エラー
        if (ExObjectUtils.isBlank(toIsoDateFromYyMMdd(value))) {
          bindingResult.addError(
              new FieldError(bindingResult.getObjectName(), fieldPath, value, false, null, null,
                  getMessage(CalendarsMessage.ERROR_DATE_FORMAT.getMessage())));
          continue;
        }

        // 過去日の日付の場合、エラー
        if (ExDateUtils.compareLocalDate(toIsoDateFromYyMMdd(value), today) < 0) {
          bindingResult.addError(
              new FieldError(bindingResult.getObjectName(), fieldPath, value, false, null, null,
                  getMessage(CalendarsMessage.ERROR_PAST_DATE.getMessage())));
        }

      }
    }
  }

  /**
   * 除外日と指定日重複チェック
   *
   * @param form 運行カレンダー詳細FORM
   * @return チェック結果（true:重複あり、false:重複なし）
   */
  public boolean hasOverlapBetweenCancelAndDesignatedDates(CalendarsDetailForm form) {
    // 運行除外日取得
    List<String> cancelDates = form.getCalendarCancelDateList().stream().filter(Objects::nonNull)
        .filter(c -> !c.isDelete()).map(CalendarCancelDateDto::getOperationCancelDate)
        .filter(Objects::nonNull).map(String::trim).filter(s -> !s.isEmpty()).toList();
    // 運行指定日取得
    List<String> designatedDates = form.getCalendarDesignatedDateList().stream().filter(Objects::nonNull)
        .filter(c -> !c.isDelete()).map(CalendarDesignatedDateDto::getOperationDesignatedDate)
        .filter(Objects::nonNull).map(String::trim).filter(s -> !s.isEmpty()).toList();
    return !Collections.disjoint(cancelDates, designatedDates);
  }

  /**
   * 運行カレンダー登録
   *
   * @param form 詳細FORM
   */
  @Transactional
  public void insert(CalendarsDetailForm form) {
    // 運行カレンダー登録
    CalendarsEntity calendarsEntity = new CalendarsEntity(form, getUserSession());
    if (calendarsMapper.registerCalendar(calendarsEntity) != 1) {
      throw new CommonDbException("【運行カレンダー管理】運行カレンダー登録エラー");
    }

    // 運行カレンダー明細登録
    this.insertCalendarDetails(calendarsEntity.getCalendarId(), form);

    // 運行カレンダー除外日設定
    if (!form.getCalendarCancelDateList().isEmpty() && form.getWeekDesignatableFlag()
        .equals(WeekDesignatableFlag.DESIGNATION_ENABLED.getValue())) {
      for (CalendarCancelDateDto dto : form.getCalendarCancelDateList()) {
        // 有効なデータを登録（データ入力がない、または削除にチェックされているデータは登録しない）
        if (!dto.isDelete() && ExObjectUtils.isNotBlank(dto.getOperationCancelDate())) {
          CalendarCancelDatesEntity entity =
              new CalendarCancelDatesEntity(calendarsEntity.getCalendarId(),
                  dto.getOperationCancelDate());
          if (calendarCancelDatesMapper.registerCalendarCancelDate(entity) != 1) {
            throw new CommonDbException("【運行カレンダー管理】運行カレンダー除外日登録エラー");
          }
        }
      }
    }

    // 運行カレンダー指定日設定
    if (!form.  getCalendarDesignatedDateList().isEmpty()) {
      for (CalendarDesignatedDateDto dto : form.getCalendarDesignatedDateList()) {
        // 有効なデータを登録（データ入力がない、または削除にチェックされているデータは登録しない）
        if (!dto.isDelete() && ExObjectUtils.isNotBlank(dto.getOperationDesignatedDate())) {
          CalendarDesignatedDatesEntity entity =
              new CalendarDesignatedDatesEntity(calendarsEntity.getCalendarId(),
                  dto.getOperationDesignatedDate());
          if (calendarDesignatedDatesMapper.registerCalendarDesignatedDate(entity) != 1) {
            throw new CommonDbException("【運行カレンダー管理】運行カレンダー指定日登録エラー");
          }
        }
      }
    }

    // 日毎便ダイヤの再作成
    List<BusServiceInfoDto> busServiceList =
        busServicesMapper.getBusServicesByCalendarId(getUserSession().getBusinessCorpId(),
            calendarsEntity.getCalendarId());
    if (ExObjectUtils.isNotBlank(busServiceList)) {
      for (BusServiceInfoDto busServiceDto : busServiceList) {
        dailyBusDaiyasService.regenerateDailyBusDaiya(busServiceDto);
      }
    }

  }

  /**
   * 運行カレンダー変更
   *
   * @param form 詳細FORM
   */
  @Transactional
  public void update(CalendarsDetailForm form) {
    // 運行カレンダー変更
    CalendarsEntity calendarsEntity = new CalendarsEntity(form, getUserSession());
    if (calendarsMapper.updateCalendar(calendarsEntity) != 1) {
      throw new CommonDbException("【運行カレンダー管理】運行カレンダー変更エラー");
    }

    // 運行カレンダー明細取得
    List<CalendarDetailDto> calendarDetailList =
        calendarDetailsMapper.getCalendarDetailsByCalendarId(form.getCalendarId());
    // 既存の運行カレンダー明細は削除
    if (ExObjectUtils.isNotBlank(calendarDetailList)) {
      for (CalendarDetailDto detail : calendarDetailList) {
        CalendarDetailsEntity entity = new CalendarDetailsEntity(detail.getCalendarDetailId());
        if (calendarDetailsMapper.deleteCalendarDetail(entity) != 1) {
          throw new CommonDbException("【運行カレンダー管理】運行カレンダー明細削除エラー");
        }
      }
    }
    // 運行カレンダー明細登録
    this.insertCalendarDetails(form.getCalendarId(), form);

    // 運行カレンダー除外日の変更処理
    if (ExObjectUtils.isNotBlank(form.getCalendarCancelDateList())) {
      for (CalendarCancelDateDto dto : form.getCalendarCancelDateList()) {
        // 削除対処のデータは削除
        if (dto.isDelete() && ExObjectUtils.isNotBlank(dto.getCalendarCancelDateId())) {
          CalendarCancelDatesEntity entity =
              new CalendarCancelDatesEntity(dto.getCalendarCancelDateId());
          if (calendarCancelDatesMapper.deleteCancelDate(entity) != 1) {
            throw new CommonDbException("【運行カレンダー管理】運行カレンダー除外日削除エラー");
          }
        }
        // 登録対処のデータは登録
        if (!dto.isDelete() && ExObjectUtils.isNotBlank(dto.getOperationCancelDate())
            && ExObjectUtils.isBlank(dto.getCalendarCancelDateId()) && form.getWeekDesignatableFlag()
            .equals(WeekDesignatableFlag.DESIGNATION_ENABLED.getValue())) {
          CalendarCancelDatesEntity entity =
              new CalendarCancelDatesEntity(calendarsEntity.getCalendarId(),
                  dto.getOperationCancelDate());
          if (calendarCancelDatesMapper.registerCalendarCancelDate(entity) != 1) {
            throw new CommonDbException("【運行カレンダー管理】運行カレンダー除外日登録エラー");
          }
        }
      }
    }

    // 運行カレンダー指定日の変更処理
    if (ExObjectUtils.isNotBlank(form.getCalendarDesignatedDateList())) {
      for (CalendarDesignatedDateDto dto : form.getCalendarDesignatedDateList()) {
        // 削除対処のデータは削除
        if (dto.isDelete() && ExObjectUtils.isNotBlank(dto.getCalendarDesignatedDateId())) {
          CalendarDesignatedDatesEntity entity =
              new CalendarDesignatedDatesEntity(dto.getCalendarDesignatedDateId());
          if (calendarDesignatedDatesMapper.deleteDesignatedDate(entity) != 1) {
            throw new CommonDbException("【運行カレンダー管理】運行カレンダー指定日削除エラー");
          }
        }
        // 登録対処のデータは登録
        if (!dto.isDelete() && ExObjectUtils.isNotBlank(dto.getOperationDesignatedDate())
            && ExObjectUtils.isBlank(dto.getCalendarDesignatedDateId())) {
          CalendarDesignatedDatesEntity entity =
              new CalendarDesignatedDatesEntity(calendarsEntity.getCalendarId(),
                  dto.getOperationDesignatedDate());
          if (calendarDesignatedDatesMapper.registerCalendarDesignatedDate(entity) != 1) {
            throw new CommonDbException("【運行カレンダー管理】運行カレンダー指定日登録エラー");
          }
        }
      }
    }

    // 日毎便ダイヤの再作成
    List<BusServiceInfoDto> busServiceList =
        busServicesMapper.getBusServicesByCalendarId(getUserSession().getBusinessCorpId(),
            form.getCalendarId());
    if (ExObjectUtils.isNotBlank(busServiceList)) {
      for (BusServiceInfoDto busServiceDto : busServiceList) {
        dailyBusDaiyasService.regenerateDailyBusDaiya(busServiceDto);
      }
    }

  }

  /**
   * 運行カレンダー除外日取得
   *
   * @param calendarId 運行カレンダーID
   * @return 運行カレンダー除外日リスト
   */
  public List<CalendarCancelDateDto> getCancelDateByCalendarId(Long calendarId) {
    // 運行カレンダー除外日取得
    return calendarCancelDatesMapper.getCancelDateByCalendarId(calendarId);
  }

  /**
   * 運行カレンダー指定日取得
   *
   * @param calendarId 運行カレンダーID
   * @return 運行カレンダー指定日リスト
   */
  public List<CalendarDesignatedDateDto> getDesignatedDateByCalendarId(Long calendarId) {
    // 運行カレンダー指定日取得
    return calendarDesignatedDatesMapper.getDesignatedDateByCalendarId(calendarId);
  }

  /**
   * 日付のファマットを変換(「yyMMdd」→「uuuu-MM-dd」)
   *
   * @param yymmdd 「yymmdd」日付
   * @return 「uuuu-MM-dd」日付
   */
  private static String toIsoDateFromYyMMdd(String yymmdd) {
    try {
      LocalDate d = LocalDate.parse(yymmdd, YYMMDD_PARSER);
      return d.format(DateTimeFormatter.ISO_LOCAL_DATE);
    } catch (DateTimeParseException e) {
      return null;
    }
  }

  /** 「yyMMdd」パーサ */
  private static final DateTimeFormatter YYMMDD_PARSER =
      new DateTimeFormatterBuilder().appendValueReduced(ChronoField.YEAR, 2, 2, 2000)
          .appendPattern("MMdd").toFormatter();

  /**
   * 運行カレンダーIDから明細情報を取得し、曜日区分を反映
   *
   * @param calendar 運行カレンダーDTO
   */
  private void applyDayWeekFlags(CalendarDto calendar) {
    List<CalendarDetailDto> details =
        calendarDetailsMapper.getCalendarDetailsByCalendarId(calendar.getCalendarId());
    applyDayWeekFlags(calendar, details);
  }

  /**
   * 曜日区分を反映
   *
   * @param calendar 運行カレンダーDTO
   * @param detailList 運行カレンダー明細リスト
   */
  private void applyDayWeekFlags(CalendarDto calendar, List<CalendarDetailDto> detailList) {
    java.util.Set<String> types = new java.util.HashSet<>();
    if (detailList != null) {
      for (CalendarDetailDto detail : detailList) {
        String t = detail.getDayWeekType();
        if (ExObjectUtils.isNotBlank(t)) {
          t = t.trim();
          if (!t.isEmpty()) {
            types.add(t);
          }
        }
      }
    }
    calendar.setSunday(types.contains(DayWeekType.SUNDAY.getValue()));
    calendar.setMonday(types.contains(DayWeekType.MONDAY.getValue()));
    calendar.setTuesday(types.contains(DayWeekType.TUESDAY.getValue()));
    calendar.setWednesday(types.contains(DayWeekType.WEDNESDAY.getValue()));
    calendar.setThursday(types.contains(DayWeekType.THURSDAY.getValue()));
    calendar.setFriday(types.contains(DayWeekType.FRIDAY.getValue()));
    calendar.setSaturday(types.contains(DayWeekType.SATURDAY.getValue()));
    calendar.setHoliday(types.contains(DayWeekType.HOLIDAY.getValue()));
  }

  /**
   * 運行カレンダー明細登録
   *
   * @param calendarId 運行カレンダーID
   * @param form 運行カレンダー明細FORM
   */
  private void insertCalendarDetails(Long calendarId, CalendarsDetailForm form) {
    List<CalendarDetailsEntity> detailEntityList = new ArrayList<>();
    if (form.isSunday()) {
      detailEntityList.add(new CalendarDetailsEntity(calendarId, DayWeekType.SUNDAY.getValue()));
    }
    if (form.isMonday()) {
      detailEntityList.add(new CalendarDetailsEntity(calendarId, DayWeekType.MONDAY.getValue()));
    }
    if (form.isTuesday()) {
      detailEntityList.add(new CalendarDetailsEntity(calendarId, DayWeekType.TUESDAY.getValue()));
    }
    if (form.isWednesday()) {
      detailEntityList.add(new CalendarDetailsEntity(calendarId, DayWeekType.WEDNESDAY.getValue()));
    }
    if (form.isThursday()) {
      detailEntityList.add(new CalendarDetailsEntity(calendarId, DayWeekType.THURSDAY.getValue()));
    }
    if (form.isFriday()) {
      detailEntityList.add(new CalendarDetailsEntity(calendarId, DayWeekType.FRIDAY.getValue()));
    }
    if (form.isSaturday()) {
      detailEntityList.add(new CalendarDetailsEntity(calendarId, DayWeekType.SATURDAY.getValue()));
    }
    if (form.isHoliday()) {
      detailEntityList.add(new CalendarDetailsEntity(calendarId, DayWeekType.HOLIDAY.getValue()));
    }
    if (!detailEntityList.isEmpty()) {
      for (CalendarDetailsEntity detailEntity : detailEntityList) {
        if (calendarDetailsMapper.registerCalendarDetail(detailEntity) != 1) {
          throw new CommonDbException("【運行カレンダー管理】運行カレンダー詳細登録エラー");
        }
      }
    }
  }

}
