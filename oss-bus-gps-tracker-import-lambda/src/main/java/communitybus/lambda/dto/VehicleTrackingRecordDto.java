package communitybus.lambda.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 車両動態登録用 DTO
 *
 * @author jekal
 *
 */
@Data
public class VehicleTrackingRecordDto implements Serializable {
  private static final long serialVersionUID = 5939526856444010247L;

  /** GPSトラッカーシリアル */
  private String gpsSerial;

  /** 仕業番号 */
  private String workNumber;

  /** 緯度、経度 */
  private String latitudeLongitude;

  /** 運行方向 */
  private String direction;

  /** 送信日時 */
  private OffsetDateTime createdDatetimeParsed;

  /** 便ID */
  private Integer busServiceId;

  /** 標柱ID */
  private Integer signpostId;

  /** 到着区分 */
  private Integer arrivalDivision;

  /** 遅延時間（秒） */
  private Long delaySeconds;

}
