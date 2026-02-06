package maint.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import maint.dto.UserPermissionDto;
import maint.entity.PermissionRoleDetailsEntity;
import maint.entity.PermissionRolesEntity;
import maint.session.UserSession;

import java.util.List;

/**
 * 権限詳細MAPPER
 *
 * @author y.kim
 */

@Mapper
public interface PermissionRoleDetailsMapper {

  /**
   * 該当権限ロールIDの権限リスト取得
   *
   * @param permRoleId 権限ロールID
   * @return 権限リスト
   */
  List<UserPermissionDto> getPermissionListByPk(Long permRoleId);

  /**
   * 権限ロール詳細登録
   *
   * @param list        権限ロール詳細 Entityリスト
   * @param userSession UserSession
   * @return 登録件数
   */
  int insert(@Param("list") List<PermissionRoleDetailsEntity> list,
      @Param("session") UserSession userSession);

  /**
   * 権限ロール詳細削除
   *
   * @param entity 権限ロール詳細 Entity
   * @return 削除件数
   */
  int deleteByPermRoleId(PermissionRoleDetailsEntity entity);

  /**
   * 権限ロール詳細削除
   *
   * @param entity 権限ロール詳細 Entity
   * @return 削除件数
   */
  int delete(PermissionRolesEntity entity);
}
