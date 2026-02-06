package maint.dto.operation.busDaiyas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 便ダイヤ変更画面の車両selectBox情報取得用DTO
 *
 * @author Yuta Ishizaka
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusDaiyasVehicleOptionDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 1111111111111111114L;

  /** 車両ID */
  private String vehicleId;
  /** 車両名 */
  private String vehicleName;
}
