package maint.entity;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maint.session.UserSession;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 運行カレンダー明細Entity
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDetailsEntity implements Serializable {

  /** Serial version UID */
  @Serial
  private static final long serialVersionUID = 238745283570L;

  /** 運行カレンダー明細ID */
  private Long calendarDetailId;
  /** 運行カレンダーID */
  private Long calendarId;
  /** 曜日区分 */
  private String dayWeekType;
  /** UserSession */
  private UserSession session;

  /**
   * コンストラクタ
   *
   * @param calendarId 運行カレンダーID
   * @param dayWeekType 運行カレンダー詳細FORM
   */
  public CalendarDetailsEntity(Long calendarId, String dayWeekType) {
    this.calendarId = calendarId;
    this.dayWeekType = dayWeekType;
    this.session =
        ((UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }

  /**
   * コンストラクタ
   *
   * @param calendarDetailId 運行カレンダー明細ID
   */
  public CalendarDetailsEntity(Long calendarDetailId) {
    this.calendarDetailId = calendarDetailId;
    this.session =
        ((UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }
}
