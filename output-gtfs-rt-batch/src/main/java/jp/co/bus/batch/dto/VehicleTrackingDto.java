package jp.co.bus.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 車両動態データ DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VehicleTrackingDto extends BaseDto {

  /** 便ID */
  private Long busServiceId;

  /** 便番号 */
  private String busServiceTripId;

  /** 緯度 */
  private Float latitude;

  /** 経度 */
  private Float longitude;

  /** 車いす利用区分 */
  private Long wheelchairAccessibleType;

  /** 運行年月日 */
  private String serviceDate;

  /** お知らせ */
  private String notification;

  /** 車両ID */
  private Long vehicleId;

  /** 車両名 */
  private String vehicleName;
}
