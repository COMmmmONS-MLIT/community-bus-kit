package maint.dto.operation.importBusServices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;

/**
 * 便情報インポート検索DTO
 *
 * @author JEKAL.H
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FindLineRouteStopSequenceDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 9485278490L;

  /** 系統停留所ID */
  private String lineRouteBusStopId;

  /** 系統ID */
  private String lineRouteId;

}
