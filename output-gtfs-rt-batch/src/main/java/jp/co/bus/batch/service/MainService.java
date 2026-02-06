package jp.co.bus.batch.service;

import com.google.transit.realtime.GtfsRealtime;
import jp.co.bus.batch.dao.CommonDao;
import jp.co.bus.batch.dao.MainDao;
import jp.co.bus.batch.dto.*;
import jp.co.bus.batch.factory.SessionFactory;
import jp.co.bus.batch.utils.ExObjectUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main service
 *
 * @author jeon
 */
public class MainService {

  /** log */
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /** SessionFactory */
  private final SessionFactory sessionFactory;

  /** MainDao */
  private final MainDao mainDao;

  /** commonDao */
  private final CommonDao commonDao;

  /** HHmm 形式フォーマッタ */
  private static final DateTimeFormatter HHMM_FORMATTER = DateTimeFormatter.ofPattern("HHmm");

  /**
   * コンストラクタ
   *
   * @param sessionFactory SessionFactory
   */
  public MainService(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
    this.commonDao = new CommonDao();
    this.mainDao = new MainDao();
  }

  /**
   * バッチ処理を開始する。
   *
   * GTFS-RT形式のTripUpdate / VehiclePosition / Alertの各フィードを生成し、 必要に応じてS3へ出力する。
   */
  public void execute() {

    LOGGER.info("処理を開始します");

    try (SqlSession sqlSession = sessionFactory.getSession()) {
      // 事業者データ取得
      List<BusServiceInfoDto> businessData = fetchBusinessData(sqlSession);
      if (businessData.isEmpty()) {
        LOGGER.warn("事業者データが取得できませんでした。");
        throw new Exception();
      }

      // 事業者ごとの処理
      for (BusServiceInfoDto business : businessData) {
        String hhmm = LocalTime.now().format(HHMM_FORMATTER);
        processVehicles(sqlSession, business.getBusinessCorpId(), hhmm);
      }
    } catch (Exception e) {
      LOGGER.error("処理中にエラーが発生しました", e);
    }
  }

  /**
   * 事業者データを取得する。
   *
   * @param sqlSession SQLセッション
   * @return 事業者データリスト
   */
  private List<BusServiceInfoDto> fetchBusinessData(SqlSession sqlSession) {
    LOGGER.info("事業者データを取得中...");
    try {
      List<BusServiceInfoDto> list = mainDao.getBusinessDataByConditions(sqlSession);
      LOGGER.info("事業者データ取得成功: 件数={}", list.size());
      return list;
    } catch (Exception e) {
      LOGGER.error("事業者データ取得中にエラーが発生しました", e);
      throw e;
    }
  }

  /**
   * 事業者に紐づく車両データを処理する。
   *
   * @param sqlSession     SQLセッション
   * @param businessCorpId 事業者ID
   * @param hhmm           HHmm形式の現在時刻
   */
  private void processVehicles(SqlSession sqlSession, Long businessCorpId, String hhmm) {

    LOGGER.info("事業者[{}] の車両処理開始", businessCorpId);

    try {
      // 車両データを取得する
      List<VehicleInfoDto> vehicles = fetchVehicleData(sqlSession, businessCorpId);

      List<List<TripUpdateContainerDto>> allContainers = new ArrayList<>();
      List<VehicleTrackingDto> allVehiclePositions = new ArrayList<>();
      List<VehicleTrackingDto> allAlerts = new ArrayList<>();

      // Mapでパラメータを設定
      Map<String, Object> params = new HashMap<>();

      // 結果が空の場合、警告ログを出力して処理をスキップ
      if (ExObjectUtils.isNotBlank(vehicles)) {
        // 各車両データをループして処理
        for (VehicleInfoDto vehicle : vehicles) {
          // 車両IDを取得
          Long vehicleId = vehicle.getVehicleId();
          if (vehicleId == null) {
            continue;
          }
          if (vehicle.getGpsSerial() == null) {
            continue;
          }

          params.clear();
          params.put("vehicleId", vehicleId);
          params.put("gpsSerial", vehicle.getGpsSerial());
          params.put("vehicleName", vehicle.getVehicleName());
          params.put("businessCorpId", businessCorpId);
          params.put("hhmm", hhmm);

          // TripUpdateデータの処理
          processTripUpdate(sqlSession, params, allContainers);
          // 車両位置データの処理
          VehiclePosition(sqlSession, params, allVehiclePositions);
        }
      }

      // アラートデータの処理
      processAlerts(sqlSession, params, allAlerts);

      if (!allContainers.isEmpty()) {
        GtfsRealtime.FeedMessage feed = new TripUpdateService().createTripUpdateFeed(allContainers);
        saveS3UpdateData(feed, params, "TripUpdate");
      }

      if (!allVehiclePositions.isEmpty()) {
        GtfsRealtime.FeedMessage feed = new VehiclePositionService().createVehiclePositionFeed(allVehiclePositions);
        saveS3UpdateData(feed, params, "VehiclePosition");
      }

      if (!allAlerts.isEmpty()) {
        GtfsRealtime.FeedMessage alertFeed = new AlertService().createAlertFeed(allAlerts);
        saveS3UpdateData(alertFeed, params, "Alert");
      }

    } catch (Exception e) {
      // エラーログを出力
      LOGGER.error("事業者[{}] の車両処理中にエラーが発生しました", businessCorpId, e);
    }
  }

  /**
   * 事業者に紐づく車両データを取得する。
   *
   * @param sqlSession     SQLセッション
   * @param businessCorpId 事業者ID
   * @return 車両データリスト
   */
  private List<VehicleInfoDto> fetchVehicleData(SqlSession sqlSession, Long businessCorpId) {
    LOGGER.info("車両データを取得中: businessCorpId={}", businessCorpId);
    return mainDao.getVehicleDataByConditions(sqlSession, businessCorpId);
  }

  /**
   * TripUpdateデータを処理する。
   *
   * @param sqlSession    SQLセッション
   * @param params        パラメータ
   * @param allContainers 事業者単位でまとめるコンテナリスト
   */
  private void processTripUpdate(SqlSession sqlSession, Map<String, Object> params, List<List<TripUpdateContainerDto>> allContainers) {

    LOGGER.info("TripUpdate処理: params={}", params);

    try {
      List<TripDataDto> tripDataList = mainDao.getTripDataByVehicle(sqlSession, params);

      if (tripDataList == null || tripDataList.isEmpty()) {
        LOGGER.info("Tripデータなし: params={}", params);
        return;
      }

      List<TripUpdateContainerDto> containers = createOperationStatusContainers(tripDataList, params);
      allContainers.add(containers);
    } catch (Exception e) {
      LOGGER.error("TripUpdate処理エラー: params={}", params, e);
    }
  }

  /**
   * VehiclePositionデータを処理する。
   *
   * @param sqlSession          SQLセッション
   * @param params              パラメータ
   * @param allVehiclePositions 事業者単位でまとめる位置情報リスト
   */
  private void VehiclePosition(SqlSession sqlSession, Map<String, Object> params, List<VehicleTrackingDto> allVehiclePositions) {

    try {
      List<VehicleTrackingDto> vehicleDataList = mainDao.selectVehicleTrackingData(sqlSession, params);

      if (vehicleDataList == null || vehicleDataList.isEmpty()) {
        LOGGER.info("VehiclePosition対象データなし: params={}", params);
        return;
      }

      // 車両1台につき最新1件だけ使用
      VehicleTrackingDto latest = vehicleDataList.get(0);
      latest.setVehicleId(Long.valueOf(params.get("vehicleId").toString()));
      latest.setVehicleName(params.get("vehicleName").toString());

      // 事業者単位でまとめるリストへ追加
      allVehiclePositions.add(latest);

    } catch (Exception e) {
      LOGGER.error("VehiclePosition収集中エラー: params={}", params, e);
    }
  }

  /**
   * Alertデータを処理する。
   *
   * @param sqlSession SQLセッション
   * @param params     パラメータ
   * @param allAlerts  事業者単位でまとめるアラートリスト
   */
  private void processAlerts(SqlSession sqlSession, Map<String, Object> params, List<VehicleTrackingDto> allAlerts) {

    LOGGER.info("Alert収集開始: params={}", params);

    try {
      List<VehicleTrackingDto> alerts = mainDao.selectDailyBusDaiyaWithAlerts(sqlSession, params);

      if (alerts == null || alerts.isEmpty()) {
        LOGGER.info("Alert対象データなし: params={}", params);
        return;
      }

      LOGGER.debug("Alert件数: {}", alerts.size());
      allAlerts.addAll(alerts);

    } catch (Exception e) {
      LOGGER.error("Alert収集中エラー: params={}", params, e);
    }
  }

  /**
   * 運行状況管理用コンテナを作成する。
   *
   * @param tripDataList TripDataリスト
   * @param params パラメータリスト（車両情報）
   * @return コンテナリスト
   */
  private List<TripUpdateContainerDto> createOperationStatusContainers(List<TripDataDto> tripDataList, Map<String, Object> params) {

    LOGGER.info("運行状況コンテナ作成開始: 件数={}", tripDataList.size());
    List<TripUpdateContainerDto> containers = new ArrayList<>();

    // ③ 遅延時間がNullの場合、直前に処理された遅延時間をセットするための保持領域
    Long lastDelay = 0L;

    for (TripDataDto trip : tripDataList) {
      TripUpdateContainerDto container = new TripUpdateContainerDto();

      // ② 便/系統/停留所情報をコンテナにセット
      container.setTripData(trip);
      container.setBusServiceId(trip.getBusServiceId());
      container.setStopSequence(trip.getStopSequence());
      container.setSignpostId(trip.getSignpostId());
      container.setScheduledArrivalTime(trip.getScheduledArrivalTimeParsed());
      container.setScheduledDepartureTime(trip.getScheduledDepartureTimeParsed());
      container.setVehicleId(Long.valueOf(params.get("vehicleId").toString()));
      container.setVehicleName(params.get("vehicleName").toString());

      // ② 到着/出発遅延を元データから取得
      Long arrivalDelay = trip.getArrivalDelay() != null ? Long.valueOf(trip.getArrivalDelay()) : null;
      Long departureDelay = trip.getDepartureDelay() != null ? Long.valueOf(trip.getDepartureDelay()) : null;

      // ② 実績時刻（到着/出発）を取得
      OffsetDateTime actualArrivalTime = trip.getActualArrivalTimeParsed();
      OffsetDateTime actualDepartureTime = trip.getActualDepartureTimeParsed();

      // ③ 通過扱い（設計書の注意事項）
      if (actualArrivalTime == null && actualDepartureTime != null) {
        // ③ 通過扱い：到着遅延は出発遅延で代用
        arrivalDelay = departureDelay;
      }

      // ③ 遅延時間がNullの場合、直前の遅延時間をセット
      if (arrivalDelay == null) {
        arrivalDelay = lastDelay;
      } else {
        lastDelay = arrivalDelay;
      }

      if (departureDelay == null) {
        departureDelay = lastDelay;
      } else {
        lastDelay = departureDelay;
      }

      // ③ 実績時刻がNullの場合、予定時刻 + 遅延時間 で補完
      if (actualArrivalTime == null) {
        actualArrivalTime = addDelayToTime(trip.getScheduledArrivalTimeParsed(), arrivalDelay);
      }

      if (actualDepartureTime == null) {
        actualDepartureTime = addDelayToTime(trip.getScheduledDepartureTimeParsed(), departureDelay);
      }

      // ②＋③ コンテナに最終値を設定
      container.setActualArrivalTime(actualArrivalTime);
      container.setActualDepartureTime(actualDepartureTime);
      container.setArrivalDelayTime(arrivalDelay);
      container.setDepartureDelayTime(departureDelay);

      containers.add(container);
    }

    LOGGER.info("運行状況コンテナ作成完了: 件数={}", containers.size());
    return containers;
  }

  /**
   * 予定時刻に遅延時間を加算する。
   *
   * @param scheduledTime 予定時刻
   * @param delay         遅延時間（秒）
   * @return 加算後のHHmm形式の時刻
   */
  private OffsetDateTime addDelayToTime(OffsetDateTime scheduledTime, Long delay) {
    if (scheduledTime == null || delay == null) {
      return null;
    }
    try {
      return scheduledTime.plusSeconds(delay);
    } catch (Exception e) {
      LOGGER.error("時刻計算中にエラー: scheduledTime={}, delay={}", scheduledTime, delay, e);
      return null;
    }
  }

  /**
   * フィードデータをS3へ保存する。
   *
   * @param tripUpdate 保存対象のFeedMessage
   * @param params     パラメータ
   * @param prefix     ファイル名プレフィックス
   */
  private void saveS3UpdateData(GtfsRealtime.FeedMessage tripUpdate, Map<String, Object> params, String prefix) {

    // ファイル名を生成
    String fileName = prefix + ".bin";

    // S3 へアップロードするためのオブジェクトキーを作成
    String objectKey = params.get("businessCorpId").toString() + "/" + fileName;

    // S3アップロード処理を実行
    S3UploadService s3 = new S3UploadService();
    s3.uploadFeed(tripUpdate, objectKey);
  }
}
