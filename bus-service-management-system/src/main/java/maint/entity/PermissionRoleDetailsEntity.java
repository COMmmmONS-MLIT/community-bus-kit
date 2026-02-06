package maint.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 権限ロール詳細 Entity
 *
 * @author y.kim
 */

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionRoleDetailsEntity extends BaseEntity {

  /** Serial version UID */
  private static final long serialVersionUID = 3894056237985754098L;
  /** 書込権限 */
  Integer writePermFlg;
  /** DL権限 */
  Integer downloadPermFlg;
  /** 参照権限フラグ */
  Integer readPermFlg;
  /** 権限ロール詳細ID */
  private Long permissionRoleDetailId;
  /** 権限ロールID */
  private Long permRoleId;
  /** メニューコード */
  private String menuCode;

}
