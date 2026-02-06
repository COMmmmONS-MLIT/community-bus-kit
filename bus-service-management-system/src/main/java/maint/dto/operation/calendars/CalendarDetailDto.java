package maint.dto.operation.calendars;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 運行カレンダー明細DTO
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CalendarDetailDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = -224093287L;

  /** 運行カレンダー明細ID */
  private Long calendarDetailId;

  /** 運行カレンダーID */
  private Long calendarId;

  /** 曜日区分 */
  private String dayWeekType;

}
