package maint.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.form.daiyaRevisions.DaiyaRevisionsDetailForm;

/**
 * 便ダイヤ改正 Entity
 *
 * @author jeon
 */

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DaiyaRevisionsEntity extends BaseEntity {
  /** Serial version UID */
  private static final long serialVersionUID = -6951896605745862623L;


  /** 便ダイヤ改正ID */
  private Long daiyaRevisionId;
  /** 路線名 */
  private String rosenName;
  /** ダイヤ改正名称 */
  private String daiyaRevisionName;
  /** 便ダイヤ改正開始日 */
  private String serviceStartDate;
  /** 便ダイヤ改正終了日 */
  private String serviceEndDate;
  /** 路線ID */
  private Long rosenId;
  /** 事業者ID */
  private Long businessCorpId;
  /** 運行会社ID */
  private Long operatingCompanyId;

  /**
   * コンストラクタ
   *
   * @param form 便ダイヤ改正詳細FORM
   */
  public DaiyaRevisionsEntity(DaiyaRevisionsDetailForm form) {
    this.daiyaRevisionId = form.getDaiyaRevisionId();
    this.daiyaRevisionName = form.getDaiyaRevisionName();
    this.rosenId = form.getRosenId();
    this.rosenName = form.getRosenName();
    this.serviceStartDate = form.getServiceStartDate();
    this.serviceEndDate = form.getServiceEndDate();
  }

}
