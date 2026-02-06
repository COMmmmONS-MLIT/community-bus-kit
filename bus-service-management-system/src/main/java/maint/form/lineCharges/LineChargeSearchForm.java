package maint.form.lineCharges;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.form.BaseForm;
import maint.validation.annonation.DateFormat;
import maint.validation.annonation.IllegalCharacters;

/**
 * 運賃検索FORM
 *
 * @author g.ko
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LineChargeSearchForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = 913876419645L;

  /** 路線ID（検索用） */
  private Long rosenId;

  /** 運賃適用開始日From（検索用） */
  @DateFormat("yyyy-MM-dd")
  private String serviceStartDate;

  /** 運賃適用開始日To（検索用） */
  @DateFormat("yyyy-MM-dd")
  private String serviceEndDate;

  /** 運賃改定名称（検索用） */
  @IllegalCharacters
  @Size(max = 50)
  private String revisionReason;

}
