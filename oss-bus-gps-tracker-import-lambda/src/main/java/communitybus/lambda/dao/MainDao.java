package communitybus.lambda.dao;

import communitybus.lambda.dto.InputDto;
import communitybus.lambda.dto.OperationAggregateDto;
import communitybus.lambda.dto.VehicleTrackingDto;
import communitybus.lambda.dto.VehiclesDto;
import communitybus.lambda.exception.DatabaseFatalException;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Main DAO
 *
 * @author y.kim
 *
 */
public class MainDao extends BaseDao {

  /**
   * 車両動態登録（7-5：便ID/標柱ID/到着区分/遅延時間を含む）
   *
   * @param sqlSession sqlSession
   * @param dto 車両動態登録用DTO
   */
  public void insertIntoVehicleTrackingWithOperation(SqlSession sqlSession, communitybus.lambda.dto.VehicleTrackingRecordDto dto) {
    try {
      if (insert(sqlSession, "MainMapper.insertIntoVehicleTrackingWithOperation", dto) != 1) {
        throw new DatabaseFatalException("車両動態テーブル登録エラー(7-5) " + dto);
      }
    } catch (Exception e) {
      throw new DatabaseFatalException("車両動態テーブル登録エラー(7-5)", e);
    }
  }

  /**
   * 車両情報情報の取得
   *
   * @param sqlSession sqlSession
   * @param dto request
   */
  public VehiclesDto getVehiclesInfo(SqlSession sqlSession, InputDto dto) {
    try {
      return (VehiclesDto)getObject(sqlSession, "MainMapper.getVehiclesInfo", dto);
    } catch (Exception e) {
      throw new DatabaseFatalException("車両情報情報の取得エラー", e);
    }
  }

  /**
   * 車両動態情報の最新1件を取得
   *
   * @param sqlSession MyBatisのSqlSession
   * @param dto request
   * @return VehicleTrackingDto 最新の車両動態情報
   */
  public VehicleTrackingDto getLatestVehicleTracking(SqlSession sqlSession,InputDto dto) {
    try {
      return (VehicleTrackingDto) getObject(sqlSession, "MainMapper.getLatestVehicleTracking", dto);
    } catch (Exception e) {
      throw new DatabaseFatalException("車両動態情報の取得エラー", e);
    }
  }

  /**
   * 運行情報集約の取得（一覧）（日毎便ダイヤ起点）
   *
   * @param sqlSession sqlSession
   * @param dto パラメータ（例: GPSシリアル、時刻 等）
   * @return List<OperationAggregateDto>
   */
  public List<OperationAggregateDto> getOperationAggregate(SqlSession sqlSession, InputDto dto) {
    try {
      return getList(sqlSession,"MainMapper.getOperationAggregate", dto);
    } catch (Exception e) {
      throw new DatabaseFatalException("運行情報集約の取得エラー", e);
    }
  }

  /**
   * 運行情報集約の取得（一覧）（便.車両ID起点）
   *
   * @param sqlSession sqlSession
   * @param dto パラメータ（例: GPSシリアル、時刻 等）
   * @return List<OperationAggregateDto>
   */
  public List<OperationAggregateDto> getOperationAggregateByBusVehicleId(SqlSession sqlSession, InputDto dto) {
    try {
      return getList(sqlSession,"MainMapper.getOperationAggregateByBusVehicleId", dto);
    } catch (Exception e) {
      throw new DatabaseFatalException("運行情報集約の取得エラー2", e);
    }
  }

  /**
   * アクセスキーが存在するか
   *
   * @param sqlSession sqlSession
   * @param accessKey アクセスキー
   * @return true : 存在しない false:存在する
   */
  public boolean isNotExistAccessKey(SqlSession sqlSession, String accessKey) {
    try {
      return getInteger(sqlSession, "MainMapper.getAccessKey", accessKey) != 1;
    } catch (Exception e) {
      throw new DatabaseFatalException("アクセスキー取得エラー", e);
    }
  }
}
