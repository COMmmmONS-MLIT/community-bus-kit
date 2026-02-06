package maint.dto.operation.calendars;

import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;

/**
 * 運行カレンダー情報DTO
 *
 * @author g.ko
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CalendarInfoDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -7543546347L;

  /** 運行カレンダーID */
  private Long calendarId;

  /** 事業者ID */
  private Long businessCorpId;

  /** 曜日指定可否区分（0:不可,1:可） */
  private Integer weekDesignatableFlag;

  /** 曜日指定区分 */
  private String weekDesignationType;

  /** 運行カレンダー区分 */
  private String calendarType;

  /** 利用可否区分 */
  private String availableFlag;

  /** 曜日区分 */
  private String dayWeekType;

}
