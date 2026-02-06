package maint.dto.operation.busDaiyas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 便の選択肢DTO
 *
 * @author kawahata
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusServicesOptionDto {

  /** 便ID */
  private Long busServiceId;
  /** 便名称 */
  private String busServiceName;
}
