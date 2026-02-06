package maint.form.daiyaRevisions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.planDaiya.daiyaRevisions.DaiyaRevisionsDetailDto;
import maint.enums.DispMode;
import maint.form.BaseForm;
import maint.validation.annonation.DateFormat;
import maint.validation.annonation.IllegalCharacters;

/**
 * 便ダイヤ改正詳細FORM
 *
 * @author jeon
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DaiyaRevisionsDetailForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = 2905383519765022776L;

  /** 便ダイヤ改正ID */
  private Long daiyaRevisionId;
  /** 路線名 */
  private String rosenName;
  /** ダイヤ改正名称 */
  @NotBlank
  @IllegalCharacters
  @Size(min = 1, max = 50)
  private String daiyaRevisionName;
  /** 便ダイヤ改正開始日 */
  @NotBlank
  @DateFormat("yyyy-MM-dd")
  private String serviceStartDate;
  /** 便ダイヤ改正終了日 */
  private String serviceEndDate;
  /** 路線ID */
  @NotNull
  private Long rosenId;
  /** ユーザーID */
  private Long userId;
  /** 画面モード */
  private DispMode dispMode;
  /** 事業者ID */
  private Long businessCorpId;

  /**
   * コンストラクタ
   *
   * @param dto            便ダイヤ改正情報DTO
   * @param page           ページ
   * @param updateDispMode 変更モード
   */
  public void setInfo(DaiyaRevisionsDetailDto dto, Long page, DispMode updateDispMode) {
    this.daiyaRevisionId = dto.getDaiyaRevisionId();
    this.daiyaRevisionName = String.valueOf(dto.getDaiyaRevisionName());
    this.rosenId = dto.getRosenId();
    this.rosenName = dto.getRosenName();
    this.serviceStartDate = dto.getServiceStartDate();
    this.serviceEndDate = dto.getServiceEndDate();
    this.page = page;
    this.dispMode = updateDispMode;
  }
}
