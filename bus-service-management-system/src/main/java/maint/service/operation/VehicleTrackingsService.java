package maint.service.operation;

import maint.dto.operation.vehicleTrackings.VehicleTrackingLocationDto;
import maint.mapper.VehiclesMapper;
import maint.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 車両動態SERVICE
 *
 * @author g.ko
 */
@Service
public class VehicleTrackingsService extends BaseService {

  /** 車両MAPPER */
  @Autowired
  VehiclesMapper vehiclesMapper;

  /**
   * 車両動態取得
   *
   * @param vehicleId 車両ID
   * @return 車両動態位置情報DTO
   */
  public VehicleTrackingLocationDto getVehicleTracking(Long vehicleId) {
    return vehiclesMapper.getLatestVehicleTracking(vehicleId,
        getUserSession());
  }

}
