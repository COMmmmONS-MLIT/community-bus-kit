package jp.co.bus.batch.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 日毎便ダイヤ取得用DTO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DailyBusDaiyaGetDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -78052375023850320L;

  /** 便ID */
  private Long busServiceId;

  /** 対象日 */
  private List<String> targetDates;

  /**
   * コンストラクタ
   *
   * @param busServiceId 便ID
   * @param targetDates 対象日
   */
  public DailyBusDaiyaGetDto(Long busServiceId, List<String> targetDates) {
    this.busServiceId = busServiceId;
    this.targetDates = targetDates;
  }

}
