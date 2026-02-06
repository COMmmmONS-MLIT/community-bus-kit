package maint.form.displayDaiyas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.form.BaseForm;
import maint.validation.annonation.DateFormat;

/**
 * 棒ダイヤ検索FORM
 *
 * @author y.kim
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DisplayDaiyasSearchForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = 84365619799330L;

  /** 事業者ID（検索用） */
  private Long businessCorpId;

  /** 路線ID（検索用） */
  @NotNull
  private Long rosenId;

  /** 運行日（検索用） */
  @DateFormat("yyyy-MM-dd")
  @NotBlank
  private String serviceDate;

  /** 仕業番号（検索用） */
  @Size(max = 20)
  private String workNumber;

}
