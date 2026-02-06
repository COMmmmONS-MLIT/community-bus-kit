package maint.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 路線Entity
 *
 * @author y.kim
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinesEntity extends BaseEntity {

  /** 路線ID */
  private Long rosenId;

  /** 事業者ID */
  private Long businessCorpId;

  /** 路線コード */
  private String rosenCode;

  /** 利用開始年月日 */
  private String serviceStartDate;

  /** 利用終了年月日 */
  private String serviceEndDate;

  /** 路線名 */
  private String rosenName;

  /** 路線名 かな */
  private String rosenKanaName;

  /** 路線名 英語 */
  private String rosenEnName;

  /** 利用可否区分 */
  private Integer availableFlag;

}
