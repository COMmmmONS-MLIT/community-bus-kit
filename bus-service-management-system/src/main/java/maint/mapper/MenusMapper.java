package maint.mapper;

import org.apache.ibatis.annotations.Mapper;
import maint.dto.system.permissions.PermissionRoleDetailsDto;

import java.util.List;

/**
 * メニューMAPPER
 *
 * @author y.kim
 */

@Mapper
public interface MenusMapper {

  /**
   * メニュー取得
   *
   * @param permRoleId 権限ID
   * @return メニュー権限リスト
   */
  List<PermissionRoleDetailsDto> getMenus(Long permRoleId);
}
