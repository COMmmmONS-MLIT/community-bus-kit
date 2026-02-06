package maint.dto.operation.importBusServices;

import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;

/**
 * 系統停留所の時系列整合性チェック用DTO
 *
 * @author JEKAL.H
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StopSequenceNodeDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -932874892375L;

  /** 便ダイヤ改正ID（文字列） */
  private String daiyaRevisionId;
  /** 便番号（Excel上の便ID） */
  private String busServiceTripId;
  /** 系統ID */
  private String lineRouteId;
  /** 系統停留所ID */
  private String lineRouteBusStopId;
  /** 到着時刻（分換算済み。例: "0930" -> 570） */
  private int busStopArrivalTime;
  /** 出発時刻（分換算済み） */
  private int busStopDepartureTime;
  /** 運行順序（系統停留所の並び順） */
  private int busStopSeq;
  /** 元Excelの行番号（エラーメッセージ表示用） */
  private int rowNo;

  /**
   * コンストラクタ
   *
   * @param daiyaRevisionId      便ダイヤ改正ID
   * @param lineRouteId          系統ID
   * @param busServiceTripId     便番号（Excel上の便ID）
   * @param lineRouteBusStopId   系統停留所ID
   * @param busStopArrivalTime   到着時刻（分換算）
   * @param busStopDepartureTime 出発時刻（分換算）
   * @param busStopSeq           運行順序
   * @param rowNo                Excel上の行番号
   */
  public StopSequenceNodeDto(String daiyaRevisionId, String lineRouteId, String busServiceTripId,
      String lineRouteBusStopId, int busStopArrivalTime, int busStopDepartureTime, int busStopSeq,
      int rowNo) {
    this.daiyaRevisionId = daiyaRevisionId;
    this.lineRouteId = lineRouteId;
    this.busServiceTripId = busServiceTripId;
    this.lineRouteBusStopId = lineRouteBusStopId;
    this.busStopArrivalTime = busStopArrivalTime;
    this.busStopDepartureTime = busStopDepartureTime;
    this.busStopSeq = busStopSeq;
    this.rowNo = rowNo;
  }
}
