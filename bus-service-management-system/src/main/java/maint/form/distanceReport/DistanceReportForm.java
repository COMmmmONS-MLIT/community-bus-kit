package maint.form.distanceReport;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.form.BaseForm;

/**
 * キロ程表出力FORM
 *
 * @author y.kim
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DistanceReportForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = -7622555655717056036L;

  /** 処理対象日 */
  @NotBlank
  private String serviceDate;

  /** 路線ID */
  @NotNull
  private Long rosenId;

  /** 事業者ID */
  private Long businessCorpId;

}
