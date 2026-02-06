package communitybus.lambda.service;


import communitybus.lambda.dao.CommonDao;
import communitybus.lambda.dao.MainDao;
import communitybus.lambda.dto.*;
import communitybus.lambda.factory.SessionFactory;
import communitybus.lambda.utils.HaversineUtils;
import communitybus.lambda.enums.JapanPositioningFlag;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.*;
import java.util.*;

/**
 * Main service
 *
 * @author y.kim
 */
public class MainService {

  /** Logger */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /** SessionFactory */
  private final SessionFactory sessionFactory;

  private final CommonDao commonDao = new CommonDao();

  /** Main DAO */
  private final MainDao mainDao = new MainDao();

  /**
   * コンストラクタ
   *
   * @param sessionFactory SessionFactory
   */
  public MainService(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  /**
   * 処理実行
   *
   * @param inputDto 入力DTO
   * @throws Exception Exception
   */
  public void execute(InputDto inputDto) throws Exception {
    SqlSession session = sessionFactory.getSession();
    try {
      LOGGER.info("getVehicles 呼出 実行");
      VehiclesDto vehiclesdto = mainDao.getVehiclesInfo(session, inputDto);
      inputDto.setVehicleId(vehiclesdto.getVehicleId());
      // 車両マスタ.日本測地フラグが1（日本測地系）の場合、リクエストの緯度、経度を世界測地系に変換して後続の処理を実施
      if (vehiclesdto.getJapanPositioningFlag().equals(JapanPositioningFlag.JAPAN_POSITIONING.getValue())) {
        Double lat = inputDto.getLatitude();
        Double lon = inputDto.getLongitude();
        if (lat != null && lon != null) {
          double latW = lat - 0.00010695 * lat + 0.000017464 * lon + 0.0046017;
          double lonW = lon - 0.000046038 * lat - 0.000083043 * lon + 0.010040;
          //小数点12桁まで(第13位を四捨五入)
          String latW12 = String.format("%.12f", latW);
          String lonW12 = String.format("%.12f", lonW);
          inputDto.setLatitude(Double.valueOf(latW12));
          inputDto.setLongitude(Double.valueOf(lonW12));
          inputDto.setLatitudeLongitude(inputDto.getLatitude() + "," + inputDto.getLongitude());
          LOGGER.info("日本測地系→世界測地系 変換: {} -> {}", lat + "," + lon, inputDto.getLatitudeLongitude());
        }
      }
      LOGGER.info("getOperationAggregate 呼出 実行");
      List<OperationAggregateDto> operationAggregateDtoList;
      List<OperationStatusContainerDto> containers;
      // 日毎便ダイヤの車両IDを条件に便情報を取得
      operationAggregateDtoList = mainDao.getOperationAggregate(session, inputDto);
      containers = mapToStatusContainers(operationAggregateDtoList, inputDto);
      // 日毎便ダイヤに該当データがない場合、便の車両IDを条件に取得し直す
      if (containers.isEmpty()) {
        operationAggregateDtoList = mainDao.getOperationAggregateByBusVehicleId(session, inputDto);
        containers = mapToStatusContainers(operationAggregateDtoList, inputDto);
      }
      LOGGER.info("getLatestVehicleTracking 呼出 実行");
      VehicleTrackingDto vehicleTrackingDto = mainDao.getLatestVehicleTracking(session, inputDto);
      LOGGER.info("getVehicles 呼出 完了");

      if (!containers.isEmpty()) {
        // 7-4) 7-2)③の運行状況管理用コンテナの件数分、運行順序の昇順で以下の処理を繰り返す
        // 運行順序 昇順
        containers.sort(Comparator.comparing(OperationStatusContainerDto::getOperationOrder,
            Comparator.nullsLast(Integer::compareTo)));
        // ① ループ外の変数として現在遅延時間を作成し、Nullを設定する
        Long currentDelaySec = null;
        for (OperationStatusContainerDto c : containers) {
          // ② 出発時刻（実績）がNullでない場合、continue
          if (c.getActualDepartureTime() != null)
            continue;
          // ③ 到着時刻（実績）がNullの場合
          // ii) 到着判定緯度、経度とRequest.緯度、経度から距離を算出する
          // 到着判定用の緯度・経度を取得
          Double[] sp = parseLatLon(c.getArrivalJudgeLatitudeLongitude());
          Double lat = inputDto.getLatitude();
          Double lon = inputDto.getLongitude();
          // 距離計算（sp, lat, lonがすべて存在する場合のみ）
          Double dist = null;
          if (sp != null && lat != null && lon != null) {
            dist = HaversineUtils.calculate_m(sp[0], sp[1], lat, lon);  // 単位（m換算なら×1000）
          }
          // 到着判定半径（nullなら0）
          double radius =
              (c.getArrivalPointRadius() != null) ? c.getArrivalPointRadius().doubleValue() : 0d;
          // Request送信日時
          OffsetDateTime reqTime = (inputDto.getCreatedDatetimeParsed() != null) ?
              inputDto.getCreatedDatetimeParsed() :
              null;
          // ③ 到着時刻（実績）がNullの場合
          if (c.getActualArrivalTime() == null) {
            // i) 現在遅延時間がNullでない場合、到着遅延時間に現在遅延時間をセットする
            if (currentDelaySec != null) {
              c.setArrivalDelaySeconds(currentDelaySec);
              // ④に進む
            } // iii) 到着判定半径 ≧ i)の距離　の場合、標柱への到着処理を行う
            else if (dist != null && dist <= radius) {
              currentDelaySec = delaySeconds(c.getScheduledArrivalTime(),
                  reqTime); // 遅延時間を算出し、現在遅延時間に格納する (Request.送信日時 - 到着時刻（予定）)
              c.setArrivalDelaySeconds(currentDelaySec);       // 到着遅延時間に算出した現在遅延時間をセットする
              c.setActualArrivalTime(reqTime);                 // 到着時刻（実績）にRequest.送信日時をセットする
              c.setVehicleTrackingArrivalDepartureDivision(1); // 新規到着区分に「1:到着」をセットする
            }
          }
          // ④ 出発時刻（実績）がNullの場合
          if (c.getActualDepartureTime() == null) {
            // i) 現在遅延時間がNullでない場合、出発遅延時間に現在遅延時間をセットする
            if (currentDelaySec != null) {
              c.setDepartureDelaySeconds(currentDelaySec);
              continue;
            }
            // ii) 到着判定緯度、経度とRequest.緯度、経度から距離を算出する
            if (dist == null)
              continue;
            // iii) 到着判定半径 < i)の距離、かつ到着時刻（実績）がNullでない場合、標柱からの出発処理を行う
            if (dist > radius && c.getActualArrivalTime() != null) {
              currentDelaySec = delaySeconds(c.getScheduledDepartureTime(),
                  reqTime); // 遅延時間を算出し、現在遅延時間に格納する （Request.送信日時 - 出発時刻（予定））
              c.setDepartureDelaySeconds(currentDelaySec);     // 出発遅延時間に算出した現在遅延時間をセットする
              c.setActualDepartureTime(reqTime);               // 出発時刻（実績）にRequest.送信日時をセットする
              c.setVehicleTrackingArrivalDepartureDivision(2); // 新規到着区分に「2:出発」をセットする
            }

            if (vehicleTrackingDto != null) {
              Optional<Double[]> prevLocData =
                Optional.ofNullable(vehicleTrackingDto.getLatitudeLongitude())
                    .filter(s -> !s.isBlank()).map(s -> {
                      try {
                        return Arrays.stream(s.split(",")).map(String::trim).map(Double::parseDouble)
                            .toArray(Double[]::new);
                      } catch (NumberFormatException e) {
                        return null;
                      }
                    });
              Double[] prevLoc = prevLocData.orElse(null);
              // iv) 到着判定半径 < ii)の距離、かつ到着時刻（実績）がNullの場合
              if (dist > radius && c.getActualArrivalTime() == null && sp != null && prevLoc != null) {
                // 7-3)の緯度、経度と到着判定緯度、経度から距離を算出する
                // 前回位置から標柱までの距離
                double prevDist = HaversineUtils.calculate_m(sp[0], sp[1], prevLoc[0], prevLoc[1]);
                // 前回位置からの距離 < 今回位置からの距離（ii)で算出）の場合、到着判定範囲に入らずに標柱を通過したと見なして到着処理と出発処理を行う
                if (prevDist < dist) {
                  // 遅延時間を算出し、現在遅延時間に格納する
                  currentDelaySec = delaySeconds(c.getScheduledDepartureTime(),
                      reqTime);  // Request.送信日時 - 出発時刻（予定）
                  c.setArrivalDelaySeconds(currentDelaySec);       // 到着遅延時間と出発遅延時間に算出した現在遅延時間をセットする
                  c.setDepartureDelaySeconds(currentDelaySec);
                  c.setActualDepartureTime(reqTime);               // 出発時刻（実績）にRequest.送信日時をセットする
                  c.setVehicleTrackingArrivalDepartureDivision(2); // 新規到着区分に「2:出発」をセットする
                }
              }
            }
          }
        }
      }
      LOGGER.info("startTransaction 呼出");
      commonDao.startTransaction(session);
      VehicleTrackingRecordDto rec = getVehicleTrackingRecordDto(inputDto);
      // 7-5) 車両動態の登録（7-4 の結果から、新規到着区分が Null でないものを登録）
      for (OperationStatusContainerDto c : containers) {
        Integer division = c.getVehicleTrackingArrivalDepartureDivision();
        if (division == null) {
          continue; // 到着区分がない場合はスキップ
        }
        setSignpostData(rec, c, division);
      }
      // 1件DB登録
      mainDao.insertIntoVehicleTrackingWithOperation(session, rec);
      LOGGER.info("commit 呼出");
      commonDao.commit(session);
      LOGGER.info("commit 完了");
    } catch (Exception e) {
      LOGGER.error("execute() 発生, rollback", e);
      commonDao.rollback(session);
      throw e;
    } finally {
      session.close();
      LOGGER.info("SqlSession close");
    }
  }

   /**
   * 車両動態レコードDTOを生成します。
   *
   * @param inputDto リクエスト情報を保持するDTO
   * @return 車両動態登録用 DTO
   */
  private static VehicleTrackingRecordDto getVehicleTrackingRecordDto(InputDto inputDto) {
    VehicleTrackingRecordDto rec = new VehicleTrackingRecordDto();
    // Request情報設定
    rec.setGpsSerial(inputDto.getGpsSerial());
    rec.setWorkNumber(inputDto.getWorkNumber());
    rec.setLatitudeLongitude(inputDto.getLatitudeLongitude());
    rec.setDirection(inputDto.getDirection());
    rec.setCreatedDatetimeParsed(inputDto.getCreatedDatetimeParsed());
    rec.setArrivalDivision(0); // default値をセット
    return rec;
  }

  /**
   * 車両動態レコードDTOに標柱到着情報をセットします。
   *
   * @param rec 車両動態登録用DTO
   * @param c 運行状況管理用コンテナDTO
   * @param division 到着区分
   */
  private static void setSignpostData(VehicleTrackingRecordDto rec, OperationStatusContainerDto c, Integer division) {
    Long delay = (division == 1) ? c.getArrivalDelaySeconds() : c.getDepartureDelaySeconds();
    // 7-4)の結果設定
    rec.setBusServiceId(c.getBusServiceId());
    rec.setSignpostId(c.getSignpostId());
    rec.setArrivalDivision(division);
    rec.setDelaySeconds(delay != null ? delay : 0L);
  }

  /**
   * 運行情報集約リスト（OperationAggregateDto）を運行状況管理用コンテナ
   * （OperationStatusContainerDto）リストへ変換します。
   * この際、運行中の便以外のデータは除外されます。
   *
   * @param list 運行情報集約DTOのリスト
   * @param inputDto リクエスト情報
   * @return 変換後の運行状況管理用コンテナDTOのリスト
   */
  private List<OperationStatusContainerDto> mapToStatusContainers(List<OperationAggregateDto> list, InputDto inputDto) {
    if (list == null || list.isEmpty()) {
      return Collections.emptyList();
    }
    List<OperationStatusContainerDto> result = new ArrayList<>(list.size());
    List<OperationStatusContainerDto> tmpResult = new ArrayList<>(list.size());
    Integer beforeBusServiceId = -1;
    boolean skipFlag = false;
    for (OperationAggregateDto oa : list) {
      if (!oa.getBusServiceId().equals(beforeBusServiceId)) {
        // 前の便が終点停留所に到着済みの場合、その便は処理対象外
        if (!tmpResult.isEmpty() && tmpResult.get(tmpResult.size() - 1).getActualArrivalTime() == null) {
          result.addAll(tmpResult);
        }
        beforeBusServiceId = oa.getBusServiceId();
        tmpResult.clear();
        // 始点の到着時刻を確認し、運行前の便であればスキップ
        skipFlag = oa.getScheduledArrivalTimeParsed().isAfter(inputDto.getCreatedDatetimeParsed());
      }
      if (skipFlag) continue;

      OperationStatusContainerDto dto = new OperationStatusContainerDto();
      // 便
      dto.setBusServiceId(oa.getBusServiceId());
      // 系統停留所
      dto.setSignpostId(oa.getSignpostId());
      dto.setOperationOrder(oa.getStopSequence());
      // 標柱
      dto.setArrivalJudgeLatitudeLongitude(oa.getArrivalJudgeLatitude() + "," + oa.getArrivalJudgeLongitude());
      dto.setArrivalPointRadius(oa.getArrivalPointRadius());
      dto.setSignpostBranch(oa.getSignpostBranchNumber());
      // 停留所
      dto.setStopCode(oa.getBusStopCode());
      // 予定
      dto.setScheduledArrivalTime(oa.getScheduledArrivalTimeParsed());
      dto.setScheduledDepartureTime(oa.getScheduledDepartureTimeParsed());
      // 実績
      // 到着判定が起きずに通過してしまう可能性もあるため、出発が非Nullで到着がNullなら到着は出発と同じ遅延としておく
      if(oa.getActualDepartureTimeParsed() != null && oa.getActualArrivalTimeParsed() == null) {
        // 実績（車両動態）
        // 到着は予定時刻を出発の遅延分ずらした時刻
        dto.setActualArrivalTime(oa.getScheduledArrivalTimeParsed().plusSeconds(oa.getDepartureDelaySeconds()));
        dto.setActualDepartureTime(oa.getActualDepartureTimeParsed());
        // 遅延（車両動態）
        dto.setArrivalDelaySeconds(oa.getDepartureDelaySeconds());
        dto.setDepartureDelaySeconds(oa.getDepartureDelaySeconds());
      } else {
        // 実績（車両動態）
        dto.setActualArrivalTime(oa.getActualArrivalTimeParsed());
        dto.setActualDepartureTime(oa.getActualDepartureTimeParsed());
        // 遅延（車両動態）
        dto.setArrivalDelaySeconds(oa.getArrivalDelaySeconds());
        dto.setDepartureDelaySeconds(oa.getDepartureDelaySeconds());
      }
      tmpResult.add(dto);
    }
    if (!tmpResult.isEmpty() && tmpResult.get(tmpResult.size() - 1).getActualArrivalTime() == null) {
      result.addAll(tmpResult);
    }
    return result;
  }

  /**
   * "緯度,経度" 形式の文字列を解析し、Double配列 [lat, lon] を返却します。
   *
   * @param s 緯度経度文字列
   * @return Double[]{lat, lon}／判定不可の場合は null
   */
  private static Double[] parseLatLon(String s) {
    if (s == null || s.isEmpty()) return null;
    String[] p = s.split(",");
    if (p.length != 2) return null;
    try { return new Double[]{ Double.valueOf(p[0].trim()), Double.valueOf(p[1].trim()) }; }
    catch (NumberFormatException e) { return null; }
  }

  /**
   * 予定時刻（文字列）と実績時刻（文字列）を比較し、遅延秒数を計算します。
   *
   * @param scheduledDateTime 予定の日時
   * @param reqDateTime       実績時刻
   * @return 遅延秒
   */
  public long delaySeconds(OffsetDateTime scheduledDateTime, OffsetDateTime reqDateTime) {
    return Duration.between(scheduledDateTime, reqDateTime).getSeconds();
  }

  /**
   * アクセスキーが存在するか
   *
   * @param accessKey アクセスキー
   * @return true : 存在しない false:存在する
   */
  public boolean isNotExistAccessKey(String accessKey) {
    try (SqlSession session = sessionFactory.getSession()) {  // try-with-resources
      return mainDao.isNotExistAccessKey(session, accessKey);
    }
  }
}
