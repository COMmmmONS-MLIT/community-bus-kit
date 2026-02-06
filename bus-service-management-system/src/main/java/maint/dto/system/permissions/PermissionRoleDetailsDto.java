package maint.dto.system.permissions;

import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;
import maint.entity.PermissionRoleDetailsEntity;

/**
 * 権限管理詳細DTO
 *
 * @author y.kim
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PermissionRoleDetailsDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -2813091830192380198L;

  /** メニューID */
  private Long menuId;

  /** メニュー名 */
  private String menuName;

  /** メニューコード */
  private String menuCode;

  /** 権限ID */
  private Long permRoleId;

  /** 権限名 */
  private String permRoleName;

  /** 参照権限 */
  private Integer readPermFlg;

  /** 書込権限 */
  private Integer writePermFlg;

  /** DL権限 */
  private Integer downloadPermFlg;

  /** 表示順 */
  private Integer displayOrder;

  /**
   * 権限読込チェック
   *
   * @return チェック結果
   */
  @AssertTrue(message = "{validation.permissions.sub.permissions.invalid.message}")
  public boolean isInvalidPerm() {
    return ((writePermFlg == null || writePermFlg.equals(
        0)) && (downloadPermFlg == null || downloadPermFlg.equals(
        0))) || (Integer.valueOf(1).equals(readPermFlg));
  }

  /**
   * 権限ロール詳細 Entity取得
   *
   * @return 権限ロール詳細 Entity
   */
  public PermissionRoleDetailsEntity getPermissionRoleDetailsEntity() {
    PermissionRoleDetailsEntity entity = new PermissionRoleDetailsEntity();
    entity.setMenuCode(this.menuCode);
    entity.setPermRoleId(this.permRoleId);
    entity.setWritePermFlg(this.writePermFlg == null ? 0 : this.writePermFlg);
    entity.setDownloadPermFlg(this.downloadPermFlg == null ? 0 : this.downloadPermFlg);
    entity.setReadPermFlg(this.readPermFlg == null ? 0 : this.readPermFlg);
    return entity;
  }
}
