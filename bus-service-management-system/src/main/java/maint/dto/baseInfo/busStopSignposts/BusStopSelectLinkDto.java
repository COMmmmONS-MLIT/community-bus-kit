package maint.dto.baseInfo.busStopSignposts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 停留所DTO（SELECT用）
 *
 * @author k.takeuchi
 */
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class BusStopSelectLinkDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = 754327765482182L;

  /** 停留所ID */
  private Long busStopId;

  /** 停留所名 */
  private String busStopName;

  /** 停留所名カナ */
  private String busStopKanaName;

  /** 停留所名英文 */
  private String busStopEnName;

  /** 緯度・経度 */
  private String latitudeLongitude;

}
