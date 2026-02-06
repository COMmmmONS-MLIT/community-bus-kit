package maint.mapper;

import java.util.List;
import maint.dto.operation.calendars.CalendarCancelDateDto;
import maint.entity.CalendarCancelDatesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 運行カレンダー除外日設定MAPPER
 *
 * @author g.ko
 */

@Mapper
public interface CalendarCancelDatesMapper {

  /**
   * 運行カレンダーIDから運行除外日取得
   *
   * @param calendarId 運行カレンダーID
   * @param businessCorpId 事業者ID
   * @return 運行除外日リスト
   */
  List<String> getFutureCancelDatesByCalendarId(@Param("calendarId") Long calendarId,
      @Param("businessCorpId") Long businessCorpId);

  /**
   * 運行カレンダー除外日取得
   *
   * @param calendarId 運行カレンダーID
   * @return 運行カレンダー除外日DTOリスト
   */
  List<CalendarCancelDateDto> getCancelDateByCalendarId(@Param("calendarId") Long calendarId);

  /**
   * 指定されたGTFSレポートフォームから、祝日・運休日などの特別運行日情報を取得します。
   * この情報は、calendar_dates.txtの生成に使用されます。
   *
   * @param calendarId 運行カレンダーID
   * @return  除外日リスト（例：yyyyMMdd形式）
   */
  List<String> getCalendarCancelDatesList(@Param("calendarId") Integer calendarId);

  /**
   * 登録
   *
   * @param calendarCancelDatesEntity 運行カレンダー除外日設定Entity
   * @return 登録数
   */
  int registerCalendarCancelDate(CalendarCancelDatesEntity calendarCancelDatesEntity);

  /**
   * 削除
   *
   * @param calendarCancelDatesEntity 運行カレンダー除外日設定Entity
   * @return 削除数
   */
  int deleteCancelDate(CalendarCancelDatesEntity calendarCancelDatesEntity);

}
