package maint.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.operation.busDaiyas.BusDaiyasRegistedDetailDto;
import maint.utils.ExObjectUtils;

/**
 * 便ダイヤ明細 Entity
 *
 * @author jeon
 */

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusDaiyasEntity extends BaseEntity {

  /** Serial version UID */
  private static final long serialVersionUID = -1526663810399025848L;

  /** 便ダイヤ明細ID */
  private Long busDaiyaId;

  /** 便ID */
  private Long busServiceId;

  /** 系統停留所ID */
  private Long lineRouteBusStopId;

  /** 到着時刻 */
  private String busStopArrivalTime;

  /** 出発時刻 */
  private String busStopDepartureTime;

  /**
   * コンストラクタ
   *
   * @param dto 登録済み便ダイヤ明細情報取得用DTO
   */
  public BusDaiyasEntity(Long serviceId, BusDaiyasRegistedDetailDto dto) {
    this.busServiceId = serviceId;
    this.lineRouteBusStopId = dto.getLineRouteBusStopId();
    this.busStopArrivalTime = dto.getBusStopArrivalTime();
    this.busStopDepartureTime = dto.getBusStopDepartureTime();
    if (ExObjectUtils.isNotBlank(dto.getBusDaiyaId())) {
      this.busDaiyaId = dto.getBusDaiyaId();
    }
  }

}
