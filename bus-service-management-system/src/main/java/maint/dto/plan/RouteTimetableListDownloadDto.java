package maint.dto.plan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;
import java.util.HashMap;

/**
 * 路線時刻表ダウンロード用DTO
 *
 * @author 林竜生
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RouteTimetableListDownloadDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = -8802086688608552086L;

  /** 路線ID */
  private Long rosenId;
  /** 路線名 */
  private String rosenName;
  /** 運行カレンダー区分 */
  private String calendarType;
  /** 往復区分 */
  private String lineDetailServiceDivision;
  /**　運行順序 */
  private String sequenceNo;
  /** 停留所ID */
  private Long busStopId;
  /** 停留所名 */
  private String busStopName;
  /** 系統ID */
  private Long lineRouteId;
  /** 系統名*/
  private String lineRouteName;
  /** 便ID */
  private Long busServiceId;
  /** 便名称 */
  private String busServiceName;
  /** 出発時刻*/
  private String busStopDepartureTime;

  public HashMap<String, Object> convertExcelHashMap() {
    HashMap<String, Object> map = new HashMap<>();
    map.put("rosenName", nvl(this.getRosenName()));
    map.put("calendarType",  nvl(this.getCalendarType()));
    String division = this.getLineDetailServiceDivision().equals("1") ? "往路" : "復路";
    map.put("lineDetailServiceDivision", nvl(division));

    return map;
  }

  private static String nvl(String s) {
    return (s == null) ? "" : s;
  }
}


