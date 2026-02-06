package communitybus.lambda.dto;

import communitybus.lambda.utils.ExObjectUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 入力DTO
 *
 * @author y.kim
 *
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class InputDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -4376606248782755845L;

  /** GPSトラッカーシリアル */
  private String gpsSerial;

  /** 仕業番号 */
  private String workNumber;

  /** 緯度 */
  private Double latitude;

  /** 経度 */
  private Double longitude;

  /** 運行方向 */
  private String direction;

  /** 送信日時 request */
  private String createdDatetime;

  /** 送信日時 */
  private OffsetDateTime createdDatetimeParsed;

  /** 緯度、経度 */
  private String latitudeLongitude;

  /** 車両ID（検索用） */
  private Integer vehicleId;

  /** アクセスキー */
  private String accessKey;

  public List<String> validate() {
    List<String> errors = new ArrayList<>();

    // gpsSerial
    if (ExObjectUtils.isBlank(gpsSerial)) {
      errors.add("gpsSerial（GPSトラッカーシリアル）は必須です。");
    } else if (gpsSerial.length() > 100) {
      errors.add("gpsSerial（GPSトラッカーシリアル）は100文字以内で指定してください。");
    }

    // workNumber
    if (ExObjectUtils.isNotBlank(workNumber) && workNumber.length() > 20) {
      errors.add("workNumber（仕業番号）は20文字以内で指定してください。");
    }

    // latitude
    if (ExObjectUtils.isBlank(latitude)) {
      errors.add("latitude（緯度）は必須です。");
    }

    // longitude
    if (ExObjectUtils.isBlank(longitude)) {
      errors.add("longitude（経度）は必須です。");
    }

    // direction
    if (ExObjectUtils.isBlank(direction)) {
      errors.add("direction（運行方向）は必須です。");
    } else if (direction.length() > 50) {
      errors.add("direction（運行方向）は50文字以内で指定してください。");
    }

    // createdDatetime
    if (ExObjectUtils.isNotBlank(createdDatetime) && createdDatetime.length() > 25) {
      errors.add("createdDatetimeは25文字以内で指定してください。");
    }

    // accessKey
    if (ExObjectUtils.isBlank(accessKey)) {
      errors.add("accessKey（アクセスキー）は必須です。");
    } else if (accessKey.length() != 16) {
      errors.add("accessKey（アクセスキー）は16桁の文字列で指定してください。");
    }

    return errors;
  }

}
