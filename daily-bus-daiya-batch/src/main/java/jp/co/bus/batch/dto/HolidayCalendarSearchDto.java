package jp.co.bus.batch.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 祝日カレンダー検索用DTO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HolidayCalendarSearchDto extends BaseDto {

  /** Serial version UID */
  private static final long serialVersionUID = 4930852430L;

  /** 対象日付 */
  private String targetDate;
  /** 事業者ID */
  private Long businessCorpId;

  /**
   * コンストラクタ
   *
   * @param targetDate 便ID
   * @param businessCorpId 運行年月日
   */
  public HolidayCalendarSearchDto(String targetDate, Long businessCorpId) {
    this.targetDate = targetDate;
    this.businessCorpId = businessCorpId;
  }

}
