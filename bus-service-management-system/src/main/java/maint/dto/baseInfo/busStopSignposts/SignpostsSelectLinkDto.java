package maint.dto.baseInfo.busStopSignposts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;
import maint.form.busStopSignposts.SignpostsLinkSearchForm;
import maint.session.UserSession;

/**
 * 標柱検索DTO(停留所・標柱管理画面用)
 *
 * @author k.takeuchi
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SignpostsSelectLinkDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 8459940768L;

  /** 停留所ID */
  private Long busStopId;

  /** 標柱枝番 */
  private String signpostBranchNumber;

  /** 標柱名称 */
  private String signpostName;

  /** 利用可否区分（0：利用不可、1：利用中） */
  private Integer availableFlag;

  /** 事業者ID */
  private Long businessCorpId;

  /**
   * コンストラクタ
   *
   * @param form        検索 Form
   * @param startIndex  行の開始インデックス
   * @param userSession ユーザーセッション
   */
  public SignpostsSelectLinkDto(SignpostsLinkSearchForm form, Long startIndex, UserSession userSession) {
    this.startIndex = startIndex;
    if (form == null) {
      return;
    }
    this.busStopId = form.getSearchBusStopId();
    this.signpostBranchNumber = form.getSearchSignpostBranchNumber();
    this.signpostName = form.getSearchSignpostName();
    this.availableFlag = form.getAvailableFlag();
    this.businessCorpId = userSession.getBusinessCorpId();
  }

  /**
   * 部分一致Like標柱名称取得
   *
   * @return 部分一致Like標柱名称
   */
  public String getLikeSearchSignpostName() {
    return makeMiddleMatchString(this.signpostName);
  }
}
