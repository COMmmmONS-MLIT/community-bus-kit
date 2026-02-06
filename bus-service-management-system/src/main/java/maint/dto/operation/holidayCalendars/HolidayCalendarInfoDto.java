package maint.dto.operation.holidayCalendars;

import maint.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 祝日カレンダー DTO
 *
 * @author g.ko
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HolidayCalendarInfoDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -524662483767431L;

  /** 祝日カレンダーID */
  private Long holidayId;

  /** 祝日 */
  private String holidayDate;

}
