package maint.entity;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maint.session.UserSession;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 運行カレンダー除外日設定Entity
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarCancelDatesEntity implements Serializable {

  /** Serial version UID */
  @Serial
  private static final long serialVersionUID = -4987236597832L;

  /** 運行カレンダー除外日設定ID */
  private Long calendarCancelDateId;
  /** 運行カレンダーID */
  private Long calendarId;
  /** 運行除外日 */
  private String operationCancelDate;
  /** UserSession */
  private UserSession session;

  /**
   * コンストラクタ
   *
   * @param calendarId 運行カレンダーID
   * @param operationCancelDate 運行除外日
   */
  public CalendarCancelDatesEntity(Long calendarId, String operationCancelDate) {
    this.calendarId = calendarId;
    this.operationCancelDate = operationCancelDate;
    this.session =
        ((UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }

  /**
   * コンストラクタ
   *
   * @param calendarCancelDateId 運行カレンダー除外日設定
   */
  public CalendarCancelDatesEntity(Long calendarCancelDateId) {
    this.calendarCancelDateId = calendarCancelDateId;
    this.session =
        ((UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }
}
