package maint.dto.baseInfo.operatingCompanies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;
import maint.form.operatingCompanies.OperatingCompaniesSearchForm;

/**
 * 運行会社検索DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SearchOperatingCompaniesDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = -3901562067637722533L;
  /** ユーザーID */
  private Long userId;
  /** 運行会社ID */
  private Long operatingCompanyId;
  /** 事業者ID */
  private Long businessCorpId;
  /** 事業者名 */
  private String businessCorpName;
  /** 運行会社名 */
  private String operatingName;
  /** 利用区分 */
  private Integer availableFlag;

  /**
   * コンストラクタ
   *
   * @param form       運行会社検索FORM
   * @param startIndex 行の開始インデックス
   */
  public SearchOperatingCompaniesDto(OperatingCompaniesSearchForm form, Long startIndex) {
    this.startIndex = startIndex;
    if (form == null) {
      return;
    }
    this.operatingName = form.getSearchOperatingName();
    this.availableFlag = form.getSearchAvailableFlag();
  }

  /**
   * 前方一致Like検索用ログインID取得
   *
   * @return 前方一致Like検索用ログインID
   */
  public String getLikeOperatingName() {
    return makeForwardMatchString(this.operatingName);
  }

}
