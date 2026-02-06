package communitybus.lambda.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 車両動態情報を保持するDTO
 *
 * @author jeon
 *
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class VehicleTrackingDto extends BaseDto {

  private static final long serialVersionUID = -2209402522105576097L;

  /** 車両動態ID */
  private Long vehicleTrackingId;

  /** 削除フラグ */
  private Integer delFlag;

  /** GPSトラッカーシリアル */
  private String gpsSerial;

  /** 仕業番号 */
  private String workNumber;

  /** 緯度、経度 */
  private String latitudeLongitude;

  /** 運行方向 */
  private String direction;

  /** 送信日時 */
  private String createdDatetime;

  /** 作成ユーザ */
  private String createdUser;

  /** 作成日 */
  private String createdAt;

  /** 更新ユーザ */
  private String updatedUser;

  /** 更新日 */
  private String updatedAt;

  /** 便ID */
  private Long busServiceId;

  /** 標柱ID */
  private Long signpostId;

  /** 遅延時間 */
  private Integer delay;

  /** 到着区分 */
  private String arrivalDivision;

}


