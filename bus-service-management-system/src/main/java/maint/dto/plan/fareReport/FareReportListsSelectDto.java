package maint.dto.plan.fareReport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 運賃改定DTO（SELECT用）
 *
 * @author jeon
 */
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class FareReportListsSelectDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -2765022517547082381L;

  /** 運賃改定開始日 */
  private String serviceStartDate;

  /** 路線ID */
  private Long rosenId;

  /** 運賃改定ID */
  private Long chargeRevisionId;
}
