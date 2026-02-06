package maint.dto.operation.lineRoutes;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;
import maint.validation.annonation.IllegalCharacters;

/**
 * 系統停留所DTO
 *
 * @author y.kim
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LineRouteBusStopsDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 33457923407598708L;

  /** 系統停留所ID */
  Long lineRouteBusStopId;

  /** 路線停留所ID */
  @NotNull
  Long rosenBusStopId;

  /** 乗降区分（0: 乗車, 1: 降車, 2: 乗降車） */
  @NotNull
  @Min(0)
  @Max(2)
  Integer rideAlightDivision;

  /** 登録・変更画面ラジオボタン制御用*/
  Integer originalRideAlightDivision;

  /** 変更画面ラジオボタン制御用*/
  Integer lineRouteBusStopsRideAlightDivision;

  /** 変更画面ラジオボタン制御用*/
  Integer dropOffDivision;

  /** 停留所名（表示用） */
  String busStopName;

  /** シーケンス */
  Integer sequenceNo;

  /** 停留所行先 */
  @IllegalCharacters
  @Size(max = 50)
  String stopHeadsign;

  /** 停留所行先 英語 */
  @IllegalCharacters
  @Size(max = 100)
  String stopHeadsignEn;

  /** 通算距離 */
  @Max(9999999)
  Integer shapeDistTraveled;

  /** 標柱ID */
  Long signpostId;

}
