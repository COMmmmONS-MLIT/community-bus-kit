package maint.dto.planDaiya.chargeRevisions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 運賃改定DTO
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ChargeRevisionDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 75643746578465L;

  /** 運賃改定ID */
  private Long chargeRevisionId;

  /** 運賃改定名称 */
  private String revisionReason;

  /** 運賃改定開始日 */
  private String serviceStartDate;

  /** 路線名 */
  private String rosenName;

}
