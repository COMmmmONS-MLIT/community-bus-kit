package maint.dto.baseInfo.busStops;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 停留所詳細情報DTO
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusStopDetailDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 6454184122600L;

  /** 停留所ID */
  private Long busStopId;

  /** 停留所コード */
  private String busStopCode;

  /** 利用期間_開始 */
  private String serviceStartDate;

  /** 利用期間_終了 */
  private String serviceEndDate;

  /** 停留所名称 */
  private String busStopName;

  /** 停留所名称 かな */
  private String busStopKanaName;

  /** 停留所名称 英語 */
  private String busStopEnName;

  /** 緯度/経度（世界測地） */
  private String latitudeLongitude;

  /** 停留所URL */
  private String busStopUrl;

  /** 利用区分（0：利用不可、1：利用中） */
  private Integer availableFlag;

  /** 屋内バスターミナルフラグ */
  private Integer indoorBusTerminalFlag;
}
