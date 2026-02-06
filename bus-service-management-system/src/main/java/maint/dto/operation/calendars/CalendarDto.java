package maint.dto.operation.calendars;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 運行カレンダー情報DTO
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CalendarDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = -543892576982354L;

  /** 運行カレンダーID */
  private Long calendarId;

  /** 運行カレンダー区分 */
  private String calendarType;

  /** 曜日指定可否区分（0：不可,1：可） */
  private Integer weekDesignatableFlag;

  /** 曜日指定区分 */
  private String weekDesignationType;

  /** 利用区分（0：利用不可、1：利用中） */
  private Integer availableFlag;

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

  /** 曜日区分_祝日 */
  private boolean holiday;

}
