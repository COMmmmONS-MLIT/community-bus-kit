package communitybus.lambda.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 運行状況管理用コンテナ DTO
 *
 * @author jekal
 *
 */
@Data
public class OperationStatusContainerDto implements Serializable {

  private static final long serialVersionUID = 8120158514745494192L;

  /** 便ID */
  private Integer busServiceId;

  /** 運行順序 */
  private Integer operationOrder;

  /** 標柱ID */
  private Integer signpostId;

  /** 停留所コード */
  private String stopCode;

  /** 標柱枝番 */
  private String signpostBranch;

  /** 到着判定緯度・経度 */
  private String arrivalJudgeLatitudeLongitude;

  /** 到着判定半径 */
  private Integer arrivalPointRadius;

  /** 到着時刻（予定） */
  private OffsetDateTime scheduledArrivalTime;

  /** 出発時刻（予定） */
  private OffsetDateTime scheduledDepartureTime;

  /** 到着時刻（実績） */
  private OffsetDateTime actualArrivalTime;

  /** 出発時刻（実績） */
  private OffsetDateTime actualDepartureTime;

  /** 到着遅延時間（秒） */
  private Long arrivalDelaySeconds;

  /** 出発遅延時間（秒） */
  private Long departureDelaySeconds;

  /** 新規到着区分 */
  private Integer vehicleTrackingArrivalDepartureDivision = null;

}
