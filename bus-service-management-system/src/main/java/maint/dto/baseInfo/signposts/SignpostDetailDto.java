package maint.dto.baseInfo.signposts;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 標柱詳細情報DTO
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SignpostDetailDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 4573888826002L;

  /** 標柱ID */
  private Long signpostId;

  /** 停留所ID */
  private Long busStopId;

  /** 標柱枝番 */
  private String signpostBranchNumber;

  /** 標柱番号 */
  private String signpostCode;

  /** 標柱名称 */
  private String signpostName;

  /** 標柱名称 かな */
  private String signpostKanaName;

  /** 標柱名称 英語 */
  private String signpostEnName;

  /** 標柱付加情報 */
  private String signpostSubName;

  /** 標柱住所 */
  private String address;

  /** 標柱地先名 */
  private String placeName;

  /** 緯度/経度（世界測地） */
  private String latitudeLongitude;

  /** 標柱付加情報 */
  private String signpostDesc;

  /** 標柱URL */
  private String signpostUrl;

  /** 停留所・標柱区分（0：標柱、1:停留所） */
  private Integer busStopType;

  /** のりば情報 */
  private String platformCode;

  /** 車いす利用区分 */
  private Integer wheelchairAccessibleType;

  /** 到着判定緯度、経度 */
  private String arrivalPoint;

  /** 到着判定半径 */
  private Integer arrivalPointRadius;

  /** 利用区分 */
  private Integer availableFlag;

}
