package maint.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.operation.lineBusStopsReport.SignpostInsertDto;

/**
 * 標柱 Entity
 *
 * @author jeon
 */

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignpostInsertEntity extends BaseEntity {

  /** Serial version UID */
  private static final long serialVersionUID = -2374437114831903773L;

  /** 標柱ID */
  private Integer signpostId;

  /** 削除フラグ */
  private Integer delFlag;

  /** 停留所ID */
  private String busStopId;

  /** 停留所コード */
  private String busStopCode;

  /** 標柱枝番 */
  private String signpostBranchNumber;

  /** 標柱番号 */
  private String signpostCode;

  /** 標柱名称 */
  private String signpostName;

  /** 標柱名称かな */
  private String signpostKanaName;

  /** 標柱名称英語 */
  private String signpostEnName;

  /** 標柱住所 */
  private String address;

  /** 標柱地先名 */
  private String placeName;

  /** 標柱付加情報 */
  private String signpostDesc;

  /** 緯度、経度 */
  private String latitudeLongitude;

  /** 標柱URL */
  private String signpostUrl;

  /** 停留所・標柱区分 */
  private Short busStopType;

  /** のりば情報 */
  private String platformCode;

  /** 利用可否区分 */
  private Short availableFlag;

  /** 作成者 */
  private String createdUser;

  /** 作成日時 */
  private String createdAt;

  /** 更新者 */
  private String updatedUser;

  /** 更新日時 */
  private String updatedAt;

  /** 標柱副名称 */
  private String signpostSubName;

  /** 車いす利用区分 */
  private Short wheelchairAccessibleType;

  /** 到着判定緯度、経度 */
  private String arrivalPoint;

  /** 到着判定半径  */
  private Integer arrivalPointRadius;


  /**
   * SignpostInsertDto から SignpostInsertDtoEntity へ値をコピーするコンストラクタ。
   *
   * @param insertDto 標柱情報の入力DTO
   */
  public SignpostInsertEntity(SignpostInsertDto insertDto) {
    this.signpostId = insertDto.getSignpostId();
    this.delFlag = insertDto.getDelFlag();
    this.busStopId = insertDto.getBusStopId();
    this.signpostBranchNumber = insertDto.getSignpostBranchNumber();
    this.signpostCode = insertDto.getSignpostCode();
    this.signpostName = insertDto.getSignpostName();
    this.signpostKanaName = insertDto.getSignpostKanaName();
    this.signpostEnName = insertDto.getSignpostEnName();
    this.address = insertDto.getAddress();
    this.placeName = insertDto.getPlaceName();
    this.signpostDesc = insertDto.getSignpostDesc();
    this.latitudeLongitude = insertDto.getLatitudeLongitude();
    this.signpostUrl = insertDto.getSignpostUrl();
    this.busStopType = insertDto.getBusStopType();
    this.platformCode = insertDto.getPlatformCode();
    this.availableFlag = insertDto.getAvailableFlag();
    this.createdUser = insertDto.getCreatedUser();
    this.createdAt = insertDto.getCreatedAt();
    this.updatedUser = insertDto.getUpdatedUser();
    this.updatedAt = insertDto.getUpdatedAt();
    this.signpostSubName = insertDto.getSignpostSubName();
    this.wheelchairAccessibleType = insertDto.getWheelchairAccessibleType();
    this.arrivalPoint = insertDto.getArrivalPoint();
    this.arrivalPointRadius = insertDto.getArrivalPointRadius();
  }
}
