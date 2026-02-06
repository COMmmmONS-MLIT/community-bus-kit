package maint.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系統停留所Entity
 *
 * @author y.kim
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineRouteBusStopsEntity extends BaseEntity {

  /** 系統停留所ID */
  private Long lineRouteBusStopId;

  /** 系統ID */
  private Long lineRouteId;

  /** 路線停留所ID */
  private Long rosenBusStopId;

  /** 標柱ID */
  private Long signpostId;

  /** 乗降区分 (0:乗車、1:乗車不可、2:乗車予約必要、3:ドライバー連絡) */
  private Integer rideAlightDivision;

  /** 下車区分 (0:降車、1:降車不可、2:降車予約必要、3:乗車時にドライバーに連絡) */
  private Integer dropOffDivision;

  /** 停留所行先 */
  private String stopHeadsign;

  /** 停留所行先 英語 */
  private String stopHeadsignEn;

  /** 通算距離（メートル） */
  private Integer shapeDistTraveled;

  /** 運行順序 */
  private Integer stopSequence;

  /** 作成ユーザ */
  private String createdUser;

  /** 更新ユーザ */
  private String updatedUser;

}
