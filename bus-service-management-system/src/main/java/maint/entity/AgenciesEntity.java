package maint.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 事業者 Entity
 *
 * @author y.kim
 */

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgenciesEntity extends BaseEntity{

  /** Serial version UID */
  private static final long serialVersionUID = 1111186475765876587L;

  /** 事業者ID */
  private Long businessCorpId;

  /** 事業者名・自治体名 */
  private String name;

  /** 事業者名・自治体表示名 */
  private String dispName;

  /** 事業者名・自治体名 かな */
  private String kanaName;

  /** 事業者名・自治体名 英語 */
  private String enName;

  /** 部署名 */
  private String departmentName;

  /** 担当者名 */
  private String user;

  /** 電話番号 */
  private String phoneNumber;

  /** 連絡先メール */
  private String mailAddress;

  /** 路線図、時刻表URL */
  private String timeTableUrl;

  /** 運賃情報URL */
  private String fareUrl;

  /** 法人番号 */
  private String operationCompanyNo;

  /** 交通種別 */
  private Integer transportationType;

  /** 提供組織名 */
  private String providerOrganizationName;

  /** 提供組織URL */
  private String providerOrganizationUrl;

  /** 提供組織連絡先メール */
  private String providerOrganizationContactEmail;

  /** 提供組織連絡先URL */
  private String providerOrganizationContactUrl;

  /** 利用区分 */
  private Integer availableFlag;

  /** アクセスキー */
  private String accessKey;
}
