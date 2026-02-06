package maint.mapper;

import java.util.List;
import maint.dto.operation.holidayCalendars.HolidayCalendarCsvDto;
import maint.dto.operation.holidayCalendars.HolidayCalendarInfoDto;
import maint.session.UserSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 祝日カレンダーMAPPER
 *
 * @author g.ko
 */
@Mapper
public interface HolidayCalendarMapper {

  /**
   * 指定した日付を基準として前月〜2ヶ月後までの祝日カレンダーを取得
   *
   * @param targetDate 指定日(yyyy-MM-dd)
   * @param businessCorpId 事業者ID
   * @return 祝日カレンダー情報リスト
   */
  List<HolidayCalendarInfoDto> getHolidayCalendarByTargetDate(String targetDate,
      Long businessCorpId);

  /**
   * 登録
   *
   * @param holidayCalendarCsvDto 祝日カレンダーDTO
   * @return 登録数
   */
  int registerHoliday(HolidayCalendarCsvDto holidayCalendarCsvDto);

  /**
   * 事業者IDから通常運行日（平日・土日など）の情報を取得
   *
   * @param businessCorpId 事業者ID
   * @return 祝日リスト（YYYYMMDD形式）
   */
  List<String> getCalendarDatesList(@Param("businessCorpId") Long businessCorpId);

  /**
   * 削除
   *
   * @param session ユーザーセッション
   */
  void deleteHoliday(@Param("session") UserSession session);

  /**
   * 日付存在チェック
   *
   * @param date 日付
   * @param businessCorpId 事業者ID
   * @return true : あり、false : なし
   */
  boolean isDatePresent(@Param("date") String date, @Param("businessCorpId") Long businessCorpId);

}
