package maint.dto.operation.importBusServices;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 2-2) 便登録用 DTO 必須: daiyaRevisionId, calendarId, (ユニーク判定用) excelTripId 便ID(tripId)는 DBシーケンスで採番한다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ImportBusServicesInsertDto extends BaseDto {

  private static final long serialVersionUID = 7154592065411175572L;


  /** 事業者ID */
  private Long businessCorpId;

  /** ダイヤ改正ID */
  private Long daiyaRevisionId;

  /** 系統ID */
  private Long lineRouteId;

  /** 便番号 */
  private String busServiceTripId;

  /** 便名称 */
  private String busServiceName;

  /** 便ID */
  private Long busServiceId;

  /** 運行カレンダーID */
  private Integer calendarId;

  /** 系統停留所ID */
  private Integer lineRouteBusStopId;

  /** 到着時刻 */
  private String busStopArrivalTime;

  /** 出発時刻 */
  private String busStopDepartureTime;

  /** 削除フラグ（0:未削除/1:削除） */
  private Integer delFlag;

  /** 作成者 */
  private String createdUser;

  /** 更新者 */
  private String updatedUser;

  /** 便ダイヤ改正開始日 */
  private String serviceStartDate;

  /** 便ダイヤ改正終了日 */
  private String serviceEndDate;

}
