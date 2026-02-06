package maint.dto.operation.busDaiyas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 路線停留所と付随する系統低留保取得用DTO
 *
 * @author Yuta Ishizaka
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusDaiyasRosenBusStopDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 1111111111111111117L;

  /** 路線停留所ID */
  private Long rosenBusStopId;
  /** 運行順 */
  private Long sequenceNo;
  /** 停留所名称 */
  private String busStopName;
  /** 乗降区分 */
  private Integer rideAlightDivision;
}
