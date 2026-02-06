package maint.entity;

import maint.session.UserSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 管理者2要素認証履歴Entity
 *
 * @author sangsoo.lee
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TwoFactorAuthHistoriesEntity extends BaseEntity {

  /** Serial version UID */
  private static final long serialVersionUID = -204853679273770433L;

  /** 2要素認証履歴ID */
  private Long twoFactorAuthHistoryId;

  /** ユーザID */
  private Long userId;

  /** 認証コード */
  private String authCode;

  /** コード発行日時 */
  private String codeIssueDatetime;

  /**
   * コンストラクタ
   *
   * @param userSession UserSession
   */
  public TwoFactorAuthHistoriesEntity(UserSession userSession) {
    this.userId = userSession.getUserId();
  }

}
