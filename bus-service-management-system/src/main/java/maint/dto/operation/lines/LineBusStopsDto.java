package maint.dto.operation.lines;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 路線停留所DTO
 *
 * @author y.kim
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LineBusStopsDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 33457923407598708L;

  /** 路線停留所ID */
  @NotNull
  Long rosenBusStopId;

  /** 乗降区分（0: 乗車, 1: 降車, 2: 乗降車） */
  @NotNull
  @Min(0)
  @Max(2)
  Integer rideAlightDivision;

  /** 停留所ID */
  @NotNull
  Long busStopId;

  /** 停留所名（表示用） */
  String busStopName;

  /** シーケンス */
  Integer sequenceNo;

}
