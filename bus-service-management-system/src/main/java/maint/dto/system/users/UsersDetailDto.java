package maint.dto.system.users;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * ユーザ情報DTO
 *
 * @author ryuichi.kashiwai
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UsersDetailDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 1872170029541855609L;

  /** ユーザID */
  private Long userId;

  /** メール */
  private String email;

  /** 氏名（姓） */
  private String lastName;

  /** 氏名（名） */
  private String firstName;

  /** パスワード変更済みフラグ */
  private Integer passwordChangedFlag;

  /** 事業者ID */
  private Long businessCorpId;

  /** 運行会社ID */
  private Long operatingCompanyId;

  /** アカウント区分（01：システム管理者、02：事業者） */
  private String accountDivision;

  /** 権限ID */
  private Long permRoleId;

  /** 退職区分 */
  private Integer retirementFlag;

  /** 退職年月日 */
  private String retirementDate;

  /** ロック時間 */
  private String accountLockAt;

  /** ロック解除 */
  private boolean lockFlag;
}
