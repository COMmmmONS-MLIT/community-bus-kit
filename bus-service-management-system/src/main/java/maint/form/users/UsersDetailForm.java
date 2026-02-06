package maint.form.users;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.system.users.UsersDetailDto;
import maint.enums.*;
import maint.form.BaseForm;
import maint.session.UserSession;
import maint.utils.ExObjectUtils;
import maint.validation.annonation.IllegalCharacters;
import maint.validation.annonation.ValidEnumValue;
import maint.validation.annonation.ValidPassword;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.util.Objects;

/**
 * 社員詳細FORM
 *
 * @author administrator
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UsersDetailForm extends BaseForm {

  /** Serial Version UID */
  @Serial
  private static final long serialVersionUID = 4729899456293569653L;

  /** PKID */
  private Long userId;

  /** 画面モード */
  private DispMode dispMode;

  /** ログインID */
  @NotBlank
  @Email
  @Size(max = 256)
  private String email;

  /** パスワード */
  @IllegalCharacters
  @ValidPassword
  private String password;

  /** パスワードメール送信フラグ */
  @Range(min = 0, max = 1)
  private Integer pwdSendMailFlag = 0;

  /** パスワード変更フラグ */
  private boolean pwdChgFlag;

  /** 氏名（姓） */
  @NotBlank
  @Size(max = 50)
  @IllegalCharacters
  private String lastName;

  /** 氏名（名） */
  @NotBlank
  @Size(max = 50)
  @IllegalCharacters
  private String firstName;

  /** アカウント区分（01：システム管理者、02：事業者） */
  @NotNull
  @ValidEnumValue(enumClass = AccountDivision.class)
  private String accountDivision;

  /** 権限ID */
  @NotNull
  private Long permRoleId;

  /** 事業者ID */
  @NotNull
  private Long agencyId;

  /** 運行会社ID */
  private Long operatingCompanyId;

  /** 利用区分 */
  @NotNull
  @ValidEnumValue(enumClass = UsageDivision.class)
  private Integer usageDivision;

  /** 退職年月日 */
  private String retirementDate;

  /** ロック時間 */
  private String accountLockAt;

  /** ロック解除 */
  private boolean lockFlag;
  /**
   * コンストラクタ（新規登録時）
   *
   * @param page ページ番号
   */
  public UsersDetailForm(Long page, UserSession session) {
    this.page = page;
    this.dispMode = DispMode.INSERT_MODE;
    this.pwdChgFlag = true;
    this.usageDivision = RetirementFlag.EMPLOYED.getValue();
    if (session.getAccountDivision().equals(AccountDivision.SYSTEM_ADMIN.getValue())) {
      this.accountDivision = AccountDivision.SYSTEM_ADMIN.getValue();
    } else {
      this.accountDivision = AccountDivision.AGENCY.getValue();
      this.agencyId = session.getBusinessCorpId();
      this.operatingCompanyId = session.getOperatingCompanyId();
    }
  }

  /**
   * コンストラクタ
   *
   * @param dto ユーザ情報DTO
   * @param page ページ
   * @param updateDispMode 変更モード
   */
  public void setInfo(UsersDetailDto dto, Long page, DispMode updateDispMode) {
    this.userId = dto.getUserId();
    this.email = dto.getEmail();
    this.pwdChgFlag =
        Objects.equals(dto.getPasswordChangedFlag(), PwdChangedFlag.NOT_CHANGED.getValue());
    this.password = null;
    this.lastName = dto.getLastName();
    this.firstName = dto.getFirstName();
    this.accountDivision = dto.getAccountDivision();
    this.agencyId = dto.getBusinessCorpId();
    this.operatingCompanyId = dto.getOperatingCompanyId();
    this.permRoleId = dto.getPermRoleId();
    this.usageDivision = dto.getRetirementFlag();
    this.retirementDate = dto.getRetirementDate();
    this.accountLockAt = dto.getAccountLockAt();
    this.lockFlag = dto.isLockFlag();
    this.dispMode = updateDispMode;
  }

  /**
   * パスワード必須チェック（登録時）
   *
   * @return チェック結果
   */
  @AssertTrue(message = "{jakarta.validation.constraints.NotBlank.message}")
  public boolean isNotBlankPasswordForRegister() {
    return !DispMode.INSERT_MODE.equals(this.dispMode) || !ExObjectUtils.isBlank(this.password);
  }
}
