package communitybus.lambda.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 入力DTO
 *
 * @author y.kim
 *
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class VehiclesDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -5550230790368601881L;

  /** 車両ID */
  private Integer vehicleId;

  /** 削除フラグ（0: 有効, 1: 削除） */
  private Integer delFlag;

  /** 運行会社ID */
  private Integer operatingCompanyId;

  /** 車両名 */
  private String vehicleName;

  /** GPSトラッカーシリアル */
  private String gpsSerial;

  /** 利用可否区分（0: 利用不可, 1: 利用可能） */
  private Short availableFlag;

  /** 作成ユーザ */
  private String createdUser;

  /** 作成日時 */
  private String createdAt;

  /** 更新ユーザ */
  private String updatedUser;

  /** 更新日時 */
  private String updatedAt;

  /** 日本測地変換フラグ */
  private Integer japanPositioningFlag;

}
