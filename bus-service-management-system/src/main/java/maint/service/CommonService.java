package maint.service;

import maint.dto.operation.lineRoutes.LineRouteBusStopsDto;
import maint.entity.BusStopsEntity;
import maint.enums.Menu;
import maint.mapper.BusStopMapper;
import maint.mapper.LinesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import maint.entity.PermissionRolesEntity;
import maint.mapper.PermissionRolesMapper;

import java.util.List;

/**
 * 共通SERVICE
 *
 * @author y.kim
 */
@Service
public class CommonService extends BaseService {

  /** 権限 MAPPER */
  @Autowired
  PermissionRolesMapper permissionRolesMapper;

  /** 停留所 MAPPER */
  @Autowired
  BusStopMapper busStopMapper;

  /** 路線 MAPPER */
  @Autowired
  LinesMapper linesMapper;


  /**
   * 権限リスト取得
   *
   * @return 権限一覧
   */
  public List<PermissionRolesEntity> getPermissionRolesList() {
    return permissionRolesMapper.getPermissionRoles();
  }

  /**
   * 利用期間から停留所リスト取得
   *
   * @param serviceStartDate 利用開始日
   * @param serviceEndDate 利用終了日
   * @param businessCorpId 事業者ID
   * @return 停留所リスト
   */
  public List<BusStopsEntity> getBusStopListByServiceDate(String serviceStartDate,
      String serviceEndDate, Long businessCorpId) {
    return busStopMapper.getBusStopListByServiceDate(serviceStartDate, serviceEndDate, businessCorpId);
  }

  /**
   * 路線と利用期間から路線停留所リスト取得
   *
   * @param serviceStartDate 利用開始日
   * @param serviceEndDate 利用終了日
   * @param rosenId 路線ID
   * @return 路線停留所リスト
   */
  public List<LineRouteBusStopsDto> getLineBusStopListByServiceDateAndRosenId(String serviceStartDate,
      String serviceEndDate, Long rosenId) {
    return linesMapper.getLineBusStopListByServiceDateAndRosenId(serviceStartDate, serviceEndDate, rosenId);
  }

  /**
   * MenuのmenuCodeからURIを返す（見つからない場合は "#" を返す）
   * Thymeleafでは @commonService.uri('...') で呼び出せます。
   *
   * @param menuCode MENUCODE
   * @return uri
   */
  public String uri(String menuCode) {
    Menu m = Menu.getByMenuCode(menuCode);
    if (m == null || m.getUri() == null) return "#";
    return m.getUri();
  }

}
