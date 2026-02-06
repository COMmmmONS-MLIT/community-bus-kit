package maint.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 路線停留所Entity
 *
 * @author y.kim
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineBusStopsEntity extends BaseEntity {

  /** 路線停留所ID (PK) */
  private Long rosenBusStopId;

  /** 路線ID */
  private Long rosenId;

  /** 停留所ID */
  private Long busStopId;

  /** 乗降区分 (0：乗車、1：降車、2：乗降車) */
  private Integer rideAlightDivision;

  /** 運行順 */
  private Integer sequenceNo;

  /** 作成ユーザ */
  private String createdUser;

  /** 更新ユーザ */
  private String updatedUser;

}
