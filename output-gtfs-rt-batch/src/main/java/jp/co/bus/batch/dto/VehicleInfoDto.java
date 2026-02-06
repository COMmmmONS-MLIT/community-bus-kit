package jp.co.bus.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 車両情報DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VehicleInfoDto {

  /** 車両ID */
  private Long vehicleId;

  /** 車両名称 */
  private String vehicleName;

  /** ＧＰＳトラッカーシリアル */
  private String gpsSerial;

}
