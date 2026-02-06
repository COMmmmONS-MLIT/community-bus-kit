
package jp.co.bus.batch.service;

import com.google.transit.realtime.GtfsRealtime;
import jp.co.bus.batch.dto.VehicleTrackingDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * GTFS-RT形式のAlertデータを生成するサービスクラス。
 *
 * @author jeon
 */
public class AlertService {

  /** ログ */
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * GTFS-RT形式のAlertデータを作成します。
   *
   * @param tripDataList アラート対象データリスト
   * @return 生成されたAlertフィード
   */
  public GtfsRealtime.FeedMessage createAlertFeed(List<VehicleTrackingDto> tripDataList) {
    // 1. FeedMessage Builder
    GtfsRealtime.FeedMessage.Builder feedBuilder = GtfsRealtime.FeedMessage.newBuilder();

    // 2. Header 設定
    GtfsRealtime.FeedHeader.Builder headerBuilder = GtfsRealtime.FeedHeader.newBuilder();
    headerBuilder.setGtfsRealtimeVersion("2.0");
    headerBuilder.setIncrementality(GtfsRealtime.FeedHeader.Incrementality.FULL_DATASET);
    headerBuilder.setTimestamp(Instant.now().getEpochSecond());
    feedBuilder.setHeader(headerBuilder);

    // 3. Alert Entity 作成
    int idx = 1;

    for (VehicleTrackingDto data : tripDataList) {
      try {
        // Alert 本体
        GtfsRealtime.Alert.Builder alertBuilder = GtfsRealtime.Alert.newBuilder();

        // (1) active_period
        if (data.getServiceDate() != null) {
          GtfsRealtime.TimeRange.Builder timeRangeBuilder = GtfsRealtime.TimeRange.newBuilder();

          // 当日の 23:59 を終了時刻として設定
          timeRangeBuilder.setEnd(getEndOfDayTimestamp(data.getServiceDate()));

          alertBuilder.addActivePeriod(timeRangeBuilder);
        }

        // (2) informed_entity
        if (data.getBusServiceTripId() != null) {
          GtfsRealtime.EntitySelector.Builder entitySelectorBuilder = GtfsRealtime.EntitySelector.newBuilder();

          GtfsRealtime.TripDescriptor.Builder tripDescriptorBuilder = GtfsRealtime.TripDescriptor.newBuilder();
          tripDescriptorBuilder.setTripId(data.getBusServiceTripId());

          entitySelectorBuilder.setTrip(tripDescriptorBuilder);
          alertBuilder.addInformedEntity(entitySelectorBuilder);
        }

        // (3) header_text / (4) description_text
        String notification = data.getNotification();
        if (notification != null) {
          // header_text
          alertBuilder.setHeaderText(buildJapaneseTranslatedText(notification));
          // description_text
          alertBuilder.setDescriptionText(buildJapaneseTranslatedText(notification));
        }

        // (5) FeedEntity に追加
        GtfsRealtime.FeedEntity.Builder entityBuilder = GtfsRealtime.FeedEntity.newBuilder();

        String entityId = "alert_" + idx;
        entityBuilder.setId(entityId);
        entityBuilder.setAlert(alertBuilder);

        feedBuilder.addEntity(entityBuilder);

        LOGGER.debug("生成されたAlertデータ: {}", alertBuilder.build());
        idx++;
      } catch (Exception e) {
        LOGGER.error("Alertデータ作成中にエラーが発生しました: {}", data, e);
      }
    }

    // 4. 完成した FeedMessage を返す
    return feedBuilder.build();
  }

  /**
   * 指定された日付のタイムスタンプを取得します。
   *
   * @param date 運行開始日時
   * @return その日の23:59:59のUnixタイム（秒単位）
   */
  private long getEndOfDayTimestamp(String date) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      LocalDateTime endOfDay = LocalDateTime.parse(date, formatter);
      return endOfDay.toEpochSecond(ZoneOffset.ofHours(9));  // JST
    } catch (Exception e) {
      LOGGER.error("End of day timestamp計算エラー date={}", date, e);
      return 0L;
    }
  }

  /**
   * 日本語テキストから TranslatedString を生成します。
   *
   * @param text 通知メッセージ（日本語）
   * @return GTFS-RT形式のTranslatedString
   */
  private GtfsRealtime.TranslatedString buildJapaneseTranslatedText(String text) {
    GtfsRealtime.TranslatedString.Translation.Builder translationBuilder =
        GtfsRealtime.TranslatedString.Translation.newBuilder().setText(text).setLanguage("ja");

    return GtfsRealtime.TranslatedString.newBuilder().addTranslation(translationBuilder).build();
  }
}
