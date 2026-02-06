package maint.form.ChargeRevisions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.form.BaseForm;
import maint.validation.annonation.DateFormat;
import maint.validation.annonation.IllegalCharacters;

/**
 * 運賃改定詳細FORM
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ChargeRevisionsDetailForm extends BaseForm {

  /** Serial Version UID */
  @Serial
  private static final long serialVersionUID = 392874361674223L;

  /** 運賃改定ID */
  private Long chargeRevisionId;

  /** 路線ID */
  @NotNull
  private Long rosenId;

  /** 運賃適用開始日 */
  @NotBlank
  @DateFormat("yyyy-MM-dd")
  private String serviceStartDate;

  /** 運賃改定名称 */
  @NotBlank
  @IllegalCharacters
  @Size(max = 50)
  private String revisionReason;

  /**
   * コンストラクタ
   *
   * @param page ページ番号
   */
  public ChargeRevisionsDetailForm(Long page) {
    this.page = page;
  }

}
