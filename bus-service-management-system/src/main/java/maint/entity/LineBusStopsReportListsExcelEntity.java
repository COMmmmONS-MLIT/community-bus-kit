package maint.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.operation.lineBusStopsReport.LineBusStopsReportInsertDto;

/**
 * 停留所Entity
 *
 * @author jeon
 */

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineBusStopsReportListsExcelEntity extends BaseEntity {
  /** Serial version UID */
  private static final long serialVersionUID = -1885878348992032012L;

  /** 事業者ID */
  private String businessCorpId;

  /** 停留所ID */
  private Integer busStopId;

  /** 停留所コード */
  private String busStopCode;

  /** 停留所・標柱区分 */
  private String busStopType;

  /** 利用開始年月日 */
  private String serviceStartDate;

  /** 利用終了年月日 */
  private String serviceEndDate;

  /** 停留所名称 */
  private String busStopName;

  /** 停留所名称かな */
  private String busStopKanaName;

  /** 停留所名称英語 */
  private String busStopEnName;

  /** 停留所URL */
  private String busStopUrl;

  /** 屋内バスターミナルフラグ */
  private String indoorBusTerminalFlag;

  /** 緯度、経度 */
  private String latitudeLongitude;

  /** 標柱枝番 */
  private String signpostBranchNumber;

  /** 標柱番号 */
  private String signpostCode;

  /** 標柱名称 */
  private String signpostName;

  /** 標柱名称かな */
  private String signpostKanaName;

  /** 標柱名称英語 */
  private String signpostEnName;

  /** 標柱副名称 */
  private String signpostSubName;

  /** 標柱住所 */
  private String address;

  /** 標柱地先名 */
  private String placeName;

  /** 標柱付加情報 */
  private String signpostDesc;

  /** 標柱URL */
  private String signpostUrl;

  /** のりば情報 */
  private String platformCode;

  /** 車いす利用区分 */
  private String wheelchairAccessibleType;

  /** 到着判定緯度、経度 */
  private String arrivalPoint;

  /** 到着判定半径  */
  private Integer arrivalPointRadius;



  /**
   * LineBusStopsReportInsertDto から LineBusStopsReportListsExcelEntity へ値をコピーするコンストラクタ。
   *
   * @param lineBusStop Excelアップロード用の停留所データDTO
   */
  public LineBusStopsReportListsExcelEntity(LineBusStopsReportInsertDto lineBusStop) {
    this.businessCorpId = lineBusStop.getBusinessCorpId() != null ? lineBusStop.getBusinessCorpId().toString() : null;
    this.busStopCode = lineBusStop.getBusStopCode();
    this.busStopType = lineBusStop.getBusStopType() != null ? lineBusStop.getBusStopType().toString() : null;
    this.serviceStartDate = lineBusStop.getServiceStartDate();
    this.serviceEndDate = lineBusStop.getServiceEndDate();
    this.busStopName = lineBusStop.getBusStopName();
    this.busStopKanaName = lineBusStop.getBusStopKanaName();
    this.busStopEnName = lineBusStop.getBusStopEnName();
    this.busStopUrl = lineBusStop.getBusStopUrl();
    this.indoorBusTerminalFlag = lineBusStop.getIndoorBusTerminalFlag();
    this.latitudeLongitude = lineBusStop.getLatitudeLongitude();
    this.signpostBranchNumber = lineBusStop.getSignpostBranchNumber();
    this.signpostCode = lineBusStop.getSignpostCode();
    this.signpostName = lineBusStop.getSignpostName();
    this.signpostKanaName = lineBusStop.getSignpostKanaName();
    this.signpostEnName = lineBusStop.getSignpostEnName();
    this.signpostSubName = lineBusStop.getSignpostSubName();
    this.address = lineBusStop.getAddress();
    this.placeName = lineBusStop.getPlaceName();
    this.signpostDesc = lineBusStop.getSignpostDesc();
    this.signpostUrl = lineBusStop.getSignpostUrl();
    this.platformCode = lineBusStop.getPlatformCode();
    this.wheelchairAccessibleType = lineBusStop.getWheelchairAccessibleType();
    this.arrivalPoint = lineBusStop.getArrivalPoint();
    this.arrivalPointRadius = lineBusStop.getArrivalPointRadius();
  }

}
