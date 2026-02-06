package maint.dto.operation.vehicleTrackings;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 車両動態位置情報DTO
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VehicleTrackingLocationDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 5438957432000L;

  /** 車両動態ID */
  private Long vehicleTrackingId;

  /** 送信日時 */
  private String createdDatetime;

  /** 緯度・経度 */
  private String latitudeLongitude;

}
