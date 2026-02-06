package maint.dto.planDaiya.vehicles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 車両情報DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VehiclesDetailDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = -1582356852203907207L;

  /** 車両ID */
  private Long vehicleId;
  /** 運行会社ID */
  private Long operatingCompanyId;
  /** 運行会社名 */
  private String operatingName;
  /** 車両名 */
  private String vehicleName;
  /** GPSトラッカーシリアル */
  private String gpsSerial;
  /** 利用可否区分 */
  private Integer availableFlag;
  /** 日本測地変換フラグ */
  private Integer japanPositioningFlag;
}
