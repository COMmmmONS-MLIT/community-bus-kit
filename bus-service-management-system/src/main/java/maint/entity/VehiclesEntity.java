package maint.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.form.vehicles.VehiclesDetailForm;

/**
 * 車両 Entity
 *
 * @author jeon
 */

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiclesEntity extends BaseEntity {
  /** Serial version UID */
  private static final long serialVersionUID = -6951896605745862623L;

  /** 車両ID */
  private Long vehicleId;
  /** 削除フラグ */
  private Long delFlag;
  /** 運行会社ID */
  private Long operatingCompanyId;
  /** 車両名 */
  private String vehicleName;
  /** GPSトラッカーシリアル */
  private String gpsSerial;
  /** 利用可否区分 */
  private Integer availableFlag;
  /** 事業者ID */
  private Long businessCorpId;
  /** 日本測地変換フラグ */
  private Integer japanPositioningFlag;
  /**
   * コンストラクタ
   *
   * @param form 車両詳細FORM
   */
  public VehiclesEntity(VehiclesDetailForm form) {
    this.vehicleId=form.getVehicleId();
    this.delFlag=form.getDelFlag();
    this.operatingCompanyId=form.getOperatingCompanyId();
    this.vehicleName=form.getVehicleName();
    this.gpsSerial=form.getGpsSerial();
    this.availableFlag=form.getAvailableFlag();
    this.japanPositioningFlag=form.getJapanPositioningFlag();
  }
}
