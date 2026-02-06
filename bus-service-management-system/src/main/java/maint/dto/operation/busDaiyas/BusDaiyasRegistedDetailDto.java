package maint.dto.operation.busDaiyas;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;
import maint.utils.ExRegUtils;
import maint.validation.annonation.IllegalCharacters;

/**
 * 登録済み便ダイヤ明細情報取得用DTO
 *
 * @author Yuta Ishizaka
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusDaiyasRegistedDetailDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 1111111111111111119L;

  /** 便ダイヤ明細ID */
  private Long busDaiyaId;
  /** 系統ID */
  private Long lineRouteId;
  /** 路線停留所ID */
  private Long rosenBusStopId;
  /** 系統停留所ID */
  private Long lineRouteBusStopId;
  /** 到着時刻 */
  @Pattern(regexp = ExRegUtils.timeHHmmReg, message = "{validation.busdaiyas.register.excel.busstoparrivaltime.pattern}")
  private String busStopArrivalTime;
  /** 出発時刻 */
  @Pattern(regexp = ExRegUtils.timeHHmmReg, message = "{validation.busdaiyas.register.excel.busstopdeparturetime.pattern}")
  private String busStopDepartureTime;
  /** スキップフラグ 0:系統停留所 1:スキップ */
  private Integer skipBusStopFlag;
}
