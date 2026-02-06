package jp.co.bus.batch.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 日毎便ダイヤ更新・登録用DTO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DailyBusDaiyaUpsertDto extends BaseDto {

  /** Serial version UID */
  private static final long serialVersionUID = 503492852023L;

  /** 日毎便ダイヤID */
  private Long dailyBusDaiyaId;
  /** 便ID */
  private Long busServiceId;
  /** 運行年月日 */
  private String serviceDate;
  /** 便休フラグ */
  private Integer salesCancelFlg;
  /** 運行対象外フラグ */
  private Integer operationCancelFlg;
  /** 車両ID */
  private Long vehicleId;
  /** 運休理由 */
  private String salesCancelReason;

  /**
   * コンストラクタ（登録用）
   *
   * @param busServiceId 便ID
   * @param serviceDate 運行年月日
   * @param vehicleId 車両ID
   */
  public DailyBusDaiyaUpsertDto(Long busServiceId, String serviceDate, Long vehicleId) {
    this.busServiceId = busServiceId;
    this.serviceDate = serviceDate;
    this.salesCancelFlg = 0;
    this.vehicleId = vehicleId;
  }

  /**
   * コンストラクタ（更新用）
   *
   * @param busServiceId 便ID
   */
  public DailyBusDaiyaUpsertDto(Long busServiceId, DailyBusDaiyaDto dto) {
    this.dailyBusDaiyaId = dto.getDailyBusDaiyaId();
    this.busServiceId = busServiceId;
    this.serviceDate = dto.getServiceDate();
  }

  /**
   * コンストラクタ（削除用）
   *
   * @param dailyBusDaiyaId 日毎便ダイヤID
   */
  public DailyBusDaiyaUpsertDto(Long dailyBusDaiyaId) {
    this.dailyBusDaiyaId = dailyBusDaiyaId;
  }

}
