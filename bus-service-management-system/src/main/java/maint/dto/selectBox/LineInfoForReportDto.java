package maint.dto.selectBox;

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
public class LineInfoForReportDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = 4678756046434606299L;

  /** 便ダイヤ改正開始日 */
  private String serviceStartDate;

  /** 路線ID */
  private String rosenId;

  /** 路線名 */
  private String rosenName;

  /** 事業者ID */
  private String businessCorpId;

  /** 便ダイヤ改正ID */
  private Long daiyaRevisionId;

}
