package maint.form.fareReport;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.form.BaseForm;

/**
 * 運賃表出力FORM
 *
 * @author JEKAL.H
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FareReportListsExcelForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = -832346452393980836L;

  /** 事業者ID */
  private Long businessCorpId;
  /** 運賃改定開始日 */
  private String serviceStartDate;
  /** 運賃改定ID */
  @NotNull
  private Long chargeRevisionId;
  /** 路線ID */
  @NotNull
  private Long rosenId;
}
