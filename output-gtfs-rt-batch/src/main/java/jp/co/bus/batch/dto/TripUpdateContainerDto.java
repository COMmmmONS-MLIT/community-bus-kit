package jp.co.bus.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

/**
 * TripUpdateContainerDto
 *
 * @author jeon
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TripUpdateContainerDto {

  /** 日毎便ダイヤID */
  private Long busServiceId;

  /** 停止順序 */
  private Integer stopSequence;

  /** 標柱ID */
  private Long signpostId;

  /** 予定到着時刻 */
  private OffsetDateTime scheduledArrivalTime;

  /** 予定出発時刻 */
  private OffsetDateTime scheduledDepartureTime;

  /** 実績到着時刻 */
  private OffsetDateTime actualArrivalTime;

  /** 実績出発時刻 */
  private OffsetDateTime actualDepartureTime;

  /** 到着遅延時間（秒） */
  private Long arrivalDelayTime;

  /** 出発遅延時間（秒） */
  private Long departureDelayTime;

  /** 便情報 */
  private TripDataDto tripData;

  /** 車両ID */
  private Long vehicleId;

  /** 車両名 */
  private String vehicleName;

}
