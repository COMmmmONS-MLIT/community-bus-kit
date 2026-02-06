package maint.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maint.session.UserSession;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serial;
import java.io.Serializable;

/**
 * 運行カレンダー指定日設定Entity
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDesignatedDatesEntity implements Serializable {

  /** Serial version UID */
  @Serial
  private static final long serialVersionUID = -5439870207602L;

  /** 運行カレンダー指定日設定ID */
  private Long calendarDesignatedDateId;
  /** 運行カレンダーID */
  private Long calendarId;
  /** 運行指定日 */
  private String operationDesignatedDate;
  /** UserSession */
  private UserSession session;

  /**
   * コンストラクタ
   *
   * @param calendarId 運行カレンダーID
   * @param operationDesignatedDate 運行指定日
   */
  public CalendarDesignatedDatesEntity(Long calendarId, String operationDesignatedDate) {
    this.calendarId = calendarId;
    this.operationDesignatedDate = operationDesignatedDate;
    this.session =
        ((UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }

  /**
   * コンストラクタ
   *
   * @param calendarDesignatedDateId 運行カレンダー指定日設定
   */
  public CalendarDesignatedDatesEntity(Long calendarDesignatedDateId) {
    this.calendarDesignatedDateId = calendarDesignatedDateId;
    this.session =
        ((UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }
}
