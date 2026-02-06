package maint.dto.baseInfo.offices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;
import maint.form.offices.OfficesSearchForm;

/**
 * 営業所管理検索DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SearchOfficesDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = -841860245714865396L;

  /** 営業所ID */
  private Long officeId;
  /** 運行会社名 */
  private String operatingName;
  /** 営業所名 */
  private String name;
  /** 利用区分 */
  private Integer availableFlag;
  /** 事業者ID */
  private Long businessCorpId;
  /** 運行会社ID */
  private Long operatingCompanyId;
  /** ユーザーID */
  private Long userId;

  /**
   * コンストラクタ
   *
   * @param form       営業所管理検索FORM
   * @param startIndex 行の開始インデックス
   */
  public SearchOfficesDto(OfficesSearchForm form, Long startIndex) {
    this.startIndex = startIndex;
    if (form == null) {
      return;
    }
    this.name = form.getSearchName();
    this.operatingCompanyId = form.getSearchOperatingCompanyId();
    this.availableFlag = form.getSearchAvailableFlag();
  }

  /**
   * 部分一致Like検索用ログインID取得
   *
   * @return 部分一致Like検索用ログインID
   */
  public String getLikeName() {
    return makeMiddleMatchString(this.name);
  }

}
