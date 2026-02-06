package maint.dto.record.operatingReport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 路線DTO（SELECT用）
 *
 * @author jeon
 */
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class OperatingReportListsSelectDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -2765022517547082381L;


  /** 便ダイヤ改正開始日 */
  private String serviceStartDate;

  /** 路線ID */
  private String rosenId;

  /** 事業者ID */
  private String businessCorpId;

}
