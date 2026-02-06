package maint.dto.operation.lineRoutes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;
import maint.form.lineRoutes.LineRoutesSearchForm;

/**
 * 系統検索DTO
 *
 * @author y.kim
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SearchLineRoutesDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 990128047631446058L;

  /** PKID */
  private Long lineRouteId;

  /** 路線ID */
  private Long rosenId;

  /** 事業者ID */
  private Long businessCorpId;

  /** 系統名称 */
  private String lineRouteName;

  /** 路線名称 */
  private String rosenName;

  /** 利用開始年月日 */
  private String serviceStartDate;

  /** 利用終了年月日 */
  private String serviceEndDate;

  /** 利用可否区分 */
  private Integer availableFlag;

  /**
   * コンストラクタ
   *
   * @param form 系統検索 Form
   * @param startIndex 行の開始インデックス
   */
  public SearchLineRoutesDto(LineRoutesSearchForm form, Long startIndex) {
    this.startIndex = startIndex;
    if (form == null) {
      return;
    }
    this.rosenId = form.getRosenId();
    this.lineRouteName = form.getLineRouteName();
    this.businessCorpId = form.getBusinessCorpId();
    this.availableFlag = form.getSearchAvailableFlag();
  }

  /**
   * 部分一致Like検索用系統名取得
   *
   * @return 部分一致Like検索用系統名
   */
  public String getLikeSearchLineRouteName() {
    return makeMiddleMatchString(this.lineRouteName);
  }

}
