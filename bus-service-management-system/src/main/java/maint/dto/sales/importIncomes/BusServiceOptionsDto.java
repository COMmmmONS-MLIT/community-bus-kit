package maint.dto.sales.importIncomes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;

/**
 * 便オプションDTO（SELECT用）
 *
 * @author g.ko
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusServiceOptionsDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 52943875240897L;

  /** 便ID */
  private Long busServiceId;

  /** 便名 */
  private String busServiceName;

  /** 便番号 */
  private String busServiceTripId;

}
