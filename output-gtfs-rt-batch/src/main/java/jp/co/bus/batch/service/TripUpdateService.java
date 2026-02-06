package jp.co.bus.batch.service;

import com.google.transit.realtime.GtfsRealtime;
import jp.co.bus.batch.dto.TripUpdateContainerDto;
import jp.co.bus.batch.utils.ExObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.List;

/**
 * GTFS-RT出力用のTripUpdateデータを作成するサービスクラス。
 *
 * @author jeon
 */
public class TripUpdateService {

  /** ログ */
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * GTFS-RTフォーマットのTripUpdateデータを作成します。
   *
   * @param tripDataLists 便データリスト
   * @return TripUpdateのFeedMessage
   */
  public GtfsRealtime.FeedMessage createTripUpdateFeed(List<List<TripUpdateContainerDto>> tripDataLists) {
    // 0. FeedMessage Builder
    GtfsRealtime.FeedMessage.Builder feedBuilder = GtfsRealtime.FeedMessage.newBuilder();

    // 1. FeedHeader 設定
    GtfsRealtime.FeedHeader.Builder headerBuilder = GtfsRealtime.FeedHeader.newBuilder();
    headerBuilder.setGtfsRealtimeVersion("2.0");
    headerBuilder.setIncrementality(GtfsRealtime.FeedHeader.Incrementality.FULL_DATASET);
    headerBuilder.setTimestamp(Instant.now().getEpochSecond());
    feedBuilder.setHeader(headerBuilder);

    // Entityの生成
    for (List<TripUpdateContainerDto> tripList : tripDataLists) {
      GtfsRealtime.FeedEntity.Builder entityBuilder = createTripUpdateDetail(tripList);
      if (entityBuilder != null) {
        feedBuilder.addEntity(entityBuilder);
      }
    }
    return feedBuilder.build();
  }

  /**
   * GTFS-RTフォーマットのTripUpdateデータを作成します。
   *
   * @param tripDataList 便データリスト
   * @return TripUpdateのFeedEntity
   */
  public GtfsRealtime.FeedEntity.Builder createTripUpdateDetail(List<TripUpdateContainerDto> tripDataList) {
    if (ExObjectUtils.isBlank(tripDataList)) {
      return null;
    }
    TripUpdateContainerDto firstRow = tripDataList.get(0);

    // TripUpdate Builder
    GtfsRealtime.TripUpdate.Builder tripUpdateBuilder = GtfsRealtime.TripUpdate.newBuilder();

    // TripDescriptor
    GtfsRealtime.TripDescriptor.Builder tripDescriptorBuilder = GtfsRealtime.TripDescriptor.newBuilder();
    tripDescriptorBuilder.setTripId(String.valueOf(firstRow.getTripData().getBusServiceTripId()));
    tripDescriptorBuilder.setScheduleRelationship(GtfsRealtime.TripDescriptor.ScheduleRelationship.SCHEDULED);
    tripUpdateBuilder.setTrip(tripDescriptorBuilder);

    // VehicleDescriptor
    GtfsRealtime.VehicleDescriptor.Builder vehicleDescriptorBuilder = GtfsRealtime.VehicleDescriptor.newBuilder();
    vehicleDescriptorBuilder.setId(firstRow.getVehicleId().toString());
    vehicleDescriptorBuilder.setLabel(firstRow.getVehicleName());
    if (firstRow.getTripData().getWheelchairAccessibleType() != null) {
      switch (firstRow.getTripData().getWheelchairAccessibleType()) {
        case 1:
          vehicleDescriptorBuilder.setWheelchairAccessible(GtfsRealtime.VehicleDescriptor.WheelchairAccessible.WHEELCHAIR_ACCESSIBLE);
          break;
        case 2:
          vehicleDescriptorBuilder.setWheelchairAccessible(GtfsRealtime.VehicleDescriptor.WheelchairAccessible.WHEELCHAIR_INACCESSIBLE);
          break;
        default:
          vehicleDescriptorBuilder.setWheelchairAccessible(GtfsRealtime.VehicleDescriptor.WheelchairAccessible.UNKNOWN);
      }
    } else {
      vehicleDescriptorBuilder.setWheelchairAccessible(GtfsRealtime.VehicleDescriptor.WheelchairAccessible.UNKNOWN);
    }
    tripUpdateBuilder.setVehicle(vehicleDescriptorBuilder);
    tripUpdateBuilder.setTimestamp(Instant.now().getEpochSecond());

    for (TripUpdateContainerDto row : tripDataList) {
      try {
        // StopTimeUpdate
        GtfsRealtime.TripUpdate.StopTimeUpdate.Builder stuBuilder = GtfsRealtime.TripUpdate.StopTimeUpdate.newBuilder();

        if (row.getStopSequence() != null) {
          stuBuilder.setStopSequence(row.getStopSequence());
        }

        if (row.getTripData().getBusStopCode() != null && row.getTripData().getSignpostBranchNumber() != null) {
          stuBuilder.setStopId(row.getTripData().getBusStopCode() + "_" + row.getTripData().getSignpostBranchNumber());
        }

        // stop_time_update.schedule_relationship必須
        stuBuilder.setScheduleRelationship( GtfsRealtime.TripUpdate.StopTimeUpdate.ScheduleRelationship.SCHEDULED);
        // arrival
        GtfsRealtime.TripUpdate.StopTimeEvent.Builder arrivalBuilder = GtfsRealtime.TripUpdate.StopTimeEvent.newBuilder();
        if (row.getActualArrivalTime() != null) {
          arrivalBuilder.setDelay(row.getArrivalDelayTime().intValue());
          arrivalBuilder.setTime(row.getActualArrivalTime().toEpochSecond());
          arrivalBuilder.setUncertainty(0);
        }
        stuBuilder.setArrival(arrivalBuilder);

        // departure
        GtfsRealtime.TripUpdate.StopTimeEvent.Builder departureBuilder = GtfsRealtime.TripUpdate.StopTimeEvent.newBuilder();
        if (row.getActualDepartureTime() != null) {
          departureBuilder.setDelay(row.getDepartureDelayTime().intValue());
          departureBuilder.setTime(row.getActualDepartureTime().toEpochSecond());
          departureBuilder.setUncertainty(0);
        }
        stuBuilder.setDeparture(departureBuilder);

        tripUpdateBuilder.addStopTimeUpdate(stuBuilder);
      } catch (Exception e) {
        LOGGER.error("TripUpdate生成中のエラー: {}", row, e);
      }
    }

    // FeedEntity
    String tripId = tripDescriptorBuilder.getTripId();
    return GtfsRealtime.FeedEntity.newBuilder().setId("tripUpdate_" + tripId).setTripUpdate(tripUpdateBuilder);
  }
}
