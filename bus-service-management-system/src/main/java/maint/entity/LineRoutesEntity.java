package maint.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系統Entity
 *
 * @author y.kim
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineRoutesEntity extends BaseEntity {

  /** 系統ID */
  private Long lineRouteId;

  /** 路線ID */
  private Long rosenId;

  /** 系統コード */
  private String lineRouteCode;

  /** 利用開始年月日 */
  private String serviceStartDate;

  /** 利用終了年月日 */
  private String serviceEndDate;

  /** 系統名称 */
  private String lineRouteName;

  /** 系統番号 */
  private String lineRouteShortName;

  /** 系統番号名称 */
  private String lineRouteNumber;

  /** 系統番号名称 英語 */
  private String lineRouteNumberEn;

  /** 往路、復路区分 */
  private Integer lineDetailServiceDivision;

  /** 主な経由地 */
  private String mainStopoverPoint;

  /** 系統URL */
  private String lineRouteUrl;

  /** 系統背景色 */
  private String lineRouteColor;

  /** 系統文字色 */
  private String lineRouteTextColor;

  /** 系統備考 */
  private String lineRouteRemarks;

  /** 利用可否区分 */
  private Integer availableFlag;

}
