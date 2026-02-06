package maint.form.operatingReport;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.form.BaseForm;

/**
 * 運行回数出力FORM
 *
 * @author JEKAL.H
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OperatingReportListsExcelForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = -832346452393980836L;

  /** 事業者ID */
  private Long businessCorpId;
  @NotBlank
  private String serviceStartDate;
  /** 路線ID */
  @NotNull
  private Long rosenId;
  /** 利用開始年月 */
  @NotBlank
  private String serviceStartMonth;
  /** 利用終了年月 */
  @NotBlank
  private String serviceEndMonth;

}
