package maint.dto.operation.lineCharges;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 運賃一覧取得DTO
 *
 * @author g.ko
 */
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class LineChargeDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -49182700794L;

  /** 運賃ID */
  private Long lineChargeId;

  /** 運賃改定ID */
  private Long chargeRevisionId;

  /** 路線ID */
  private Long rosenId;

  /** 路線名 */
  private String rosenName;

  /** 運賃改定開始日 */
  private String serviceStartDate;

  /** 運賃改定終了日 */
  private String serviceEndDate;

  /** 運賃改定名称 */
  private String revisionReason;

  /** 路線の停留所の数 */
  private Integer busStopCount;

}
