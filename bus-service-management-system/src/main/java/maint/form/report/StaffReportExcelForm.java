package maint.form.report;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.form.BaseForm;

/**
 * スターフ出力FORM
 *
 * @author 林竜生
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StaffReportExcelForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = 6824435489252495135L;

  /** 事業者ID */
  private Long businessCorpId;
  /** 便ダイヤ改正開始日 */
  private String serviveStartDate;
  /** 路線ID */
  @NotNull
  private Long rosenId;
  /** 便ダイヤ改正ID */
  @NotNull
  private Long daiyaRevisionId;
}
