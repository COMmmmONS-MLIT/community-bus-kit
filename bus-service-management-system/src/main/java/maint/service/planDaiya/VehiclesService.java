package maint.service.planDaiya;

import maint.dto.ListPager;
import maint.dto.planDaiya.vehicles.VehicleOptionsDto;
import maint.dto.planDaiya.vehicles.VehiclesDetailDto;
import maint.dto.planDaiya.vehicles.SearchVehiclesDto;
import maint.entity.VehiclesEntity;
import maint.exception.CommonDbException;
import maint.form.vehicles.VehiclesDetailForm;
import maint.form.vehicles.VehiclesSearchForm;
import maint.mapper.VehiclesMapper;
import maint.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;

/**
 * 車両SERVICE
 *
 * @author jeon
 */

@Service
public class VehiclesService extends BaseService {

  /** 車両 MAPPER */
  @Autowired
  VehiclesMapper vehiclesMapper;

  /**
   *  車両一覧
   *
   * @param form  車両検索DTO
   * @param pager ページャ
   */
  public void setVehiclesList(VehiclesSearchForm form, ListPager pager) {

    SearchVehiclesDto dto = new SearchVehiclesDto(form, pager.getStartIndex());
    // セッションから事業者IDを設定
    dto.setBusinessCorpId(getUserSession().getBusinessCorpId());
    // 車両一覧を取得
    pager.setTotalListSize(vehiclesMapper.countVehicles(dto));
    if (pager.getTotalListSize().compareTo(0L) > 0) {
      pager.setList(vehiclesMapper.getVehiclesBySearch(dto));
    }
  }

  /**
   * 車両登録
   *
   * @param form 車両詳細FORM
   */
  @Transactional
  public void insert(VehiclesDetailForm form) {

    VehiclesEntity vehiclesEntity = new VehiclesEntity(form);
    // 事業者IDを取得
    vehiclesEntity.setBusinessCorpId(getUserSession().getBusinessCorpId());
    // 登録失敗時、エラーとする
    if (vehiclesMapper.registerVehicles(vehiclesEntity) != 1) {
      throw new CommonDbException("【車両管理】登録エラー");
    }
  }

  /**
   * 車両情報取得
   *
   * @param id 車両ID
   * @return 車両情報DTO
   */
  public VehiclesDetailDto getVehiclesByPk(Long id) {
      Long businessCorpId = getUserSession().getBusinessCorpId();
      return vehiclesMapper.getVehiclesByPk(id, businessCorpId);
  }

  /**
   * 全件車両情報取得
   *
   * @return 車両情報検索結果リスト
   */
  public List<VehiclesDetailDto> getAllLinesForSelectOption(Long businessCorpId) {
    if (businessCorpId == null) {
      return Collections.emptyList();
    }
    return vehiclesMapper.getVehiclesAllLinesForSelectOption(businessCorpId);
  }

  /**
   * 車両情報取得（SELECT用）
   *
   * @return 車両情報DTO
   */
  public List<VehicleOptionsDto> getVehicleOptions() {
    return vehiclesMapper.getVehicleOptions(getUserSession());
  }

  /**
   * 車両変更
   *
   * @param form 車両詳細FORM
   */
  @Transactional
  public void update(VehiclesDetailForm form) {

    VehiclesEntity vehiclesEntity = new VehiclesEntity(form);
    // 車両更新
    if (vehiclesMapper.updateVehicles(vehiclesEntity) != 1) {
      throw new CommonDbException("【車両管理】変更エラー");
    }
  }

}

