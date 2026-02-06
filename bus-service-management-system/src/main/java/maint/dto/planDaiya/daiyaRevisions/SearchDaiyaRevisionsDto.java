package maint.dto.planDaiya.daiyaRevisions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;
import maint.form.daiyaRevisions.DaiyaRevisionsSearchForm;

/**
 * 便ダイヤ改正検索DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SearchDaiyaRevisionsDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = -130182204270670630L;

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
  /** 路線ID **/
  private Long rosenId;
  /** 事業者ID */
  private Long businessCorpId;

  /**
   * コンストラクタ
   *
   * @param form       便ダイヤ改正検索FORM
   * @param startIndex 行の開始インデックス
   */
  public SearchDaiyaRevisionsDto(DaiyaRevisionsSearchForm form, Long startIndex) {
    this.startIndex = startIndex;
    if (form == null) {
      return;
    }
    this.daiyaRevisionName = form.getSearchDaiyaRevisionName();
    this.serviceStartDate = form.getSearchServiceStartDate();
    this.serviceEndDate = form.getSearchServiceEndDate();
    this.rosenId = form.getSearchRosenId();
  }

}
