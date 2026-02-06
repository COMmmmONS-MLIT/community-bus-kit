package maint.dto.operation.busDaiyas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 便ダイヤ明細に使用する路線停留所情報用DTO
 *
 * @author Yuta Ishizaka
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusDaiyasLineBusStopDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 1111111111111111116L;

  /** 路線停留所ID */
  private Integer rosenBusStopId;
  /** 運行順 */
  private Long sequenceNo;
  /** 停留所名称 */
  private String busStopName;
  /** 乗降区分 */
  private Integer rideAlightDivision;
}
