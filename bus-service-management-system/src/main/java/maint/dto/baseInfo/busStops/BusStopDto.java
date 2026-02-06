package maint.dto.baseInfo.busStops;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 停留所情報DTO
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusStopDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 52354746609L;

  /** 停留所ID */
  private Long busStopId;

  /** 停留所コード */
  private String busStopCode;

  /** 停留所名 */
  private String busStopName;

  /** 利用期間_開始 */
  private String serviceStartDate;

  /** 利用期間_終了 */
  private String serviceEndDate;

  /** 利用区分（0：利用不可、1：利用中） */
  private Integer availableFlag;

  /** 事業者名 */
  private String businessCorpName;

}
