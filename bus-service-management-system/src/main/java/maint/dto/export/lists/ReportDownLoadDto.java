package maint.dto.export.lists;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 一覧表出力ダウンロード用DTO
 *
 * @author JEKAL.H
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReportDownLoadDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 2572143723305543357L;
  // java
  /** 事業者ID */
  private Long businessCorpId;
  /** 停留所ID */
  private Long busStopId;
  /** 便ダイヤ改正ID */
  private Long daiyaRevisionId;
  /** 停留所名 */
  private String busStopName;
  /** 利用開始年月 */
  private String serviceStartDate;
  /** 利用終了年月 */
  private String serviceEndDate;
  /** 緯度 */
  private Double latitude;
  /** 経度 */
  private Double longitude;
  /** 緯度・経度 */
  private String latitudeLongitude;
  /** 停留所URL */
  private String busStopUrl;
  /** 標柱ID */
  private Long signpostId;
  /** 標柱枝番 */
  private String signpostBranchNumber;
  /** 標柱番号 */
  private String signpostCode;
  /** 標柱名称 */
  private String signpostName;
  /** 標柱住所 */
  private String address;
  /** 標柱地先名 */
  private String placeName;
  /** 標柱付加情報 */
  private String signpostDesc;
  /** 標柱緯度 */
  private Double signLatitude;
  /** 標柱経度 */
  private Double signLongitude;
  /** 標柱緯度・経度 */
  private String signLatitudeLongitude;
  /** 標柱URL */
  private String signpostUrl;
  /** 停留所・標柱区分 */
  private Integer busStopType;
  /** のりば情報 */
  private String platformCode;
  /** 路線名 */
  private String rosenName;
  /** 系統ID */
  private Long lineRouteId;
  /** 系統コード */
  private String lineRouteCode;
  /** 系統利用開始年月 */
  private String lineRouteServiceStartDate;
  /** 系統利用終了年月 */
  private String lineRouteServiceEndDate;
  /** 系統名称 */
  private String lineRouteName;
  /** 系統番号 */
  private String lineRouteShortName;
  /** 系統番号名称 */
  private String lineRouteNumber;
  /** 往路、復路区分 */
  private Long lineDetailServiceDivision;
  /** 系統URL */
  private String lineRouteUrl;
  /** 系統背景色 */
  private String lineRouteColor;
  /** 系統文字色 */
  private String lineRouteTextColor;
  /** 系統備考 */
  private String lineRouteRemarks;
  /** 乗降区分 */
  private Integer rideAlightDivision;
  /** 下車区分 */
  private Integer dropOffDivision;
  /** 停留所行先 */
  private String stopHeadsign;
  /** 通算距離 */
  private String shapeDistTraveled;
  /** 運行順序 */
  private Integer stopSequence;
  /** 便ID */
  private Long busServiceId;
  /** 便番号 */
  private String busServiceTripId;
  /** 便行先 */
  private String busServiceTripHeadsign;
  /** 便名称 */
  private String busServiceName;
  /** 運行カレンダーID */
  private Integer calendarId;
  /** 車いす利用区分 */
  private Integer wheelchairAccessibleType;
  /** 自転車持ち込み区分 */
  private Integer bikesAllowedType;
  /** 便情報 */
  private String busServiceTripDesc;
  /** 便記号 */
  private String busServiceTripDescSymbol;
  /** 営業所ID */
  private Integer officeId;
  /** 仕業番号 */
  private String workNumber;
  /** 系統停留所ID */
  private Integer lineRouteBusStopId;
  /** 到着時刻 */
  private String busStopArrivalTime;
  /** 出発時刻 */
  private String busStopDepartureTime;
  /** ダウンロード区分 */
  private Integer downloadDivisionFlag;
  /** 到着判定緯度、経度 */
  private String arrivalPoint;
  /** 到着判定半径 */
  private Integer arrivalPointRadius;
  /** 車両ID */
  private Integer vehicleId;

}
