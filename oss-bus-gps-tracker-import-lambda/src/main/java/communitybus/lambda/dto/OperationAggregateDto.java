package communitybus.lambda.dto;

import communitybus.lambda.utils.ExObjectUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 運行情報集約DTO
 *
 * @author jekal
 *
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class OperationAggregateDto extends BaseDto {

  private static final long serialVersionUID = 6826990578424854302L;

  /** 削除フラグ */
  private Integer dailyDelFlag;

  /** 運行年月日 */
  private String dailyServiceDate;

  /** 便休フラグ */
  private Integer dailySalesCancelFlg;

  /** 運行対象外フラグ */
  private Integer dailyOperationCancelFlg;

  /** 車両ID */
  private Integer dailyVehicleId;

  /** 削除フラグ */
  private Integer busDelFlag;

  /** 便ID */
  private Integer busServiceId;

  /** 車両ID */
  private Integer busVehicleId;

  /** 車いす利用区分 */
  private Integer wheelchairAccessibletype;

  /** 削除フラグ */
  private Integer busDetailDelFlag;

  /** 便ID */
  private Integer busDetailBusServiceId;

  /** 到着時刻 */
  private String busStopArrivalTime;

  /** 削除フラグ */
  private Integer lineRouteStopDelFlag;

  /** 系統停留所ID */
  private Integer lineRouteStopId;

  /** 運行順序 */
  private Integer stopSequence;

  /** 削除フラグ */
  private Integer signpostDelFlag;

  /** 利用可否区分 */
  private Short signpostAvailableFlag;

  /** 標柱ID */
  private Integer signpostId;

  /** 緯度、経度 */
  private String latitudeLongitude;

  /** 到着判定緯度、経度 */
  private String arrivalPoint;

  /** 到着判定半径 */
  private Integer arrivalPointRadius;

  /** 標柱枝番 */
  private String signpostBranchNumber;

  /** 到着時刻（予定） */
  private String scheduledArrivalTime;

  /** 出発時刻（予定） */
  private String scheduledDepartureTime;

  /** 到着遅延時間 */
  private Long arrivalDelaySeconds;

  /** 出発遅延時間 */
  private Long departureDelaySeconds;

  /** 到着時刻（実績） */
  private String actualArrivalTime;

  /** 出発時刻（実績） */
  private String actualDepartureTime;

  /** 便ID */
  private Integer vehicleTrackingArrivalBusServiceId;

  /** 標柱ID */
  private Integer vehicleTrackingArrivalSignpostId;

  /** 送信日時 */
  private String vehicleTrackingArrivalCreatedDatetime;

  /** 到着区分 */
  private Integer arrivalDivision;

  /** 便ID */
  private Integer vehicleTrackingDepartureBusServiceId;

  /** 標柱ID */
  private Integer vehicleTrackingDepartureSignpostId;

  /** 送信日時 */
  private String vehicleTrackingDepartureCreatedDatetime;

  /** 停留所コード */
  private String busStopCode;

  /** 便番号 */
  private String busServiceTripId;

  /** 日付フォーマット文字列（予定時刻の変換用） */
  private static final DateTimeFormatter DATE_FORMAT_SCHEDULED = DateTimeFormatter.ofPattern("uuuu-MM-dd HHmmX");
  /** 日付フォーマット文字列（実績時刻の変換用） */
  private static final DateTimeFormatter DATE_FORMAT_ACTUAL = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ssX");

  /**
   * 到着時刻（予定）をOffsetDateTime型に変換して取得する
   * @return 到着時刻（予定）
   */
  public OffsetDateTime getScheduledArrivalTimeParsed() {
    return ExObjectUtils.isBlank(scheduledArrivalTime) ? null : OffsetDateTime.parse(String.format("%s %s+09", dailyServiceDate, scheduledArrivalTime), DATE_FORMAT_SCHEDULED);
  }

  /**
   * 出発時刻（予定）をOffsetDateTime型に変換して取得する
   * @return 出発時刻（予定）
   */
  public OffsetDateTime getScheduledDepartureTimeParsed() {
    return ExObjectUtils.isBlank(scheduledDepartureTime) ? null : OffsetDateTime.parse(String.format("%s %s+09", dailyServiceDate, scheduledDepartureTime), DATE_FORMAT_SCHEDULED);
  }

  /**
   * 到着時刻（実績）をOffsetDateTime型に変換して取得する
   * @return 到着時刻（実績）
   */
  public OffsetDateTime getActualArrivalTimeParsed() {
    return ExObjectUtils.isBlank(actualArrivalTime) ? null : OffsetDateTime.parse(String.format("%s+09", actualArrivalTime), DATE_FORMAT_ACTUAL);
  }

  /**
   * 出発時刻（実績）をOffsetDateTime型に変換して取得する
   * @return 出発時刻（実績）
   */
  public OffsetDateTime getActualDepartureTimeParsed() {
    return ExObjectUtils.isBlank(actualDepartureTime) ? null : OffsetDateTime.parse(String.format("%s+09", actualDepartureTime), DATE_FORMAT_ACTUAL);
  }

  /**
   * 到着判定緯度を取得する
   * @return 到着判定緯度
   */
  public Double getArrivalJudgeLatitude() {
    String pos = ExObjectUtils.isBlank(arrivalPoint) ? latitudeLongitude : arrivalPoint;
    return Double.parseDouble(pos.split(",")[0]);
  }

  /**
   * 到着判定経度を取得する
   * @return 到着判定経度
   */
  public Double getArrivalJudgeLongitude() {
    String pos = ExObjectUtils.isBlank(arrivalPoint) ? latitudeLongitude : arrivalPoint;
    return Double.parseDouble(pos.split(",")[1]);
  }
}


