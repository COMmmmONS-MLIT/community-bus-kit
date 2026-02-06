package maint.dto.operation.holidayCalendars;

import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;
import maint.dto.operation.busDaiyas.BusDaiyasRegistedDto;
import maint.dto.planDaiya.daiyaRevisions.DaiyaRevisionsDetailDto;

/**
 * 便情報DTO
 *
 * @author g.ko
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BusServiceInfoDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -50493782540376500L;

  /** 便ID */
  private Long busServiceId;

  /** 運行カレンダーID */
  private Long calendarId;

  /** 車両ID */
  private Long vehicleId;

  /** 開始日 */
  private String serviceStartDate;

  /** 終了日 */
  private String serviceEndDate;

  /**
   * コンストラクタ（BusServicesMapperの抽出結果受け取り用）
   *
   * @param busServiceId 便ID
   * @param calendarId 運行カレンダーID
   * @param vehicleId 車両ID
   * @param serviceStartDate 開始日
   * @param serviceEndDate 終了日
   */
  public BusServiceInfoDto(Long busServiceId, Long calendarId, Long vehicleId, String serviceStartDate, String serviceEndDate) {
    this.busServiceId = busServiceId;
    this.calendarId = calendarId;
    this.vehicleId = vehicleId;
    this.serviceStartDate = serviceStartDate;
    this.serviceEndDate = serviceEndDate;
  }

  /**
   * コンストラクタ
   *
   * @param daiyaRevisionsDetailDto 便ダイヤ改正情報DTO
   * @param busDaiyasRegistedDto 登録済み便ダイヤ情報取得用DTO
   */
  public BusServiceInfoDto(DaiyaRevisionsDetailDto daiyaRevisionsDetailDto,
      BusDaiyasRegistedDto busDaiyasRegistedDto) {
    this.busServiceId = busDaiyasRegistedDto.getBusServiceId();
    this.calendarId = Long.valueOf(busDaiyasRegistedDto.getCalendarId());
    if (busDaiyasRegistedDto.getVehicleId() != null && !busDaiyasRegistedDto.getVehicleId().isEmpty()) {
      this.vehicleId = Long.valueOf(busDaiyasRegistedDto.getVehicleId());
    }
    this.serviceStartDate = daiyaRevisionsDetailDto.getServiceStartDate();
    this.serviceEndDate = daiyaRevisionsDetailDto.getServiceEndDate();
  }

}
