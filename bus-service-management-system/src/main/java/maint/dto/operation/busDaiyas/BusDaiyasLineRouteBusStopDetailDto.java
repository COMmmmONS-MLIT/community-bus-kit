package maint.dto.operation.busDaiyas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 路線停留所と付随する系統低留保取得用DTO
 *
 * @author Yuta Ishizaka
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusDaiyasLineRouteBusStopDetailDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 1111111111111111121L;

  /** 系統停留所ID */
  private Long lineRouteBusStpId;
}
