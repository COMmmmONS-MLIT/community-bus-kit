package maint.dto.operation.lineBusStopsReport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 標柱情報インポート登録DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SignpostInsertDto extends BaseDto {

  private static final long serialVersionUID = -8233008971215813243L;

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

  /** 標柱名称 かな */
  private String signpostKanaName;

  /** 標柱名称 英語 */
  private String signpostEnName;

  /** 標柱住所 */
  private String address;

  /** 標柱地先名 */
  private String placeName;

  /** 標柱付加情報 */
  private String signpostDesc;

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

  /** 緯度・経度 */
  private String latitudeLongitude;

  /** 車いす利用区分 */
  private Short wheelchairAccessibleType;

  /** 到着判定緯度・経度 */
  private String arrivalPoint;

  /** 到着判定半径  */
  private Integer arrivalPointRadius;

}

