package maint.dto.operation.busDaiyas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

import java.util.List;

/**
 * 路線停留所と付随する系統低留保取得用DTO
 *
 * @author Yuta Ishizaka
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusDaiyasLineRouteBusStopDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 1111111111111111120L;

  /** 系統ID */
  private Long lineRouteId;
  /** 系統停留所ID */
  private Long lineRouteBusStopId;
  /** 路線停留所ID */
  private Long rosenBusStopId;

  /** 系統停留所リスト */
  List<BusDaiyasLineRouteBusStopDetailDto> detailList;
}
