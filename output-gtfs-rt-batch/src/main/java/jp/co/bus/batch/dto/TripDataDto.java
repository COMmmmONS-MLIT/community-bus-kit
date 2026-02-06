package jp.co.bus.batch.dto;

import jp.co.bus.batch.utils.ExObjectUtils;
import jp.co.bus.batch.utils.date.ExDateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 便データDTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TripDataDto {

  /** 便ID */
  private Long busServiceId;

  /** 便番号 */
  private String busServiceTripId;

  /** 停留所コード */
  private String busStopCode;

  /** 標柱枝番 */
  private String signpostBranchNumber;

  /** 運行順序 */
  private Integer stopSequence;

  /** 標柱ID */
  private Long signpostId;

  /** 到着予定時刻 */
  private String scheduledArrivalTime;

  /** 出発予定時刻 */
  private String scheduledDepartureTime;

  /** 到着実績時刻 */
  private String actualArrivalTime;

  /** 出発実績時刻 */
  private String actualDepartureTime;

  /** 到着遅延（秒） */
  private Integer arrivalDelay;

  /** 出発遅延（秒） */
  private Integer departureDelay;

  /** 車いす利用区分 */
  private Integer wheelchairAccessibleType;

  /** 緯度 */
  private Float latitude;

  /** 経度 */
  private Float longitude;

  /** 日付フォーマット文字列（予定時刻の変換用） */
  private static final DateTimeFormatter DATE_FORMAT_SCHEDULED = DateTimeFormatter.ofPattern("uuuu-MM-dd HHmmX");
  /** 日付フォーマット文字列（実績時刻の変換用） */
  private static final DateTimeFormatter DATE_FORMAT_ACTUAL = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ssX");

  /**
   * 到着時刻（予定）をOffsetDateTime型に変換して取得する
   * @return 到着時刻（予定）
   */
  public OffsetDateTime getScheduledArrivalTimeParsed() {
    return ExObjectUtils.isBlank(scheduledArrivalTime) ? null : OffsetDateTime.parse(String.format("%s %s+09", ExDateUtils.getCurrentDate(), scheduledArrivalTime), DATE_FORMAT_SCHEDULED);
  }

  /**
   * 出発時刻（予定）をOffsetDateTime型に変換して取得する
   * @return 出発時刻（予定）
   */
  public OffsetDateTime getScheduledDepartureTimeParsed() {
    return ExObjectUtils.isBlank(scheduledDepartureTime) ? null : OffsetDateTime.parse(String.format("%s %s+09", ExDateUtils.getCurrentDate(), scheduledDepartureTime), DATE_FORMAT_SCHEDULED);
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
}
