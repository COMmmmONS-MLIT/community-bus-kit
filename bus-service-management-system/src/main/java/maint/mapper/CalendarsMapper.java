package maint.mapper;

import java.util.List;
import maint.dto.operation.calendars.CalendarDto;
import maint.dto.operation.calendars.SearchCalendarsDto;
import maint.entity.CalendarsEntity;
import maint.dto.operation.calendars.CalendarInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 運行カレンダーMAPPER
 *
 * @author g.ko
 */

@Mapper
public interface CalendarsMapper {

  /**
   * 運行カレンダー数取得
   *
   * @param dto 運行カレンダー検索DTO
   * @return 運行カレンダー数
   */
  Long countCalendarsBySearch(SearchCalendarsDto dto);

  /**
   * 運行カレンダー一覧取得
   *
   * @param dto 運行カレンダー検索DTO
   * @return 運行カレンダー一覧
   */
  List<CalendarDto> getCalendarsBySearch(SearchCalendarsDto dto);

  /**
   * 運行カレンダー取得
   *
   * @param busStopId PK
   * @return 運行カレンダーDTO
   */
  CalendarDto getCalendarByPk(@Param("busStopId") Long busStopId);

  /**
   * IDから運行カレンダー情報を取得
   *
   * @return 運行カレンダー情報リスト
   */
  List<CalendarInfoDto> getCalendarsById(@Param("calendarId") Long calendarId,
      @Param("businessCorpId") Long businessCorpId);

  /**
   * 有効な運行カレンダー数を取得
   *
   * @param businessCorpId 事業者ID
   * @param calendarId カレンダーID
   * @return 件数
   */
  int countCalendarActive(@Param("businessCorpId") String businessCorpId,
      @Param("calendarId") String calendarId);

  /**
   * 登録
   *
   * @param calendarsEntity 運行カレンダーEntity
   * @return 登録数
   */
  int registerCalendar(CalendarsEntity calendarsEntity);

  /**
   * 変更
   *
   * @param calendarsEntity 運行カレンダーEntity
   * @return 変更数
   */
  int updateCalendar(CalendarsEntity calendarsEntity);

}
