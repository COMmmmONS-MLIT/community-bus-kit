package maint.dto.planDaiya.vehicles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;

/**
 * 車両情報DTO（SELECT用）
 *
 * @author g.ko
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VehicleOptionsDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = -54327653775L;

  /** 車両ID */
  private Long vehicleId;

  /** 車両名 */
  private String vehicleName;

}
