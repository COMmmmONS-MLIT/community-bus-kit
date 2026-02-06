package maint.mapper;

import java.util.List;
import maint.dto.operation.calendars.CalendarDetailDto;
import maint.entity.CalendarDetailsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 運行カレンダー詳細MAPPER
 *
 * @author g.ko
 */

@Mapper
public interface CalendarDetailsMapper {

  /**
   * 運行カレンダー詳細に指定した曜日区分のデータが存在するかチェック
   *
   * @param calendarId 運行カレンダーID
   * @param dayWeekType 曜日区分
   * @return true : あり、false : なし
   */
  boolean isDayWeekTypePresent(@Param("calendarId") Long calendarId,
      @Param("dayWeekType") String dayWeekType);

  /**
   * 運行カレンダー詳細取得
   *
   * @param calendarId 運行カレンダーID
   * @return 停留所一覧
   */
  List<CalendarDetailDto> getCalendarDetailsByCalendarId(@Param("calendarId") Long calendarId);

  /**
   * 登録
   *
   * @param calendarDetailsEntity 運行カレンダー明細Entity
   * @return 登録数
   */
  int registerCalendarDetail(CalendarDetailsEntity calendarDetailsEntity);

  /**
   * 削除
   *
   * @param calendarDetailsEntity 運行カレンダー明細Entity
   * @return 削除数
   */
  int deleteCalendarDetail(CalendarDetailsEntity calendarDetailsEntity);
}

