package maint.dto.planDaiya.chargeRevisions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;
import maint.form.ChargeRevisions.ChargeRevisionsSearchForm;
import maint.session.UserSession;

/**
 * 運賃改定検索DTO
 *
 * @author g.ko
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SearchChargeRevisionDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 754232436328L;

  /** 路線ID */
  private Long rosenId;

  /** 運賃適用開始日From */
  private String serviceStartDate;

  /** 運賃適用開始日To */
  private String serviceEndDate;

  /** 運賃改定名称 */
  private String revisionReason;

  /** 事業者ID */
  private Long businessCorpId;

  /**
   * コンストラクタ
   *
   * @param form 運賃改定検索FORM
   * @param startIndex 行の開始インデックス
   * @param userSession ユーザーセッション
   */
  public SearchChargeRevisionDto(ChargeRevisionsSearchForm form, Long startIndex, UserSession userSession) {
    this.startIndex = startIndex;
    if (form == null) {
      return;
    }
    this.rosenId = form.getRosenId();
    this.serviceStartDate = form.getServiceStartDate();
    this.serviceEndDate = form.getServiceEndDate();
    this.revisionReason = form.getRevisionReason();
    this.businessCorpId = userSession.getBusinessCorpId();
  }

  /**
   * 部分一致Like検索用運賃改定名称取得
   *
   * @return 部分一致Like検索用運賃改定名称
   */
  public String getLikeSearchRevisionReason() {
    return makeMiddleMatchString(this.revisionReason);
  }

}
