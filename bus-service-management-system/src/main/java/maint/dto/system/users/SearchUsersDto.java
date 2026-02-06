package maint.dto.system.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;
import maint.form.users.UsersSearchForm;
import maint.session.UserSession;

/**
 * ユーザ検索DTO
 *
 * @author ryuichi.kashiwai
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SearchUsersDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 790128047631446058L;

  /** メール */
  private String email;

  /** 氏名（姓） */
  private String lastName;

  /** 氏名（名） */
  private String firstName;

  /** 退職区分 */
  private Integer retirementFlag;

  /** ユーザー区分 */
  private String accountDivision;

  /** 事業者ID */
  private Long businessCorpId;

  /**
   * コンストラクタ
   *
   * @param form        ユーザ検索 Form
   * @param startIndex  行の開始インデックス
   * @param userSession ユーザーセッション
   */
  public SearchUsersDto(UsersSearchForm form, Long startIndex, UserSession userSession) {
    this.startIndex = startIndex;
    if (form == null) {
      return;
    }
    this.email = form.getSearchEmail();
    this.lastName = form.getSearchLastName();
    this.firstName = form.getSearchFirstName();
    this.retirementFlag = form.getSearchUsageDivision();
    this.accountDivision = userSession.getAccountDivision();
    this.businessCorpId = userSession.getBusinessCorpId();
  }

  /**
   * 前方一致Like検索用ログインID取得
   *
   * @return 前方一致Like検索用ログインID
   */
  public String getLikeSearchEmail() {
    return makeForwardMatchString(this.email);
  }

  /**
   * 部分一致Like検索用ユーザ名（姓）取得
   *
   * @return 部分一致Like検索用ユーザ名（姓）
   */
  public String getLikeSearchLastName() {
    return makeMiddleMatchString(this.lastName);
  }

  /**
   * 部分一致Like検索用ユーザ名（名）取得
   *
   * @return 部分一致Like検索用ユーザ名（名）
   */
  public String getLikeSearchFirstName() {
    return makeMiddleMatchString(this.firstName);
  }
}
