package maint.dto.opeInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;
import java.util.HashMap;

/**
 * スターフダウンロード用DTO
 *
 * @author 林竜生
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class StaffReportListDownloadDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 3925637159712557529L;

  /** 路線ID */
  private Long rosenId;
  /** 路線名 */
  private String rosenName;
  /** 運行カレンダー区分 */
  private String calendarType;
  /** 往復区分 */
  private String lineDetailServiceDivision;
  /** 停留所ID */
  private Long busStopId;
  /** 停留所名 */
  private String busStopName;
  /** 系統ID */
  private Long lineRouteId;
  /** 系統名*/
  private String lineRouteName;
  /** 仕業番号 */
  private String workNumber;
  /** 便ID */
  private Long busServiceId;
  /** 便名 */
  private String busServiceName;
  /** 便番号 */
  private String busServiceTripId;
  /** 出発時刻 */
  private String busStopDepartureTime;

  public HashMap<String, Object> convertExcelHashMap() {
    HashMap<String, Object> map = new HashMap<>();
    map.put("rosenName", nvl(this.getRosenName()));
    map.put("calendarType",  nvl(this.getCalendarType()));
    String division = this.getLineDetailServiceDivision().equals("1") ? "往路" : "復路";
    map.put("lineDetailServiceDivision", nvl(division));
    map.put("workNumber", nvl(workNumber));

    return map;
  }

  private static String nvl(String s) {
    return (s == null) ? "" : s;
  }
}
