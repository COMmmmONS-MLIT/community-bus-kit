package maint.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maint.form.users.UsersDetailForm;
import maint.session.UserSession;
import maint.utils.ExObjectUtils;
import maint.utils.PasswordUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serial;
import java.io.Serializable;

/**
 * 管理者Entity
 *
 * @author administrator
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersEntity implements Serializable {

  /** Serial version UID */
  @Serial
  private static final long serialVersionUID = 5571354923662939941L;

  /** ユーザID */
  private Long userId;
  /** 氏名_姓 */
  private String lastName;
  /** 氏名_名 */
  private String firstName;
  /** 退職済みフラグ */
  private Integer retirementFlag;
  /** 退職日 */
  private String retirementDate;
  /** ログインID */
  private String email;
  /** パスワード */
  private String password;
  /** 初期パスワード変更済みフラグ */
  private Integer passwordChangedFlag;
  /** アカウント区分 */
  private String accountDivision;
  /** 権限ロールID */
  private Long permRoleId;
  /** 事業者ID */
  private Long businessCorpId;
  /** 運行会社ID */
  private Long operatingCompanyId;
  /** UserSession */
  private UserSession session;
  /**  ロック解除 */
  private boolean lockFlag;
  /** 2要素認証日時 */
  private String twoFactorAuthDatetime;
  /**
   * コンストラクタ
   *
   * @param form     ユーザ詳細FORM
   */
  public UsersEntity(UsersDetailForm form) {
    this.userId = form.getUserId();
    this.email = form.getEmail();
    if(ExObjectUtils.isNotBlank(form.getPassword())) {
      this.password = PasswordUtils.getBCryptStr(form.getPassword());
    }
    this.passwordChangedFlag = form.isPwdChgFlag() ? 0 : 1;
    this.lastName = form.getLastName();
    this.firstName = form.getFirstName();
    this.accountDivision = form.getAccountDivision();
    this.businessCorpId = form.getAgencyId();
    this.operatingCompanyId = form.getOperatingCompanyId();
    this.permRoleId = form.getPermRoleId();
    this.retirementFlag = form.getUsageDivision();
    this.retirementDate = form.getRetirementDate();
    this.lockFlag = form.isLockFlag();
    this.session =
        ((UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }

  /**
   * コンストラクタ
   *
   * @param userId        ユーザID
   * @param bCryptStrPass 暗号化されたパスワード
   */
  public UsersEntity(Long userId, String bCryptStrPass) {
    this.userId = userId;
    this.password = bCryptStrPass;
    this.session =
        ((UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }

  /**
   * コンストラクタ
   *
   * @param userId PKID
   * @param userSession UserSession
   */
  public UsersEntity(Long userId, UserSession userSession) {
    this.userId = userId;
    this.session = userSession;
  }
}
