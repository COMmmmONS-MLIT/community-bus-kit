package jp.co.bus.batch.dao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import jp.co.bus.batch.dto.*;
import jp.co.bus.batch.exception.DatabaseFatalException;
import org.apache.ibatis.session.SqlSession;

/**
 * Main DAO
 *
 * @author g.ko
 */
public class MainDao extends BaseDao {

  /**
   * 車両情報を条件に基づいて取得する
   *
   * @param sqlSession     sqlSession
   * @param businessCorpId 事業者ID
   * @return 車両情報リスト
   */
  public List<VehicleInfoDto> getVehicleDataByConditions(SqlSession sqlSession, Long businessCorpId) {
    try {
      return getList(sqlSession, "MainMapper.getVehicleDataByConditions", businessCorpId);
    } catch (Exception e) {
      throw new DatabaseFatalException("事業者IDに基づく車両データ取得でエラー発生: businessCorpId=" + businessCorpId, e);
    }
  }

  /**
   * 便データを車両IDに基づいて取得する
   *
   * @param sqlSession sqlSession
   * @param params  車両ID
   * @return 便データリスト
   */
  public List<TripDataDto> getTripDataByVehicle(SqlSession sqlSession, Map<String, Object> params) {
    try {
      List<TripDataDto> result = getList(sqlSession, "MainMapper.getTripDataByVehicle", params);
      if (result == null || result.isEmpty()) {
        return Collections.emptyList();
      }
      return result;
    } catch (Exception e) {
      throw new DatabaseFatalException("車両IDに基づく便データ取得でエラー発生: vehicleId=" + params, e);
    }
  }

  /**
   * 条件に基づいて事業者データを取得する
   *
   * @param sqlSession sqlSession
   * @return 事業者データリスト
   */
  public List<BusServiceInfoDto> getBusinessDataByConditions(SqlSession sqlSession) {
    try {
      return getList(sqlSession, "MainMapper.getBusinessDataByConditions", null);
    } catch (Exception e) {
      throw new DatabaseFatalException("条件に基づく事業者データ取得でエラー発生", e);
    }
  }

  /**
   * 車両動態データを取得する
   *
   * @param sqlSession SqlSession
   * @param params     クエリパラメータ (例: systemDate, currentTime, vehicleId, gpsTrackerSerial)
   * @return VehicleTrackingDto データ
   */
  public List<VehicleTrackingDto> selectVehicleTrackingData(SqlSession sqlSession, Map<String, Object> params) {
    try {
      List<VehicleTrackingDto> data = getList(sqlSession, "MainMapper.selectVehicleTrackingData", params);
      if (data == null) {
        return Collections.emptyList();
      }
      return data;
    } catch (Exception e) {
      throw new DatabaseFatalException("車両動態データ取得でエラー発生: params=" + params, e);
    }
  }
  /**

   * 日毎便ダイヤデータをアラート情報付きで取得する
   *
   * @param sqlSession sqlSession
   * @param params     クエリ条件 (例: businessCorpId, systemDate など)
   * @return 日毎便ダイヤデータリスト
   */
  public List<VehicleTrackingDto> selectDailyBusDaiyaWithAlerts(SqlSession sqlSession, Map<String, Object> params) {
    try {
      List<VehicleTrackingDto> result = getList(sqlSession, "MainMapper.selectDailyBusDaiyaWithAlerts", params);
      if (result == null || result.isEmpty()) {
       return Collections.emptyList();
      }
      return result;
    } catch (Exception e) {
      throw new DatabaseFatalException("日毎便ダイヤデータ取得でエラー発生: params=" + params, e);
    }
  }
}
