package maint.dto.operation.busDaiyas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 便ダイヤ情報DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusDaiyasDetailDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 225635973801224214L;

  /** 系統ID */
  private Long lineRouteId;
  /** 路線名 */
  private String rosenName;
  /** 系統名称 */
  private String lineRouteName;
  /** 往路、復路区分 */
  private Integer lineDetailServiceDivision;
  /** 便ダイヤ開始日 */
  private String serviceStartDate;
  /** 便ダイヤ終了日 */
  private String serviceEndDate;
  /** ダイヤ改正名称 */
  private String daiyaRevisionName;
  /** 路線ID */
  private Long rosenId;
  /** 便ダイヤID */
  private Long daiyaRevisionId;
}
