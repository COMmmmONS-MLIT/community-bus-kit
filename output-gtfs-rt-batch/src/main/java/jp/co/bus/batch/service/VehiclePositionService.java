package jp.co.bus.batch.service;

import com.google.transit.realtime.GtfsRealtime;
import jp.co.bus.batch.dto.VehicleTrackingDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.List;

/**
 * 車両動態データを作成するサービスクラス。
 *
 * @author jeon
 */
public class VehiclePositionService {

  /** ログ */
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * GTFS-RTフォーマットのVehiclePositionデータを作成します。
   *
   * @param tripDataList 便データリスト
   * @return 生成されたTripUpdateフィード
   */
  public GtfsRealtime.FeedMessage createVehiclePositionFeed(List<VehicleTrackingDto> tripDataList) {
    // 0. FeedMessage ビルダー作成
    GtfsRealtime.FeedMessage.Builder feedBuilder = GtfsRealtime.FeedMessage.newBuilder();

    // 1. ヘッダー設定
    GtfsRealtime.FeedHeader.Builder headerBuilder = GtfsRealtime.FeedHeader.newBuilder();
    headerBuilder.setGtfsRealtimeVersion("2.0");
    headerBuilder.setIncrementality(GtfsRealtime.FeedHeader.Incrementality.FULL_DATASET);
    headerBuilder.setTimestamp(Instant.now().getEpochSecond()); // サーバ現在時刻
    feedBuilder.setHeader(headerBuilder);

    // 2. 位置情報データ生成
    if (tripDataList == null || tripDataList.isEmpty()) {
      LOGGER.warn("VehicleTrackingDto リストが空です");
      return feedBuilder.build();
    }

    for (VehicleTrackingDto data : tripDataList) {
      try {
        // VehiclePosition
        GtfsRealtime.VehiclePosition.Builder vpBuilder = GtfsRealtime.VehiclePosition.newBuilder();

        // TripDescriptor（trip.trip_id のみ設定 / null → ""）
        GtfsRealtime.TripDescriptor.Builder tripDescriptorBuilder = GtfsRealtime.TripDescriptor.newBuilder();
        String tripId = nullToEmpty(data.getBusServiceTripId()); // null → ""
        // schedule_relationship
        tripDescriptorBuilder.setScheduleRelationship(GtfsRealtime.TripDescriptor.ScheduleRelationship.SCHEDULED);
        tripDescriptorBuilder.setTripId(tripId);

        // direction_id は不要なので設定しない
        vpBuilder.setTrip(tripDescriptorBuilder);

        // VehicleDescriptor
        GtfsRealtime.VehicleDescriptor.Builder vehicleDescriptorBuilder = GtfsRealtime.VehicleDescriptor.newBuilder();

        // vehicle.id = 車両マスタ.車両ID（null → ""）
        String vehicleId = nullToEmpty(data.getVehicleId());
        vehicleDescriptorBuilder.setId(vehicleId);

        // vehicle.label = 車両マスタ.車両名（null → ""）
        String vehicleName = nullToEmpty(data.getVehicleName());
        vehicleDescriptorBuilder.setLabel(vehicleName);

        // vehicle.wheelchair_accessible = 便.車いす利用区分
        Long wheelchairAccessibleType = data.getWheelchairAccessibleType();
        if (wheelchairAccessibleType != null) {
          switch (wheelchairAccessibleType.intValue()) {
            case 1:
              vehicleDescriptorBuilder.setWheelchairAccessible(GtfsRealtime.VehicleDescriptor.WheelchairAccessible.WHEELCHAIR_ACCESSIBLE);
              break;
            case 2:
              vehicleDescriptorBuilder.setWheelchairAccessible(GtfsRealtime.VehicleDescriptor.WheelchairAccessible.WHEELCHAIR_INACCESSIBLE);
              break;
            default:
              vehicleDescriptorBuilder.setWheelchairAccessible(GtfsRealtime.VehicleDescriptor.WheelchairAccessible.UNKNOWN);
              break;
          }
        } else {
          vehicleDescriptorBuilder.setWheelchairAccessible(GtfsRealtime.VehicleDescriptor.WheelchairAccessible.UNKNOWN);
        }
        vpBuilder.setVehicle(vehicleDescriptorBuilder);

        // Position（position.latitude / position.longitude）
        Float lat = data.getLatitude();
        Float lon = data.getLongitude();
        if (lat == null || lon == null) {
          LOGGER.warn("緯度/経度が null のため スキップします: tripId={}, vehicleId={}", data.getBusServiceTripId(), vehicleId);
          continue;
        }

        GtfsRealtime.Position.Builder posBuilder = GtfsRealtime.Position.newBuilder();
        posBuilder.setLatitude(lat);
        posBuilder.setLongitude(lon);
        vpBuilder.setPosition(posBuilder);

        // timestamp（システム日時）
        vpBuilder.setTimestamp(Instant.now().getEpochSecond());

        // FeedEntity 追加
        String baseTripId = (data.getBusServiceTripId() != null) ? data.getBusServiceTripId() : "unknown";
        String entityId = "vehiclePosition_" + baseTripId;

        GtfsRealtime.FeedEntity.Builder entityBuilder = GtfsRealtime.FeedEntity.newBuilder().setId(entityId).setVehicle(vpBuilder);
        feedBuilder.addEntity(entityBuilder);
      } catch (Exception e) {
        LOGGER.error("VehiclePosition 生成中にエラー: {}", data, e);
      }
    }

    return feedBuilder.build();
  }

  /**
   * null の場合は空文字を返却します。 それ以外の場合は toString() の結果を返却します。
   *
   * @param value 任意オブジェクト
   * @return 空文字または value の文字列表現
   */
  private String nullToEmpty(Object value) {
    return value == null ? "" : value.toString();
  }
}
