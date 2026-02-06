package maint.form.calendars;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.operation.calendars.CalendarCancelDateDto;
import maint.dto.operation.calendars.CalendarDesignatedDateDto;
import maint.dto.operation.calendars.CalendarDto;
import maint.enums.DispMode;
import maint.enums.UsageDivision;
import maint.enums.WeekDesignatableFlag;
import maint.enums.WeekDesignationType;
import maint.form.BaseForm;
import maint.validation.annonation.IllegalCharacters;
import maint.validation.annonation.ValidEnumValue;

/**
 * 運行カレンダー詳細FORM
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CalendarsDetailForm extends BaseForm {

  /** Serial Version UID */
  @Serial
  private static final long serialVersionUID = 37576265L;

  /** 運行カレンダーID */
  private Long calendarId;

  /** 画面モード */
  private DispMode dispMode;

  /** 運行カレンダー区分 */
  @NotBlank
  @IllegalCharacters
  @Size(max = 60)
  private String calendarType;

  /** 曜日指定可否区分（0：不可,1：可） */
  @NotNull
  @ValidEnumValue(enumClass = WeekDesignatableFlag.class)
  private Integer weekDesignatableFlag;

  /** 曜日祝日指定区分 */
  @NotBlank
  @ValidEnumValue(enumClass = WeekDesignationType.class)
  private String weekDesignationType;

  /** 曜日区分_日曜（検索用） */
  private boolean sunday;

  /** 曜日区分_月曜（検索用） */
  private boolean monday;

  /** 曜日区分_火曜（検索用） */
  private boolean tuesday;

  /** 曜日区分_水曜（検索用） */
  private boolean wednesday;

  /** 曜日区分_木曜（検索用） */
  private boolean thursday;

  /** 曜日区分_金曜（検索用） */
  private boolean friday;

  /** 曜日区分_土曜（検索用） */
  private boolean saturday;

  /** 曜日区分_祝日（検索用） */
  private boolean holiday;

  /** 利用区分（0：利用不可、1：利用中） */
  @NotNull
  @ValidEnumValue(enumClass = UsageDivision.class)
  private Integer availableFlag;

  /** 運行カレンダー除外日 */
  List<CalendarCancelDateDto> calendarCancelDateList;

  /** 運行カレンダー指定日 */
  List<CalendarDesignatedDateDto> calendarDesignatedDateList;

  /**
   * コンストラクタ（新規登録時）
   *
   * @param page ページ番号
   */
  public CalendarsDetailForm(Long page) {
    this.page = page;
    this.dispMode = DispMode.INSERT_MODE;
    this.weekDesignatableFlag = WeekDesignatableFlag.DESIGNATION_ENABLED.getValue();
    this.weekDesignationType = WeekDesignationType.HOLIDAY_PRIORITY.getValue();
    this.availableFlag = UsageDivision.ACTIVE.getValue();
    this.calendarCancelDateList = new ArrayList<>();
    this.calendarDesignatedDateList = new ArrayList<>();
  }

  /**
   * コンストラクタ
   *
   * @param dto 運行カレンダーDTO
   * @param page ページ
   * @param updateDispMode 変更モード
   */
  public void setInfo(CalendarDto dto, Long page, DispMode updateDispMode) {
    this.page = page;
    this.dispMode = updateDispMode;
    this.calendarId = dto.getCalendarId();
    this.calendarType = dto.getCalendarType();
    this.weekDesignatableFlag = dto.getWeekDesignatableFlag();
    this.weekDesignationType = dto.getWeekDesignationType();
    this.sunday = dto.isSunday();
    this.monday = dto.isMonday();
    this.tuesday = dto.isTuesday();
    this.wednesday = dto.isWednesday();
    this.thursday = dto.isThursday();
    this.friday = dto.isFriday();
    this.saturday = dto.isSaturday();
    this.holiday = dto.isHoliday();
    this.availableFlag = dto.getAvailableFlag();
  }

}
