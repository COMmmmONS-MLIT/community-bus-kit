package maint.form.operationRecords;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.form.BaseForm;
import maint.validation.annonation.DateFormat;

/**
 * 運行実績FORM
 *
 * @author g.ko
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OperationRecordsForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = 643623547254L;

  /** 抽出開始日 */
  @NotBlank
  @DateFormat("yyyy-MM-dd")
  private String extractionStartDate;

  /** 抽出終了日 */
  @NotBlank
  @DateFormat("yyyy-MM-dd")
  private String extractionEndDate;

  /** 路線ID */
  @NotNull
  private Long rosenId;

}
