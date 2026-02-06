package maint.dto.system.permissions;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

import java.util.List;

/**
 * 権限管理メニューDTO
 *
 * @author y.kim
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PermissionsMenuDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = -2847923847298089170L;

  /** メニュー名 */
  private String menuName;

  /** メニューコード */
  private String menuCode;

  /** 表示順 */
  private Integer displayOrder;

  /** Subメニュー権限リスト */
  @Valid
  private List<PermissionRoleDetailsDto> subMenuList;
}
