package maint.entity;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maint.enums.BusStopType;
import maint.form.busStops.BusStopsDetailForm;
import maint.session.UserSession;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 停留所Entity
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusStopsEntity implements Serializable {

  /** Serial version UID */
  @Serial
  private static final long serialVersionUID = 937827642541L;

  /** 停留所ID */
  private Long busStopId;
  /** 事業者ID */
  private Long businessCorpId;
  /** 停留所コード */
  private String busStopCode;
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
  /** 緯度/経度（世界測地） */
  private String latitudeLongitude;
  /** 停留所URL */
  private String busStopUrl;
  /** 停留所・標柱区分 */
  private Integer busStopType;
  /** 利用区分 */
  private Integer availableFlag;
  /** UserSession */
  private UserSession session;
  /** 屋内バスターミナルフラグ */
  private Integer indoorBusTerminalFlag;
  /**
   * コンストラクタ
   *
   * @param form     停留所詳細FORM
   */
  public BusStopsEntity(BusStopsDetailForm form) {
    this.busStopId = form.getBusStopId();
    this.busStopCode = form.getBusStopCode();
    this.serviceStartDate = form.getServiceStartDate();
    this.serviceEndDate = form.getServiceEndDate();
    this.busStopName = form.getBusStopName();
    this.busStopKanaName = form.getBusStopKanaName();
    this.busStopEnName = form.getBusStopEnName();
    this.latitudeLongitude = form.getLatitudeLongitude();
    this.busStopUrl = form.getBusStopUrl();
    this.busStopType = BusStopType.BUS_STOP.getValue();
    this.availableFlag = form.getAvailableFlag();
    this.indoorBusTerminalFlag = form.getIndoorBusTerminalFlag();
    this.session =
        ((UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }
}
