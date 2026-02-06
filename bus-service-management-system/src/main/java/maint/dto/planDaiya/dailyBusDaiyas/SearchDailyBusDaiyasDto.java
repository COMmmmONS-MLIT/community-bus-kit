package maint.dto.planDaiya.dailyBusDaiyas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;
import maint.form.dailyBusDaiyas.DailyBusDaiyasSearchForm;

/**
 * 日毎便ダイヤ検索DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SearchDailyBusDaiyasDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = -130182204270670630L;

  /** 日毎便ダイヤID */
  private Long daiyaRevisionId;
  /** 路線ID */
  private Long rosenId;
  /** ダイヤ改正名称 */
  private String daiyaRevisionName;
  /** 日毎便ダイヤ開始日 */
  private String serviceStartDate;
  /** 日毎便ダイヤ終了日 */
  private String serviceEndDate;
  /** 始発出発時刻_開始 */
  private String busStopDepartureTimeStart;
  /** 始発出発時刻_終了 */
  private String busStopDepartureTimeEnd;
  /** 到着時刻時刻 */
  private String busStopArrivalTime;
  /** 始発出発時刻 */
  private String busStopDepartureTime;
  /** 運休フラグ */
  private Integer suspensionDivisionFlag;
  /** 便ID */
  private Long busServiceId;
  /** 系統ID */
  private Long lineRouteId;
  /** 事業者ID */
  private Long businessCorpId;
  /** 運行会社ID */
  private Long operatingCompanyId;
  /** 運行状態 */
  private Integer salesCancelFlg;

  /**
   * コンストラクタ
   *
   * @param form       日毎便ダイヤ検索FORM
   * @param startIndex 行の開始インデックス
   */
  public SearchDailyBusDaiyasDto(DailyBusDaiyasSearchForm form, Long startIndex) {
    this.startIndex = startIndex;
    if (form == null) {
      return;
    }
    this.busServiceId = form.getSearchBusServiceId();
    this.rosenId = form.getSearchRosenId();
    this.daiyaRevisionName = form.getSearchDaiyaRevisionName();
    this.serviceStartDate = form.getSearchServiceStartDate();
    this.serviceEndDate = form.getSearchServiceEndDate();
    this.busStopArrivalTime = form.getSearchBusStopArrivalTime();
    this.busStopDepartureTime = form.getSearchBusStopDepartureTime();
    this.busStopDepartureTimeStart = form.getSearchBusStopArrivalTime();
    this.busStopDepartureTimeEnd = form.getSearchBusStopDepartureTime();
    this.suspensionDivisionFlag = form.getSearchSalesCancelFlg();
  }

}
