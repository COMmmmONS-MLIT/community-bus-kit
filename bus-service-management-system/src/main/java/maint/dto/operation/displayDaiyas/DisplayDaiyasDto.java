package maint.dto.operation.displayDaiyas;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 棒ダイヤDTO
 *
 * @author y.kim
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DisplayDaiyasDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 33457923123458708L;

  /** 仕業番号 */
  private String workNumber;

  /** 運行ダイヤ_始発出発時刻 */
  private String busStopDepartureTime;

  /** 運行ダイヤ_最終到着時刻 */
  private String busStopArrivalTime;

  /** 運休フラグ */
  private int salesCancelFlg;
}
