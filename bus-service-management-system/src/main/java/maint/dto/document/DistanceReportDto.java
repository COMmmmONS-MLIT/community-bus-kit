package maint.dto.document;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * キロ程表出力DTO
 *
 * @author y.kim
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DistanceReportDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 33451234507598708L;

  /** 系統名（往路）*/
  private String lineRouteName;

  /** 系統順番（往路）*/
  private String stopSequence;

  /** 停留所名 */
  private String busStopName;

  /** 往路住所 */
  private String outboundAddress;

  /** 復路住所 */
  private String inboundAddress;

  /** 往路キロ */
  private Integer outboundShapeDistTraveled;

}
