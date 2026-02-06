package jp.co.bus.batch.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 運行カレンダー明細検索用DTO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CalendarDetailSearchDto extends BaseDto {

  /** Serial version UID */
  private static final long serialVersionUID = 4312308508937L;

  /** 運行カレンダーID */
  private Long calendarId;
  /** 曜日区分 */
  private String dayWeekType;

  /**
   * コンストラクタ
   *
   * @param calendarId 運行カレンダーID
   * @param dayWeekType 曜日区分
   */
  public CalendarDetailSearchDto(Long calendarId, String dayWeekType) {
    this.calendarId = calendarId;
    this.dayWeekType = dayWeekType;
  }

}
