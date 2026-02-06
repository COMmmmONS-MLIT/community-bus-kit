package maint.form.vehicles;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.planDaiya.vehicles.VehiclesDetailDto;
import maint.enums.DispMode;
import maint.enums.JapanPositioningFlag;
import maint.enums.UsageDivision;
import maint.form.BaseForm;
import maint.validation.annonation.IllegalCharacters;
import maint.validation.annonation.ValidEnumValue;

/**
 * 車両詳細FORM
 *
 * @author jeon
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VehiclesDetailForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = 3577736879843192375L;

  /** 車両ID */
  private Long vehicleId;
  /** 運行会社ID */
  @NotNull
  private Long operatingCompanyId;
  /** 車両名 */
  @NotBlank
  @IllegalCharacters
  @Size(min = 1, max = 100)
  private String vehicleName;
  /** GPSトラッカーシリアル */
  @NotBlank
  @IllegalCharacters
  @Size(min = 1, max = 100)
  private String gpsSerial;
  /** 利用区分（検索用） */
  @NotNull
  @ValidEnumValue(enumClass = UsageDivision.class)
  private Integer availableFlag;
  /** ユーザーID */
  private Long userId;
  /** 画面モード */
  private DispMode dispMode;
  /** 事業者ID */
  private Long businessCorpId;
  /** 日本測地変換フラグ */
  @NotNull
  @ValidEnumValue(enumClass = JapanPositioningFlag.class)
  private Integer japanPositioningFlag;

  /**
   * コンストラクタ
   *
   * @param dto            車両情報DTO
   * @param page           ページ
   * @param updateDispMode 変更モード
   */
  public void setInfo(VehiclesDetailDto dto, Long page, DispMode updateDispMode) {
    this.operatingCompanyId = dto.getOperatingCompanyId();
    this.vehicleId = dto.getVehicleId();
    this.vehicleName = dto.getVehicleName();
    this.gpsSerial = dto.getGpsSerial();
    this.availableFlag = dto.getAvailableFlag();
    this.japanPositioningFlag = dto.getJapanPositioningFlag();
    this.page = page;
    this.dispMode = updateDispMode;
  }
}
