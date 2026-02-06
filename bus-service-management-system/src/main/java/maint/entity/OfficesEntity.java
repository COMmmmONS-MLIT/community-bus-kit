package maint.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.form.offices.OfficesDetailForm;

/**
 * 営業所 Entity
 *
 * @author jeon
 */

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficesEntity extends BaseEntity {
  /** Serial version UID */
  private static final long serialVersionUID = -1885878348992032012L;

  /** 営業所ID */
  Long officeId;
  /** 運行会社ID */
  Long operatingCompanyId;
  /** 運行会社名 */
  String operatingName;
  /** 営業所名 */
  String name;
  /** 営業所URL */
  String officeUrl;
  /** 営業所電話番号 */
  String phoneNumber;
  /** 利用区分 */
  Integer availableFlag;
  /** 削除フラグ */
  Long delFlag;

  /**
   * コンストラクタ
   *
   * @param form 営業所管理詳細FORM
   */
  public OfficesEntity(OfficesDetailForm form) {
    this.officeId = form.getOfficeId();
    this.operatingCompanyId = form.getOperatingCompanyId();
    this.operatingName = form.getOperatingName();
    this.name = form.getName();
    this.officeUrl = form.getOfficeUrl();
    this.phoneNumber = form.getPhoneNumber();
    this.availableFlag = form.getAvailableFlag();
    this.delFlag = form.getDelFlag();
  }
}
