package maint.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import maint.dto.system.permissions.SearchPermissionsDto;
import maint.entity.PermissionRolesEntity;

import java.util.List;

/**
 * 権限MAPPER
 *
 * @author y.kim
 */

@Mapper
public interface PermissionRolesMapper {

  /**
   * 権限名取得
   *
   * @param permissionRolesId 権限ロールID
   * @return 権限名
   */
  String getPermissionRolesNameByPk(Long permissionRolesId);

  /**
   * 権限ロールリスト取得
   *
   * @return 権限ロールリスト
   */
  List<PermissionRolesEntity> getPermissionRoles();

  /**
   * 権限ロール件数取得(Page用)
   *
   * @param dto 権限検索DTO
   * @return 権限ロール件数
   */
  Long countPermissionRoles(SearchPermissionsDto dto);

  /**
   * 権限ロールリスト取得(Page用)
   *
   * @param dto 権限検索DTO
   * @return 権限ロールリスト
   */
  List<PermissionRolesEntity> getPermissionRolesList(SearchPermissionsDto dto);

  /**
   * 権限名重複チェック
   *
   * @param permRoleId 権限ロールID
   * @param permRoleName     権限名
   * @return true : 重複有り、false : 重複無し
   */
  boolean isDuplePermRoleName(@Param("permRoleId") Long permRoleId,
      @Param("permRoleName") String permRoleName);

  /**
   * 権限ロール登録
   *
   * @param entity 権限ロール Entity
   * @return 登録件数
   */
  int insert(PermissionRolesEntity entity);

  /**
   * 権限ロール変更
   *
   * @param entity 権限ロール Entity
   * @return 変更件数
   */
  int update(PermissionRolesEntity entity);

  /**
   * 論理削除
   *
   * @param permissionRolesEntity 権限ロール Entity
   * @return 変更数
   */
  int delete(PermissionRolesEntity permissionRolesEntity);

  /**
   * 該当権限を使用しているユーザが存在するかチェック
   *
   * @param permRoleId 権限ロールID
   * @return 件数
   */
  int countUsedPermissionRoles(Long permRoleId);

}

