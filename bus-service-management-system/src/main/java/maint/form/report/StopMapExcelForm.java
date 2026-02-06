package maint.form.report;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.form.BaseForm;
import java.time.LocalDate;

/**
 * 停留所付近図出力FORM
 *
 * @author 林竜生
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StopMapExcelForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = -2810714324511456313L;

  /** 事業者ID */
  private Long businessCorpId;
  /** 便ダイヤ改正ID */
  private Long daiyaRevisionId;
  /** 路線ID */
  @NotNull
  private Long rosenId;
  /** 処理対象日 */
  @NotNull
  private LocalDate processingDate;
}
