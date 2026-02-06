package maint.entity;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maint.enums.BusStopType;
import maint.form.signposts.SignpostsDetailForm;
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
public class SignpostsEntity implements Serializable {

  /** Serial version UID */
  @Serial
  private static final long serialVersionUID = -893280876401L;

  /** 標柱ID */
  private Long signpostId;
  /** 停留所ID */
  private Long busStopId;
  /** 標柱枝番 */
  private String signpostBranchNumber;
  /** 標柱番号 */
  private String signpostBranchCode;
  /** 標柱名称 */
  private String signpostName;
  /** 標柱名称 かな */
  private String signpostKanaName;
  /** 標柱名称 英語 */
  private String signpostEnName;
  /** 停留所名称 英語 */
  private String busStopEnName;
  /** 標柱付加情報 */
  private String signpostSubName;
    /** 標柱住所 */
  private String address;
  /** 標柱地先名 */
  private String placeName;
  /** 標柱付加情報 */
  private String signpostDesc;
  /** 緯度/経度（世界測地） */
  private String latitudeLongitude;
  /** 標柱URL */
  private String signpostUrl;
  /** 停留所・標柱区分（0：標柱、1:停留所） */
  private Integer busStopType;
  /** のりば情報 */
  private String platformCode;
  /** 利用区分（0：利用不可、1：利用中） */
  private Integer availableFlag;
  /** UserSession */
  private UserSession session;
  /** 車いす利用区分（0/1/2 等の区分値） */
  private Integer wheelchairAccessibleType;
  /** 到着判定緯度、経度 */
  private String arrivalPoint;
  /** 到着判定半径 */
  private Integer arrivalPointRadius;
  /**
   * コンストラクタ
   *
   * @param form 標柱詳細FORM
   */
  public SignpostsEntity(SignpostsDetailForm form) {
    this.signpostId = form.getSignpostId();
    this.busStopId = form.getBusStopId();
    this.signpostBranchNumber = form.getSignpostBranchNumber();
    this.signpostBranchCode = form.getSignpostBranchCode();
    this.signpostName = form.getSignpostName();
    this.signpostKanaName = form.getSignpostKanaName();
    this.signpostEnName = form.getSignpostEnName();
    this.signpostSubName = form.getSignpostSubName();
    this.address = form.getAddress();
    this.placeName = form.getPlaceName();
    this.signpostDesc = form.getSignpostDesc();
    this.busStopEnName = form.getSignpostSubName();
    this.latitudeLongitude = form.getLatitudeLongitude();
    this.signpostUrl = form.getSignpostUrl();
    this.busStopType = BusStopType.ROAD_SIGN.getValue();
    this.platformCode = form.getPlatformCode();
    this.availableFlag = form.getAvailableFlag();
    this.wheelchairAccessibleType = form.getWheelchairAccessibleType();
    this.arrivalPoint = form.getArrivalPoint();
    this.arrivalPointRadius = (form.getArrivalPointRadius() == null || form.getArrivalPointRadius().isBlank())
        ? null
        : Integer.parseInt(form.getArrivalPointRadius());
    this.session =
        ((UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }
}
