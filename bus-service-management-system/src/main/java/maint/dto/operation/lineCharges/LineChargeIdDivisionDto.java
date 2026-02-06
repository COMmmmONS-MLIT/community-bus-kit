package maint.dto.operation.lineCharges;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 運賃ID・区分DTO
 *
 * @author g.ko
 */
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class LineChargeIdDivisionDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -4635367823492L;

  /** 運賃ID */
  private Long lineChargeId;

  /** 運賃区分 */
  private Integer lineChargeDivision;

}
