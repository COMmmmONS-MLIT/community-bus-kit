package maint.form.report;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.form.BaseForm;

/**
 * 路線時刻表出力FORM
 *
 * @author 林竜生
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RouteTimetableExcelForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = -2810714324511456313L;

  /** 事業者ID */
  private Long businessCorpId;
  /** 便ダイヤ改正日 */
  private String serviceStartDate;
  /** 路線ID */
  @NotNull
  private Long rosenId;
  /** 便ダイヤ改正ID */
  @NotNull
  private Long daiyaRevisionId;
}
