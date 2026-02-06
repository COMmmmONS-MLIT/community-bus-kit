package maint.entity;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maint.dto.baseInfo.busStopSignposts.BusStopLinkDetailDto;
import maint.dto.baseInfo.busStopSignposts.SignpostsLinkDetailDto;
import maint.enums.BusStopType;
import maint.form.busStopSignposts.BusStopSignpostsDetailForm;
import maint.session.UserSession;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 標柱Entity
 *
 * @author k.takeuchi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignpostsLinkEntity implements Serializable {

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
  public SignpostsLinkEntity(BusStopSignpostsDetailForm form, int indexBs, int indexSp) {
    BusStopLinkDetailDto busStopLinkDetailDto = form.getBusStopLinkDetailDtoList().get(indexBs);
    SignpostsLinkDetailDto signpostsLinkDetailDto = busStopLinkDetailDto.getSignpostsLinkDetailDtoList().get(indexSp);

    this.busStopId = signpostsLinkDetailDto.getBusStopId();
    this.signpostId = signpostsLinkDetailDto.getSignpostId();
    this.signpostBranchNumber = signpostsLinkDetailDto.getSignpostBranchNumber();
    this.signpostBranchCode = signpostsLinkDetailDto.getSignpostCode();
    this.signpostName = signpostsLinkDetailDto.getSignpostName();
    this.signpostKanaName = signpostsLinkDetailDto.getSignpostKanaName();
    this.signpostEnName = signpostsLinkDetailDto.getSignpostEnName();
    this.signpostSubName = signpostsLinkDetailDto.getSignpostSubName();
    this.address = signpostsLinkDetailDto.getAddress();
    this.placeName = signpostsLinkDetailDto.getPlaceName();
    this.signpostDesc = signpostsLinkDetailDto.getSignpostDesc();
    this.busStopEnName = signpostsLinkDetailDto.getSignpostSubName();
    this.latitudeLongitude = signpostsLinkDetailDto.getLatitudeLongitude();
    this.signpostUrl = signpostsLinkDetailDto.getSignpostUrl();
    this.busStopType = BusStopType.ROAD_SIGN.getValue();
    this.platformCode = signpostsLinkDetailDto.getPlatformCode();
    this.availableFlag = signpostsLinkDetailDto.getAvailableFlag();
    this.wheelchairAccessibleType = signpostsLinkDetailDto.getWheelchairAccessibleType();
    this.arrivalPoint = signpostsLinkDetailDto.getArrivalPoint();
    this.arrivalPointRadius = signpostsLinkDetailDto.getArrivalPointRadius();
    this.session =
        ((UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }
}
