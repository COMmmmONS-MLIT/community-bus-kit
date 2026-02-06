package maint.service.system;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import maint.dto.ListPager;
import maint.dto.system.permissions.PermissionRoleDetailsDto;
import maint.dto.system.permissions.PermissionsMenuDto;
import maint.dto.system.permissions.SearchPermissionsDto;
import maint.entity.PermissionRoleDetailsEntity;
import maint.entity.PermissionRolesEntity;
import maint.enums.Menu;
import maint.exception.CommonDbException;
import maint.form.permissions.PermissionsDetailForm;
import maint.form.permissions.PermissionsSearchForm;
import maint.mapper.MenusMapper;
import maint.mapper.PermissionRoleDetailsMapper;
import maint.mapper.PermissionRolesMapper;
import maint.service.BaseService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 権限SERVICE
 *
 * @author y.kim
 */

@Service
public class PermissionService extends BaseService {

  /** 権限 MAPPER */
  @Autowired
  PermissionRolesMapper permissionRolesMapper;

  /** 権限詳細 MAPPER */
  @Autowired
  PermissionRoleDetailsMapper permissionRoleDetailsMapper;

  /** メニューMAPPER */
  @Autowired
  MenusMapper menusMapper;

  /**
   * 権限一覧リスト取得
   *
   * @param form  権限検索FORM
   * @param pager ListPager
   */
  public void getPermissionList(PermissionsSearchForm form, ListPager pager) {

    SearchPermissionsDto dto = new SearchPermissionsDto(form, pager.getStartIndex());

    pager.setTotalListSize(permissionRolesMapper.countPermissionRoles(dto));

    if (pager.getTotalListSize().compareTo(0L) > 0) {
      pager.setList(permissionRolesMapper.getPermissionRolesList(dto));
    }
  }

  /**
   * 権限名設定
   *
   * @param form 権限詳細FORM
   */
  public void setPermissionName(PermissionsDetailForm form) {
    String permissionName =
        permissionRolesMapper.getPermissionRolesNameByPk(form.getPermRoleId());

    if (StringUtils.isBlank(permissionName)) {
      throw new CommonDbException("【権限管理】権限名設定エラー:" + form);
    }

    form.setPermRoleName(permissionName);
  }

  /**
   * 権限メニューリスト取得
   *
   * @param form 権限詳細FORM
   * @return 権限メニューリスト
   */
  public List<PermissionsMenuDto> getPermissionMenuList(PermissionsDetailForm form) {

    List<PermissionsMenuDto> retList = new ArrayList<>();
    List<PermissionRoleDetailsDto> menuList = menusMapper.getMenus(form.getPermRoleId());

    for (Menu menu : Menu.getAllTopMenuValues()) {
      PermissionsMenuDto topMenu = getTopMenuDtoByMenuCode(menuList, menu);
      topMenu.setSubMenuList(setSubMenuList(menuList, Menu.getAllSubMenu(menu)));
      retList.add(topMenu);
    }

    return retList.stream().sorted(Comparator.comparing(PermissionsMenuDto::getDisplayOrder,
        Comparator.nullsLast(Integer::compareTo))).collect(Collectors.toList());
  }

  /**
   * TOPメニューDTO取得
   *
   * @param menuList メニュー詳細リスト
   * @param menu     TOPメニュー
   * @return TOPメニューDTO
   */
  private PermissionsMenuDto getTopMenuDtoByMenuCode(List<PermissionRoleDetailsDto> menuList,
      Menu menu) {

    for (PermissionRoleDetailsDto dto : menuList) {
      if (dto.getMenuCode().equals(menu.getMenuCode())) {
        return new PermissionsMenuDto(menu.getName(), menu.getMenuCode(), dto.getDisplayOrder(),
            Collections.emptyList());
      }
    }

    return new PermissionsMenuDto(menu.getName(), menu.getMenuCode(), null, Collections.emptyList());
  }

  /**
   * Subメニュー詳細リスト取得
   *
   * @param menuList メニュー詳細リスト
   * @param menus    Subメニュー配列
   * @return Subメニュー詳細リスト
   */
  private List<PermissionRoleDetailsDto> setSubMenuList(List<PermissionRoleDetailsDto> menuList,
      Menu[] menus) {

    List<PermissionRoleDetailsDto> retList = new ArrayList<>();

    for (Menu menu : menus) {
      for (PermissionRoleDetailsDto dto : menuList) {
        if (dto.getMenuCode().equals(menu.getMenuCode())) {
          retList.add(dto);
          break;
        }
      }
    }

    return retList;
  }

  /**
   * 権限名重複チェック
   *
   * @param form 権限詳細FORM
   * @return true : 重複有り、false : 重複無し
   */
  public boolean isDuplePermRoleName(PermissionsDetailForm form) {
    return permissionRolesMapper.isDuplePermRoleName(form.getPermRoleId(),
        form.getPermRoleName());
  }

  /**
   * 権限ロール登録
   *
   * @param form 権限詳細FORM
   */
  @Transactional
  public void insert(PermissionsDetailForm form) {

    PermissionRolesEntity permRoleEntity = form.getPermissionRolesEntity();
    permissionRolesMapper.insert(permRoleEntity);

    if (permRoleEntity.getPermRoleId() == null) {
      throw new CommonDbException("【権限管理】権限ロール登録エラー:" + permRoleEntity);
    }

    List<PermissionRoleDetailsEntity> detailList = form.getPermissionRolesDetailsEntityList();
    detailList.stream().forEach(entity -> {
      entity.setPermRoleId(permRoleEntity.getPermRoleId());
    });

    int insertCnt = permissionRoleDetailsMapper.insert(detailList, getUserSession());

    if (detailList.size() != insertCnt) {
      throw new CommonDbException("【権限管理】権限ロール詳細登録エラー:" + detailList);
    }
  }

  /**
   * 権限ロール変更
   *
   * @param form 権限詳細FORM
   */
  @Transactional
  public void update(PermissionsDetailForm form) {

    PermissionRolesEntity permRoleEntity = form.getPermissionRolesEntity();

    if (permissionRolesMapper.update(permRoleEntity) != 1) {
      throw new CommonDbException("【権限管理】権限ロール変更エラー:" + permRoleEntity.toString());
    }

    List<PermissionRoleDetailsEntity> detailList = form.getPermissionRolesDetailsEntityList();

    detailList.stream().forEach(entity -> {
      entity.setPermRoleId(form.getPermRoleId());
    });

    PermissionRoleDetailsEntity detailEntity = new PermissionRoleDetailsEntity();
    detailEntity.setPermRoleId(form.getPermRoleId());

    if (permissionRoleDetailsMapper.deleteByPermRoleId(detailEntity) <= 0) {
      throw new CommonDbException(
          "【権限管理】権限ロール詳細変更エラー(既存データ削除):" + detailList);
    }

    int insertCnt = permissionRoleDetailsMapper.insert(detailList, getUserSession());

    if (detailList.size() != insertCnt) {
      throw new CommonDbException("【権限管理】権限ロール詳細変更エラー(新規登録):" + detailList);
    }
  }

  /**
   * 権限Validationチェック（最小一つ権限設定必要）
   *
   * @param form 権限詳細FORM
   * @return true:設定権限なし、false:設定権限有り
   */
  public boolean isInvalidPermission(PermissionsDetailForm form) {

    for (PermissionsMenuDto top : form.getTopMenuList()) {
      for (PermissionRoleDetailsDto sub : top.getSubMenuList()) {
        // 最小一つ権限設定必要
        if ((sub.getReadPermFlg() != null && sub.getReadPermFlg()
            .equals(1)) || (sub.getWritePermFlg() != null && sub.getWritePermFlg()
            .equals(1)) || (sub.getDownloadPermFlg() != null && sub.getDownloadPermFlg()
            .equals(1))) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * 論理削除
   *
   * @param form 権限詳細FORM
   */
  @Transactional
  public void delete(PermissionsDetailForm form) {
    // 削除
    PermissionRolesEntity permRoleEntity = form.getPermissionRolesEntity();

    // 権限ロール削除失敗時、エラーとする
    if (permissionRolesMapper.delete(permRoleEntity) != 1) {
      throw new CommonDbException("【権限ロール】削除エラー:" + permRoleEntity.toString());
    }

    // 権限ロール詳細削除失敗時、エラーとする
    if (permissionRoleDetailsMapper.delete(permRoleEntity) == 0) {
      throw new CommonDbException("【権限ロール詳細】削除エラー:" + permRoleEntity.toString());
    }
  }

  /**
   * 該当権限を使用しているユーザが存在するかチェック
   *
   * @param form 権限詳細FORM
   * @return 1件以上取得:true 0件:false
   */
  public boolean isUsedPermissionRoles(PermissionsDetailForm form) {
    // 該当権限を使用しているユーザが存在する場合、エラーとする
    return permissionRolesMapper.countUsedPermissionRoles(form.getPermRoleId()) > 0;
  }

}

