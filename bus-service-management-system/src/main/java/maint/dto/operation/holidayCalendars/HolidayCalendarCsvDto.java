package maint.dto.operation.holidayCalendars;

import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;
import maint.session.UserSession;

/**
 * 祝日カレンダーDTO（CSV用）
 *
 * @author g.ko
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HolidayCalendarCsvDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -8254256566242L;

  /** 祝日ID */
  private Long holidayId;

  /** 祝日 */
  private String holidayDate;

  /** 祝日名所 */
  private String holidayName;

  /** 事業者ID */
  private Long businessCorpId;

  /** UserSession */
  private UserSession session;

  /**
   * コンストラクタ
   *
   * @param session ユーザーセッション
   */
  public HolidayCalendarCsvDto(UserSession session) {
    this.businessCorpId = session.getBusinessCorpId();
    this.session = session;
  }

}
