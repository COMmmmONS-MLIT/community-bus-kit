package maint.form.dailyBusDaiyas;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.planDaiya.dailyBusDaiyas.DailyBusDaiyasDetailDto;
import maint.enums.DispMode;
import maint.enums.SuspensionDivision;
import maint.form.BaseForm;
import maint.validation.annonation.ValidEnumValue;

/**
 * 日毎便ダイヤ詳細FORM
 *
 * @author jeon
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DailyBusDaiyasDetailForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = 6346037232801441287L;

  /** 路線ID */
  private Long rosenId;
  /** 路線名 */
  private String rosenName;
  /** 往復区分 */
  private Integer lineDetailServiceDivision;
  /** 系統名 */
  private String lineRouteName;
  /** 運行日 */
  private String serviceDate;
  /** 運行年月日の00:00 */
  private Long serviceStartTime;
  /** 運行年月日の23:59 */
  private Long serviceEndTime;
  /** 始発停留所時刻 */
  private String busStopDepartureTime;
  /** 運行会社 */
  private Long operatingCompanyId;
  /** 車両ID */
  private Long vehicleId;
  /** 車両名 */
  private String vehicleName;
  /** 運行状態 */
  @NotNull
  @ValidEnumValue(enumClass = SuspensionDivision.class)
  private Integer salesCancelFlg;
  /** 運休フラグ */
  private Integer suspensionDivisionFlag;
  /** 運行除外日 */
  private Integer operationCancelFlg;
  /** お知らせ */
  private String notification;
  /** 事業者ID */
  private Long businessCorpId;
  /** 運行会社 */
  private String operatingCompanyName;
  /** 日毎便ダイヤID ? */
  private Long daiyaRevisionId;
  /** 日毎便ダイヤID */
  private Long dailyBusDaiyaId;
  /** 便番号 */
  private String busServiceTripId;

  /** 画面モード */
  private DispMode dispMode;

  /**
   * コンストラクタ
   *
   * @param dto            日毎便ダイヤ情報DTO
   * @param page           ページ
   * @param updateDispMode 変更モード
   */
  public void setInfo(DailyBusDaiyasDetailDto dto, Long page, DispMode updateDispMode) {
    this.rosenName = dto.getRosenName();
    this.lineDetailServiceDivision = dto.getLineDetailServiceDivision();
    this.lineRouteName = dto.getLineRouteName();
    this.busStopDepartureTime = dto.getBusStopDepartureTime();
    this.operatingCompanyName = dto.getOperatingCompanyName();
    this.serviceDate = dto.getServiceDate();
    this.salesCancelFlg = dto.getSalesCancelFlg();
    this.operationCancelFlg = dto.getOperationCancelFlg();
    this.vehicleId = dto.getVehicleId();
    this.vehicleName = dto.getVehicleName();
    this.notification = dto.getNotification();
    this.busServiceTripId = dto.getBusServiceTripId();
    this.page = page;
    this.dispMode = updateDispMode;
  }
}
