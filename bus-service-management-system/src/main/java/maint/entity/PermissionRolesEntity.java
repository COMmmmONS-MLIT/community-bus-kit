package maint.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 権限ロール Entity
 *
 * @author y.kim
 */

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionRolesEntity extends BaseEntity {

  /** Serial version UID */
  private static final long serialVersionUID = 6875786475765876587L;

  /** 権限ロールID */
  private Long permRoleId;

  /** 権限ロール名 */
  private String permRoleName;
}
