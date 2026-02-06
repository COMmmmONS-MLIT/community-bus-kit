package maint.entity;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maint.dto.baseInfo.busStopSignposts.BusStopLinkDetailDto;
import maint.enums.BusStopType;
import maint.form.busStopSignposts.BusStopSignpostsDetailForm;
import maint.session.UserSession;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 停留所Entity
 *
 * @author k.takeuchi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusStopsLinkEntity implements Serializable {

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
   * @param form     停留所・標柱詳細FORM
   */
  public BusStopsLinkEntity(BusStopSignpostsDetailForm form, int index) {
    BusStopLinkDetailDto busStopLinkDetailDto = form.getBusStopLinkDetailDtoList().get(index);

    this.busStopId = busStopLinkDetailDto.getBusStopId();
    this.busStopCode = busStopLinkDetailDto.getBusStopCode();
    this.serviceStartDate = busStopLinkDetailDto.getServiceStartDate();
    this.serviceEndDate = busStopLinkDetailDto.getServiceEndDate();
    this.busStopName = busStopLinkDetailDto.getBusStopName();
    this.busStopKanaName = busStopLinkDetailDto.getBusStopKanaName();
    this.busStopEnName = busStopLinkDetailDto.getBusStopEnName();
    this.latitudeLongitude = busStopLinkDetailDto.getLatitudeLongitude();
    this.busStopUrl = busStopLinkDetailDto.getBusStopUrl();
    this.busStopType = BusStopType.BUS_STOP.getValue();
    this.availableFlag = busStopLinkDetailDto.getAvailableFlag();
    this.indoorBusTerminalFlag = busStopLinkDetailDto.getIndoorBusTerminalFlag();
    this.session =
        ((UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }
}
