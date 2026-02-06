package maint.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.form.operatingCompanies.OperatingCompaniesDetailForm;

/**
 * 運行会社 Entity
 *
 * @author jeon
 */

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatingCompaniesEntity extends BaseEntity {
  /** Serial version UID */
  private static final long serialVersionUID = -6951896605745862623L;
  /** 運行会社ID */
  Long operatingCompanyId;
  /** 削除フラグ */
  Long delFlag;
  /** 事業者ID */
  Long businessCorId;
  /** 運行会社名 */
  String operatingName;
  /** 電話番号 */
  String phoneNumber;
  /** 連絡先メール */
  String mailAddress;
  /** URL */
  String url;
  /** 利用区分 */
  Integer availableFlag;

  /**
   * コンストラクタ
   *
   * @param form 運行会社詳細FORM
   */
  public OperatingCompaniesEntity(OperatingCompaniesDetailForm form) {
    this.operatingCompanyId = form.getOperatingCompanyId();
    this.delFlag = form.getDelFlag();
    this.businessCorId = form.getBusinessCorpId();
    this.operatingName = form.getOperatingName();
    this.phoneNumber = form.getPhoneNumber();
    this.mailAddress = form.getMailAddress();
    this.url = form.getUrl();
    this.availableFlag = form.getAvailableFlag();
  }
}
