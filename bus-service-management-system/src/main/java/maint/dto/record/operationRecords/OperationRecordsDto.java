package maint.dto.record.operationRecords;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 運行実績DTO
 *
 * @author g.ko
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OperationRecordsDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 542725427264L;

  /** 系統番号 */
  private String lineRouteNo;

  /** 起点 */
  private String origin;

  /** 主な経由地 */
  private String mainViaStops;

  /** 終点 */
  private String destination;

  /** 始発時刻 */
  private String firstDepartureTime;

  /** 終発時刻 */
  private String lastDepartureTime;

  /** 所要時間(分) */
  private String durationMinutes;

  /** 運行回数 */
  private String operationCount;

  /** 走行キロ */
  private String distanceKm;

}
