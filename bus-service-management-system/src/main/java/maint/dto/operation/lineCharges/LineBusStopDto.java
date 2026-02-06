package maint.dto.operation.lineCharges;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 路線停留所DTO
 *
 * @author g.ko
 */
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class LineBusStopDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -40584238275L;

  /** 路線停留所ID */
  private Long rosenBusStopId;

  /** 路線ID */
  private Long rosenId;

  /** 停留所ID */
  private Long busStopId;

  /** 乗降区分 */
  private Integer rideAlightDivision;

  /** 運行順 */
  private Long sequenceNo;

}
