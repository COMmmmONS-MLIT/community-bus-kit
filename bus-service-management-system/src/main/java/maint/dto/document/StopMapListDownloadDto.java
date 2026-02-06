package maint.dto.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;
import java.util.HashMap;

/**
 * 停留所付近図ダウンロード用DTO
 *
 * @author 林竜生
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class StopMapListDownloadDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 2572143723305543357L;

  /** 停留所ID */
  private Long busStopId;
  /** 停留所名 */
  private String busStopName;
  /** 往路標柱ID*/
  private Long outSignpostId;
  /** 往路標柱名*/
  private String outSignpostName;
  /** 復路標柱ID*/
  private Long inSignpostId;
  /** 復路標柱名*/
  private String inSignpostName;
  /** 往路住所*/
  private String outRouteAddress;
  /** 復路住所 */
  private String inRouteAddress;
  /** 設置場所 */
  private String latitudeLongitude;

  public HashMap<String, Object> convertExcelHashMap() {
    HashMap<String, Object> map = new HashMap<>();
    map.put("busStopName",      nvl(this.getBusStopName()));      // 停留所名
    map.put("outRouteAddress",  nvl(this.getOutRouteAddress()));  // 往路住所
    map.put("inRouteAddress",   nvl(this.getInRouteAddress()));   // 復路住所
    map.put("installPlace",     nvl(this.getLatitudeLongitude())); // 設置場所（緯度・経度）

    return map;
  }

  private static String nvl(String s) {
    return (s == null) ? "" : s;
  }
}
