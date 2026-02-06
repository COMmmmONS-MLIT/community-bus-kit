package maint.dto.operation.lineBusStopsReport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 停留所情報インポート登録DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LineBusStopsReportInsertDto extends BaseDto {

  private static final long serialVersionUID = -5326657281948471768L;

  /** 停留所ID */
  private String busStopId;

  /** 事業者ID */
  private Long businessCorpId;

  /** 停留所コード */
  private String busStopCode;

  /** 停留所・標柱区分 */
  private Integer busStopType;

  /** 利用開始年月日 */
  private String serviceStartDate;

  /** 利用終了年月日 */
  private String serviceEndDate;

  /** 停留所名称 */
  private String busStopName;

  /** 停留所名称 かな */
  private String busStopKanaName;

  /** 停留所名称 英語 */
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

  /** 標柱名称 かな */
  private String signpostKanaName;

  /** 標柱名称 英語 */
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

  /** 作成者 */
  private String createdUser;

  /** 更新者 */
  private String updatedUser;

}

