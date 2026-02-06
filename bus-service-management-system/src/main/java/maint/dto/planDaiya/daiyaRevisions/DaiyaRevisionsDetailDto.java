package maint.dto.planDaiya.daiyaRevisions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 便ダイヤ改正情報DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DaiyaRevisionsDetailDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 225635973801224214L;

  /** 便ダイヤ改正ID */
  private Long daiyaRevisionId;
  /** 路線名 */
  private String rosenName;
  /** ダイヤ改正名称 */
  private String daiyaRevisionName;
  /** 便ダイヤ改正開始日 */
  private String serviceStartDate;
  /** 便ダイヤ改正終了日 */
  private String serviceEndDate;
  /** 路線ID */
  private Long rosenId;

}
