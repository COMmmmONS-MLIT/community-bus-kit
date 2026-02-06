package maint.dto.baseInfo.agencies;

import maint.dto.BaseDto;
import maint.form.agencies.AgenciesSearchForm;

public class SearchAgenciesDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 790128047631446058L;

  /** PKID */
  private Long businessCorpId;

  /** 事業者名・自治体名 */
  private String name;

  /** 法人番号 */
  private String operationCompanyNo;

  /** 利用区分 */
  private Integer availableFlag;

  /**
   * コンストラクタ
   *
   * @param form 事業者検索 Form
   * @param startIndex 行の開始インデックス
   */
  public SearchAgenciesDto(AgenciesSearchForm form, Long startIndex) {
    this.startIndex = startIndex;
    if (form == null) {
      return;
    }
    this.businessCorpId = form.getBusinessCorpId();
    this.name = form.getSearchName();
    this.operationCompanyNo = form.getSearchOperationCompanyNo();
    this.availableFlag = form.getSearchAvailableFlag();
  }

  /**
   * 部分一致Like検索用事業者名・自治体名取得
   *
   * @return 部分一致Like検索用事業者名・自治体名
   */
  public String getLikeSearchName() {
    return makeMiddleMatchString(this.name);
  }
}
