package maint.dto.operation.busDaiyas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 登録済み便ダイヤ情報取得用DTO
 *
 * @author Yuta Ishizaka
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusDaiyasLineRouteArrDepDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 1111111111111111118L;

  /** 往復区分 */
  private Integer lineDetailServiceDivision;
  /** 系統ID */
  private Long lineRouteId;
  /** 到着時刻 */
  private String busStopArrivalTime;
  /** 出発時刻 */
  private String busStopDepartureTime;
}
