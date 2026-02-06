package maint.form.agencies;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.entity.AgenciesEntity;
import maint.enums.DispMode;
import maint.form.BaseForm;
import maint.utils.ExObjectUtils;
import maint.utils.ExRegUtils;
import maint.validation.annonation.IllegalCharacters;

/**
 * 事業者詳細FORM
 *
 * @author y.kim
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AgenciesDetailForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = 1111124395803498502L;

  /** PK */
  private Long businessCorpId;

  /** 事業者名・自治体名 */
  @NotBlank
  @IllegalCharacters
  @Size(max = 100)
  private String name;

  /** 事業者名・自治体表示名 */
  @NotBlank
  @IllegalCharacters
  @Size(max = 100)
  private String dispName;

  /** 事業者名・自治体名 かな */
  @IllegalCharacters
  @Size(max = 100)
  private String kanaName;

  /** 事業者名・自治体名 英語 */
  @IllegalCharacters
  @Size(max = 200)
  private String enName;

  /** 部署名 */
  @IllegalCharacters
  @Size(max = 100)
  private String departmentName;

  /** 担当者名 */
  @IllegalCharacters
  @Size(max = 50)
  private String user;

  /** 電話番号（数値、「-」のみ。任意） */
  @Pattern(regexp = ExRegUtils.phoneNumber, message = "{validation.common.error.tel}")
  @Size(max = 15)
  private String phoneNumber;

  /** 連絡先メール */
  @Email
  @Size(max = 256)
  private String mailAddress;

  /** 路線図、時刻表URL */
  @NotBlank
  @Size(max = 256)
  private String timeTableUrl;

  /** 運賃情報URL */
  @Size(max = 256)
  private String fareUrl;

  /** 法人番号（13桁数値、文字列として扱う） */
  @NotBlank
  @Pattern(regexp = ExRegUtils.operationCompanyNo, message = "{validation.agency.error.operationCompanyNo}")
  private String operationCompanyNo;

  /** 交通種別 */
  @Pattern(regexp = ExRegUtils.transportationType, message = "{validation.agency.error.transportationType}")
  private String transportationType;

  /** 提供組織名 */
  @NotBlank
  @IllegalCharacters
  @Size(max = 256)
  private String providerOrganizationName;

  /** 提供組織URL */
  @NotBlank
  @Size(max = 256)
  private String providerOrganizationUrl;

  /** 提供組織連絡先メール */
  @Email
  @Size(max = 256)
  private String providerOrganizationContactEmail;

  /** 提供組織連絡先URL */
  @Size(max = 256)
  private String providerOrganizationContactUrl;

  /** 利用区分（0: 利用不可, 1: 利用中） */
  @NotNull
  @Min(0)
  @Max(1)
  private Integer availableFlag;

  /** 画面モード */
  private DispMode dispMode;

  /** アクセスキー */
  private String accessKey;

  /**
   * 事業者ロール Entity取得
   *
   * @return 事業者ロール Entity
   */
  public AgenciesEntity getAgenciesEntity() {
    AgenciesEntity agenciesEntity = new AgenciesEntity();
    agenciesEntity.setBusinessCorpId(this.businessCorpId);
    agenciesEntity.setName(this.name);
    agenciesEntity.setDispName(this.dispName);
    agenciesEntity.setKanaName(this.kanaName);
    agenciesEntity.setEnName(this.enName);
    agenciesEntity.setDepartmentName(this.departmentName);
    agenciesEntity.setUser(this.user);
    agenciesEntity.setPhoneNumber(this.phoneNumber);
    agenciesEntity.setMailAddress(this.mailAddress);
    agenciesEntity.setTimeTableUrl(this.timeTableUrl);
    agenciesEntity.setFareUrl(this.fareUrl);
    agenciesEntity.setOperationCompanyNo(this.operationCompanyNo);
    agenciesEntity.setTransportationType(ExObjectUtils.isNotBlank(this.transportationType) ?
        Integer.valueOf(this.transportationType) :
        null);
    agenciesEntity.setProviderOrganizationName(this.providerOrganizationName);
    agenciesEntity.setProviderOrganizationUrl(this.providerOrganizationUrl);
    agenciesEntity.setProviderOrganizationContactEmail(this.providerOrganizationContactEmail);
    agenciesEntity.setProviderOrganizationContactUrl(this.providerOrganizationContactUrl);
    agenciesEntity.setAvailableFlag(this.availableFlag);
    return agenciesEntity;
  }

  /**
   * Entityから値セット
   *
   * @param agenciesEntity 事業者ロール Entity
   */
  public void setAgenciesEntity(AgenciesEntity agenciesEntity) {
    this.businessCorpId = agenciesEntity.getBusinessCorpId();
    this.name = agenciesEntity.getName();
    this.dispName = agenciesEntity.getDispName();
    this.kanaName = agenciesEntity.getKanaName();
    this.enName = agenciesEntity.getEnName();
    this.departmentName = agenciesEntity.getDepartmentName();
    this.user = agenciesEntity.getUser();
    this.phoneNumber = agenciesEntity.getPhoneNumber();
    this.mailAddress = agenciesEntity.getMailAddress();
    this.timeTableUrl = agenciesEntity.getTimeTableUrl();
    this.fareUrl = agenciesEntity.getFareUrl();
    this.operationCompanyNo = agenciesEntity.getOperationCompanyNo();
    this.transportationType = ExObjectUtils.isNotBlank(agenciesEntity.getTransportationType()) ?
        agenciesEntity.getTransportationType().toString() :
        null;
    this.providerOrganizationName = agenciesEntity.getProviderOrganizationName();
    this.providerOrganizationUrl = agenciesEntity.getProviderOrganizationUrl();
    this.providerOrganizationContactEmail = agenciesEntity.getProviderOrganizationContactEmail();
    this.providerOrganizationContactUrl = agenciesEntity.getProviderOrganizationContactUrl();
    this.availableFlag = agenciesEntity.getAvailableFlag();
    this.accessKey = agenciesEntity.getAccessKey();
  }
}
