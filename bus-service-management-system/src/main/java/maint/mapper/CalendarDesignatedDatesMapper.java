package maint.mapper;

import maint.dto.operation.calendars.CalendarDesignatedDateDto;
import maint.entity.CalendarDesignatedDatesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 運行カレンダー指定日設定MAPPER
 *
 * @author g.ko
 */

@Mapper
public interface CalendarDesignatedDatesMapper {

  /**
   * 運行カレンダーIDから運行指定日取得
   *
   * @param calendarId 運行カレンダーID
   * @param businessCorpId 事業者ID
   * @return 運行指定日リスト
   */
  List<String> getFutureDesignatedDatesByCalendarId(@Param("calendarId") Long calendarId,
      @Param("businessCorpId") Long businessCorpId);

  /**
   * 運行カレンダー指定日取得
   *
   * @param calendarId 運行カレンダーID
   * @return 運行カレンダー指定日DTOリスト
   */
  List<CalendarDesignatedDateDto> getDesignatedDateByCalendarId(@Param("calendarId") Long calendarId);

  /**
   * 指定されたGTFSレポートフォームから、運行指定日を取得します。
   *
   * @param calendarId 運行カレンダーID
   * @return 運行指定日リスト（形式：yyyyMMdd）
   */
  List<String> getCalendarDesignatedDatesList(@Param("calendarId") Integer calendarId);

  /**
   * 登録
   *
   * @param calendarDesignatedDatesEntity 運行カレンダー指定日設定Entity
   * @return 登録数
   */
  int registerCalendarDesignatedDate(CalendarDesignatedDatesEntity calendarDesignatedDatesEntity);

  /**
   * 削除
   *
   * @param calendarDesignatedDatesEntity 運行カレンダー指定日設定Entity
   * @return 削除数
   */
  int deleteDesignatedDate(CalendarDesignatedDatesEntity calendarDesignatedDatesEntity);

}
