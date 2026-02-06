package jp.co.bus.batch.dao;

import java.util.List;

import jp.co.bus.batch.dto.*;
import jp.co.bus.batch.exception.DatabaseFatalException;
import org.apache.ibatis.session.SqlSession;

/**
 * Main DAO
 *
 * @author hr.kim
 */
public class MainDao extends BaseDao {

  /**
   * 便情報取得
   *
   * @param sqlSession SqlSession
   * @return 便情報
   */
  public List<BusServiceInfoDto> getBusServices(SqlSession sqlSession) {
    return getList(sqlSession, "MainMapper.getBusServices", null);
  }

  /**
   * 運行カレンダー情報取得
   *
   * @param sqlSession SqlSession
   * @param busServiceInfoDto 便情報DTO
   * @return 運行カレンダー情報
   */
  public List<CalendarInfoDto> getCalendarsById(SqlSession sqlSession,
      BusServiceInfoDto busServiceInfoDto) {
    return getList(sqlSession, "MainMapper.getCalendarsById", busServiceInfoDto);
  }

  /**
   * 運行カレンダー除外日取得
   *
   * @param sqlSession SqlSession
   * @param busServiceInfoDto 便情報DTO
   * @return 運行カレンダー除外日
   */
  public List<String> getFutureCancelDatesByCalendarId(SqlSession sqlSession,
      BusServiceInfoDto busServiceInfoDto) {
    return getList(sqlSession, "MainMapper.getFutureCancelDatesByCalendarId", busServiceInfoDto);
  }

  /**
   * 運行カレンダー指定日取得
   *
   * @param sqlSession SqlSession
   * @param busServiceInfoDto 便情報DTO
   * @return 運行カレンダー指定日
   */
  public List<String> getFutureDesignatedDatesByCalendarId(SqlSession sqlSession,
      BusServiceInfoDto busServiceInfoDto) {
    return getList(sqlSession, "MainMapper.getFutureDesignatedDatesByCalendarId",
        busServiceInfoDto);
  }

  /**
   * 日毎便ダイヤ取得（開始日～終了日）
   *
   * @param sqlSession SqlSession
   * @param busServiceId 便ID
   * @param startDate 開始日
   * @param endDate 終了日
   * @return 日毎便ダイヤ
   */
  public List<DailyBusDaiyaDto> getDailyBusDaiyasByBusServiceId(SqlSession sqlSession,
      Long busServiceId, String startDate, String endDate) {
    BusServiceInfoDto parameterDto = new BusServiceInfoDto(busServiceId, startDate, endDate);
    return getList(sqlSession, "MainMapper.getDailyBusDaiyasByBusServiceId", parameterDto);
  }

  /**
   * 日毎便ダイヤ取得（対象日）
   *
   * @param sqlSession SqlSession
   * @param busServiceId 便ID
   * @param targetDates 対象日リスト
   * @return 日毎便ダイヤ
   */
  public List<DailyBusDaiyaDto> getDailyBusDaiyasByServiceIdAndDateList(SqlSession sqlSession,
      Long busServiceId, List<String> targetDates) {
    DailyBusDaiyaGetDto parameterDto = new DailyBusDaiyaGetDto(busServiceId, targetDates);
    return getList(sqlSession, "MainMapper.getDailyBusDaiyasByServiceIdAndDateList", parameterDto);
  }

  /**
   * 日毎便ダイヤ取得（対象日以外）
   *
   * @param sqlSession SqlSession
   * @param busServiceId 便ID
   * @param targetDates 対象日リスト
   * @return 日毎便ダイヤ
   */
  public List<Long> getDailyBusDaiyasByServiceIdAndDateListNotIn(SqlSession sqlSession,
      Long busServiceId, List<String> targetDates) {
    DailyBusDaiyaGetDto parameterDto = new DailyBusDaiyaGetDto(busServiceId, targetDates);
    return getList(sqlSession, "MainMapper.getDailyBusDaiyasByServiceIdAndDateListNotIn",
        parameterDto);
  }

  /**
   * 処理対象日が祝日カレンダーに存在するか
   *
   * @param sqlSession SqlSession
   * @param svcDate 運行日
   * @param businessCorpId 事業者ID
   * @return true：登録済み、false：登録されてない
   */
  public boolean isDatePresent(SqlSession sqlSession, String svcDate, Long businessCorpId) {
    HolidayCalendarSearchDto dto = new HolidayCalendarSearchDto(svcDate, businessCorpId);
    return getString(sqlSession, "MainMapper.isDatePresent", dto).equals("1");
  }

  /**
   * 運行カレンダー詳細に指定した曜日区分のデータが存在するかチェック
   *
   * @param sqlSession SqlSession
   * @param calendarId 運行カレンダーID
   * @param dayWeekType 曜日区分
   * @return true：存在、false：存在しない
   */
  public boolean isDayWeekTypePresent(SqlSession sqlSession, Long calendarId, String dayWeekType) {
    CalendarDetailSearchDto dto = new CalendarDetailSearchDto(calendarId, dayWeekType);
    return getString(sqlSession, "MainMapper.isDayWeekTypePresent", dto).equals("1");
  }

  /**
   * 日毎便ダイヤ削除
   *
   * @param sqlSession SqlSession
   * @param dto 日毎便ダイヤ更新・登録用DTO
   */
  public void deleteDailyBusDaiya(SqlSession sqlSession, DailyBusDaiyaUpsertDto dto) {
    if (update(sqlSession, "MainMapper.deleteDailyBusDaiya", dto) != 1) {
      throw new DatabaseFatalException("日毎便ダイヤ削除エラー");
    }
  }

  /**
   * 日毎便ダイヤ登録
   *
   * @param sqlSession SqlSession
   * @param dto 日毎便ダイヤ更新・登録用DTO
   */
  public void insertDailyBusDaiya(SqlSession sqlSession, DailyBusDaiyaUpsertDto dto) {
    if (insert(sqlSession, "MainMapper.insertDailyBusDaiya", dto) != 1) {
      throw new DatabaseFatalException("日毎便ダイヤ登録エラー");
    }
  }

  /**
   * 日毎便ダイヤ更新
   *
   * @param sqlSession SqlSession
   * @param dto 日毎便ダイヤ更新・登録用DTO
   */
  public void updateDailyBusDaiya(SqlSession sqlSession, DailyBusDaiyaUpsertDto dto) {
    if (update(sqlSession, "MainMapper.updateDailyBusDaiya", dto) != 1) {
      throw new DatabaseFatalException("日毎便ダイヤ更新エラー");
    }
  }

}
