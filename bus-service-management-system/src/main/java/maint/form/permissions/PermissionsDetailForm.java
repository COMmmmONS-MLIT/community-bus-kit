package maint.form.permissions;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.system.permissions.PermissionRoleDetailsDto;
import maint.dto.system.permissions.PermissionsMenuDto;
import maint.entity.PermissionRoleDetailsEntity;
import maint.entity.PermissionRolesEntity;
import maint.enums.DispMode;
import maint.form.BaseForm;
import maint.form.ValidationGroups.Delete;
import maint.validation.annonation.IllegalCharacters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 権限詳細FORM
 *
 * @author y.kim
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PermissionsDetailForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = 2304524395803498502L;

  /** PK */
  @NotNull(groups = Delete.class)
  @DecimalMin(value = "1", groups = Delete.class)
  private Long permRoleId;

  /** 権限ロール名 */
  @NotBlank
  @IllegalCharacters
  @Size(max = 20)
  private String permRoleName;

  /** 詳細権限リスト */
  @Valid
  private List<PermissionsMenuDto> topMenuList;

  /** 画面モード */
  private DispMode dispMode;

  /**
   * 権限ロール Entity取得
   *
   * @return 権限ロール Entity
   */
  public PermissionRolesEntity getPermissionRolesEntity() {
    PermissionRolesEntity permRoleEntity = new PermissionRolesEntity();
    permRoleEntity.setPermRoleId(this.permRoleId);
    permRoleEntity.setPermRoleName(this.permRoleName);
    return permRoleEntity;
  }

  /**
   * 権限ロール詳細 Entityリスト取得
   *
   * @return 権限ロール詳細 Entityリスト
   */
  public List<PermissionRoleDetailsEntity> getPermissionRolesDetailsEntityList() {
    List<PermissionRoleDetailsEntity> list = new ArrayList<>();

    for (PermissionsMenuDto top : topMenuList) {
      if (Objects.isNull(top.getSubMenuList())) {
        continue;
      }
      for (PermissionRoleDetailsDto sub : top.getSubMenuList()) {
        // 参照権限がある場合
        if (sub.getReadPermFlg().equals(1)) {
          list.add(sub.getPermissionRoleDetailsEntity());
        }
      }
    }

    return list;
  }
}
