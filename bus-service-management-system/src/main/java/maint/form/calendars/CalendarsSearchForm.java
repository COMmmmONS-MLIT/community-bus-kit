package maint.form.calendars;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.enums.UsageDivision;
import maint.form.BaseForm;
import maint.validation.annonation.IllegalCharacters;
import maint.validation.annonation.ValidEnumValue;

/**
 * 運行カレンダー検索FORM
 *
 * @author g.ko
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CalendarsSearchForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = 75824395237L;

  /** 運行カレンダー区分（検索用） */
  @IllegalCharacters
  @Size(max = 60)
  private String calendarType;

  /** 曜日区分_日曜（検索用） */
  private boolean sunday;

  /** 曜日区分_月曜（検索用） */
  private boolean monday;

  /** 曜日区分_火曜（検索用） */
  private boolean tuesday;

  /** 曜日区分_水曜（検索用） */
  private boolean wednesday;

  /** 曜日区分_木曜（検索用） */
  private boolean thursday;

  /** 曜日区分_金曜（検索用） */
  private boolean friday;

  /** 曜日区分_土曜（検索用） */
  private boolean saturday;

  /** 曜日区分_祝日（検索用） */
  private boolean holiday;

  /** 利用区分 */
  @NotNull
  @ValidEnumValue(enumClass = UsageDivision.class)
  private Integer availableFlag;

  /**
   * コンストラクタ
   */
  public CalendarsSearchForm() {
    this.availableFlag = UsageDivision.ACTIVE.getValue();
  }

}
