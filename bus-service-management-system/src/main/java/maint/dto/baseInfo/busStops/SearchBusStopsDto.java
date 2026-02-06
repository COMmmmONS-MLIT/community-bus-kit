package maint.dto.baseInfo.busStops;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;
import maint.form.busStops.BusStopsSearchForm;
import maint.session.UserSession;

/**
 * 停留所検索DTO
 *
 * @author g.ko
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SearchBusStopsDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 7853546783333328L;

  /** 停留所名（検索用） */
  private String busStopName;

  /** 停留所コード（検索用） */
  private String busStopCode;

  /** 利用期間_開始（検索用） */
  private String serviceStartDate;

  /** 利用期間_終了（検索用） */
  private String serviceEndDate;

  /** 利用可否（検索用） */
  private Integer searchUsageDivision;

  /** 事業者ID */
  private Long businessCorpId;

  /**
   * コンストラクタ
   *
   * @param form 停留所検索 Form
   * @param startIndex 行の開始インデックス
   * @param userSession ユーザーセッション
   */
  public SearchBusStopsDto(BusStopsSearchForm form, Long startIndex, UserSession userSession) {
    this.startIndex = startIndex;
    if (form == null) {
      return;
    }
    this.busStopName = form.getBusStopName();
    this.busStopCode = form.getBusStopCode();
    this.serviceStartDate = form.getServiceStartDate();
    this.serviceEndDate = form.getServiceEndDate();
    this.searchUsageDivision = form.getSearchUsageDivision();
    this.businessCorpId = userSession.getBusinessCorpId();
  }

  /**
   * 部分一致Like検索用停留所名取得
   *
   * @return 部分一致Like検索用停留所名
   */
  public String getLikeSearchBusStopName() {
    return makeMiddleMatchString(this.busStopName);
  }
}
