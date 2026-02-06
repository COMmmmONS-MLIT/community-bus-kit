package maint.mapper;

import maint.dto.planDaiya.vehicles.VehicleOptionsDto;
import maint.dto.planDaiya.vehicles.VehiclesDetailDto;
import maint.dto.planDaiya.vehicles.SearchVehiclesDto;
import maint.dto.operation.vehicleTrackings.VehicleTrackingLocationDto;
import maint.entity.VehiclesEntity;
import maint.session.UserSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 車両MAPPER
 *
 * @author jeon
 */

@Mapper
public interface VehiclesMapper {

  /**
   * 車両一覧取得
   *
   * @param dto 車両検索DTO
   * @return 車両情報検索結果リスト
   */
  List<VehiclesDetailDto> getVehiclesBySearch(SearchVehiclesDto dto);

  /**
   * 車両一覧件数取得
   *
   * @param dto 車両検索DTO
   * @return 全件件数
   */
  Long countVehicles(SearchVehiclesDto dto);

  /**
   * 車両情報取得
   *
   * @param vehicleId 車両ID
   * @param businessCorpId 事業者ID（セッション）
   * @return 車両詳細DTO
   */
  VehiclesDetailDto getVehiclesByPk(@Param("vehicleId") Long vehicleId,
      @Param("businessCorpId") Long businessCorpId);

  /**
   * 車両情報取得
   *
   * @param session ユーザーセッション
   * @return 車両詳細DTO
   */
  List<VehicleOptionsDto> getVehicleOptions(@Param("session") UserSession session);

  /**
   * 最新の車両動態取得
   *
   * @param vehicleId 車両ID
   * @param session ユーザーセッション
   * @return 車両動態位置情報
   */
  VehicleTrackingLocationDto getLatestVehicleTracking(@Param("vehicleId") Long vehicleId,
      @Param("session") UserSession session);

  /**
   * 全件車両情報取得
   *
   * @param businessCorpId 事業者ID（セッション）
   * @return 車両情報検索結果リスト
   */
  List<VehiclesDetailDto> getVehiclesAllLinesForSelectOption(@Param("businessCorpId") Long businessCorpId);

  /**
   * 車両登録
   *
   * @param vehiclesEntity  車両 Entity
   * @return insert件数
   */
  int registerVehicles(VehiclesEntity vehiclesEntity);

  /**
   * 車両変更
   *
   * @param vehiclesEntity  車両 Entity
   * @return 更新件数
   */
  int updateVehicles(VehiclesEntity vehiclesEntity);

}
