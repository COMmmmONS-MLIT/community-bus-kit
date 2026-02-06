package maint.form.holidayCalendars;

import javax.validation.constraints.Pattern;
import maint.form.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.utils.ExRegUtils;

/**
 * 祝日カレンダー検索FORM
 *
 * @author g.ko
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class HolidayCalendarSearchForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = 235333361766L;

  /** 基準カレンダー年 */
  @Pattern(regexp = ExRegUtils.calendarYearReg, message = "validation.common.format")
  private String srcYear;

  /** 基準カレンダー月 */
  @Pattern(regexp = ExRegUtils.calendarMonthReg, message = "validation.common.format")
  private String srcMonth;

  /**
   * 対象日付の取得
   *
   * @return 対象日付 ('yyyy-MM-dd')
   */
  public String getTargetDate() {
    return this.srcYear + "-" + this.srcMonth + "-01";
  }

}
