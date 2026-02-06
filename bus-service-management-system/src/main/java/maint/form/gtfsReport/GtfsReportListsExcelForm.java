package maint.form.gtfsReport;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.form.BaseForm;
import maint.validation.annonation.DateFormat;

/**
 * GTFS-ScheduleFORM
 *
 * @author JEON
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GtfsReportListsExcelForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = -5235324623667L;

  /** 対象日 */
  @NotBlank
  @DateFormat("yyyy-MM-dd")
  private String targetDate;

  /** 事業者ID */
  private Long businessCorpId;

  /** 運行カレンダーID */
  private Integer calendarId;

  /** 便ダイヤ改正ID */
  private Long daiyaRevisionId;

}
