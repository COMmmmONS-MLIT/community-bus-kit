package maint.entity;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maint.form.calendars.CalendarsDetailForm;
import maint.session.UserSession;

/**
 * 運行カレンダーEntity
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarsEntity implements Serializable {

  /** Serial version UID */
  @Serial
  private static final long serialVersionUID = 3926843265L;

  /** 運行カレンダーID */
  private Long calendarId;
  /** 事業者ID */
  private Long businessCorpId;
  /** 曜日指定可否区分（0：不可,1：可） */
  private Integer weekDesignatableFlag;
  /** 曜日指定区分（0:祝日優先、1:曜日優先） */
  private String weekDesignationType;
  /** 運行カレンダー区分 */
  private String calendarType;
  /** 利用区分 */
  private Integer availableFlag;
  /** UserSession */
  private UserSession session;

  /**
   * コンストラクタ
   *
   * @param form 運行カレンダー詳細FORM
   * @param session ユーザーセッション
   */
  public CalendarsEntity(CalendarsDetailForm form, UserSession session) {
    this.calendarId = form.getCalendarId();
    this.businessCorpId = session.getBusinessCorpId();
    this.weekDesignatableFlag = form.getWeekDesignatableFlag();
    this.weekDesignationType = form.getWeekDesignationType();
    this.calendarType = form.getCalendarType();
    this.availableFlag = form.getAvailableFlag();
    this.session = session;
  }
}
