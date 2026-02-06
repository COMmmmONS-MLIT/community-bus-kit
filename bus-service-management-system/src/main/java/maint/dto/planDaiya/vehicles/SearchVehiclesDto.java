package maint.dto.planDaiya.vehicles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;
import maint.form.vehicles.VehiclesSearchForm;

/**
 * 車両検索DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SearchVehiclesDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = -3901562067637722533L;
  /** ユーザーID */
  private Long userId;
  /** 車両ID */
  private Long vehicleId;
  /** 運行会社ID */
  private Long operatingCompanyId;
  /** 車両名 */
  private String vehicleName;
  /** GPSトラッカーシリアル */
  private String gpsSerial;
  /** 利用可否区分 */
  private Integer availableFlag;
  /** 事業者ID */
  private Long BusinessCorpId;
  /** 日本測地変換フラグ */
  private Integer japanPositioningFlag;

  /**
   * コンストラクタ
   *
   * @param form       車両検索FORM
   * @param startIndex 行の開始インデックス
   */
  public SearchVehiclesDto(VehiclesSearchForm form, Long startIndex) {
    this.startIndex = startIndex;
    if (form == null) {
      return;
    }

    this.operatingCompanyId = form.getSearchOperatingCompanyId();
    this.vehicleName = form.getSearchVehicleName();
    this.availableFlag = form.getSearchAvailableFlag();
  }
  /**
   * 部分一致Like検索用車両名取得
   *
   * @return 部分一致Like検索用車両名
   */
  public String getLikeVehicleName() {
    return makeMiddleMatchString(this.vehicleName);
  }
}
