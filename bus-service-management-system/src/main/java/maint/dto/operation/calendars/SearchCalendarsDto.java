package maint.dto.operation.calendars;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;
import maint.form.calendars.CalendarsSearchForm;
import maint.session.UserSession;
import maint.utils.ExObjectUtils;

/**
 * 運行カレンダー検索DTO
 *
 * @author g.ko
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SearchCalendarsDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 31629432895809L;

  /** 運行カレンダー区分 */
  private String calendarType;

  /** 曜日区分_日曜 */
  private boolean sunday;

  /** 曜日区分_月曜 */
  private boolean monday;

  /** 曜日区分_火曜 */
  private boolean tuesday;

  /** 曜日区分_水曜 */
  private boolean wednesday;

  /** 曜日区分_木曜 */
  private boolean thursday;

  /** 曜日区分_金曜 */
  private boolean friday;

  /** 曜日区分_土曜 */
  private boolean saturday;

  /** 曜日区分_祝日（検索用） */
  private boolean holiday;

  /** 利用区分 */
  private Integer availableFlag;

  /** 事業者ID */
  private Long businessCorpId;

  /**
   * コンストラクタ
   *
   * @param form 運行カレンダー検索 Form
   * @param startIndex 行の開始インデックス
   * @param userSession ユーザーセッション
   */
  public SearchCalendarsDto(CalendarsSearchForm form, Long startIndex, UserSession userSession) {
    this.startIndex = startIndex;
    if (ExObjectUtils.isBlank(form)) {
      return;
    }
    this.calendarType = form.getCalendarType();
    this.sunday = form.isSunday();
    this.monday = form.isMonday();
    this.tuesday = form.isTuesday();
    this.wednesday = form.isWednesday();
    this.thursday = form.isThursday();
    this.friday = form.isFriday();
    this.saturday = form.isSaturday();
    this.holiday = form.isHoliday();
    this.availableFlag = form.getAvailableFlag();
    this.businessCorpId = userSession.getBusinessCorpId();
  }

  /**
   * 部分一致Like運行カレンダー区分取得
   *
   * @return 部分一致Like運行カレンダー区分
   */
  public String getLikeCalendarType() {
    return makeMiddleMatchString(this.calendarType);
  }
}
