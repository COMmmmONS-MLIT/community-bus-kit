package jp.co.bus.batch.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 便情報DTO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BusServiceInfoDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -508947238005432L;

  /** 便ID */
  private Long busServiceId;

  /** 運行カレンダーID */
  private Long calendarId;

  /** 車両ID */
  private Long vehicleId;

  /** 事業者ID */
  private Long businessCorpId;

  /** 開始日 */
  private String serviceStartDate;

  /** 終了日 */
  private String serviceEndDate;

  /**
   * コンストラクタ
   *
   * @param busServiceId 便ID
   * @param startDate 開始日
   * @param endDate 終了日
   */
  public BusServiceInfoDto(Long busServiceId, String startDate, String endDate) {
    this.busServiceId = busServiceId;
    this.serviceStartDate = startDate;
    this.serviceEndDate = endDate;
  }

}
