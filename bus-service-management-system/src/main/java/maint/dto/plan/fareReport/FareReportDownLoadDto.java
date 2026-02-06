package maint.dto.plan.fareReport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 運賃表出力ダウンロード用DTO
 *
 * @author JEKAL.HUN
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FareReportDownLoadDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 2572143723305543357L;

  /** 路線名 */
  private String rosenName;
  /** 停留所名 From */
  private String busStopNameFrom;
  /** 停留所名 To */
  private String busStopNameTo;
  /** 運賃 */
  private String charge;
  /** 運行順序 From */
  private String sequenceNoFrom;
  /** 運行順序 To */
  private String sequenceNoTo;
}
