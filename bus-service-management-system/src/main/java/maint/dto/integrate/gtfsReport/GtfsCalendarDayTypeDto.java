// Java
package maint.dto.integrate.gtfsReport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.enums.FlagValueEnums;
import maint.enums.WeekDesignationType;

import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
/**
 * カレンダー情報出力DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GtfsCalendarDayTypeDto extends GtfsBaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = 7239815450439645982L;

  /** 運行日ID */
  private String serviceId;

  /** 運行カレンダーID */
  private Integer calendarId;

  /** カレンダー種別 */
  private String calendarType;

  /** 曜日指定可否区分（0:不可,1:可） */
  private Integer weekDesignatableFlag;

  /** 曜日指定区分 */
  private String weekDesignationType;

  /** 曜日区分 */
  private String dayWeekType;

  /** 日種別0フラグ */
  private Integer dayType0Flag;

  /** 日種別1フラグ */
  private Integer dayType1Flag;

  /** 日種別2フラグ */
  private Integer dayType2Flag;

  /** 日種別3フラグ */
  private Integer dayType3Flag;

  /** 日種別4フラグ */
  private Integer dayType4Flag;

  /** 日種別5フラグ */
  private Integer dayType5Flag;

  /** 日種別6フラグ */
  private Integer dayType6Flag;

  /** 日種別7フラグ */
  private Integer dayType7Flag;

  /** 祝日フラグ */
  private Integer holidaysFlag;

  /** 便ダイヤ改正開始日 */
  private String serviceStartDate;

  /** 便ダイヤ改正終了日 */
  private String serviceEndDate;

  public record CalendarDateRow(
      String serviceId,      // 運行日ID
      String date,           // YYYYMMDD
      String exceptionType   //
  ) {}

  public static class CalendarRow {
    String serviceId;   // 運行日ID
    String monday;      // 月曜日
    String tuesday;     // 火曜日
    String wednesday;   // 水曜日
    String thursday;    // 木曜日
    String friday;      // 金曜日
    String saturday;    // 土曜日
    String sunday;      // 日曜日
    String holidays;    // 祝日
    String startDate;   // サービス開始日
    String endDate;     // サービス終了日

    /**
     * CalendarDayTypeDtoの情報からGTFSのcalendar.txtに対応するCalendarRowレコードを構築します。
     *
     * @param m 曜日別の運行情報を保持するCalendarDayTypeDtoオブジェクト
     * @return CalendarRowレコード
     */
    public static CalendarRow fromSqlMap(GtfsCalendarDayTypeDto m) {
      if (m == null) return null;
      CalendarRow r = new CalendarRow();

      r.serviceId = nz(m.getCalendarType());
      if (isEmpty(r.serviceId))
        return null;

      r.startDate = toYyyyMMdd(m.getServiceStartDate());
      r.endDate = toYyyyMMdd(m.getServiceEndDate());

      String mon = String.valueOf(m.getDayType2Flag()); // 月
      String tue = String.valueOf(m.getDayType3Flag()); // 火
      String wed = String.valueOf(m.getDayType4Flag()); // 水
      String thu = String.valueOf(m.getDayType5Flag()); // 木
      String fri = String.valueOf(m.getDayType6Flag()); // 金
      String sat = String.valueOf(m.getDayType7Flag()); // 土
      String sun = String.valueOf(m.getDayType1Flag()); // 日(0)
      String holiday = String.valueOf(m.getHolidaysFlag()); // 祝日

      Set<Integer> codeSet = parseWeekdayCodeSet(m);
      if (isEmpty(mon)) mon = codeSet.contains(2) ? "1" : "0";
      if (isEmpty(tue)) tue = codeSet.contains(3) ? "1" : "0";
      if (isEmpty(wed)) wed = codeSet.contains(4) ? "1" : "0";
      if (isEmpty(thu)) thu = codeSet.contains(5) ? "1" : "0";
      if (isEmpty(fri)) fri = codeSet.contains(6) ? "1" : "0";
      if (isEmpty(sat)) sat = codeSet.contains(7) ? "1" : "0";
      if (isEmpty(sun)) sun = codeSet.contains(0) ? "1" : "0";
      if (isEmpty(holiday)) holiday = codeSet.contains(0) ? "1" : "0";

      r.monday    = to01(mon);
      r.tuesday   = to01(tue);
      r.wednesday = to01(wed);
      r.thursday  = to01(thu);
      r.friday    = to01(fri);
      r.saturday  = to01(sat);
      r.sunday    = to01(sun);
      r.holidays    = to01(holiday);

      return r;
    }
  }

  /**
   * CalendarRowのリストからGTFSのcalendar.txt形式のキー・バリュー情報を構築し、
   * 指定されたパスに書き込みます。
   *
   * @param rows CalendarRowレコードのリスト
   * @param output 出力先のファイルパス
   * @throws Exception 書き込み処理中に発生した例外
   */
  public static void writeCalendarTxtKeyValue(List<CalendarRow> rows, Path output) throws Exception {
    List<String> lines = new ArrayList<>();
    if (rows != null) {

      // ヘッダー
      lines.add(String.join(",",
          "service_id",
          "monday",
          "tuesday",
          "wednesday",
          "thursday",
          "friday",
          "saturday",
          "sunday",
          "start_date",
          "end_date"
      ));

      for (CalendarRow r : rows) {
        String line = String.join(",",
            output_format("service_id", r.serviceId),
            output_format("monday", nz(r.monday)),
            output_format("tuesday", nz(r.tuesday)),
            output_format("wednesday", nz(r.wednesday)),
            output_format("thursday", nz(r.thursday)),
            output_format("friday", nz(r.friday)),
            output_format("saturday", nz(r.saturday)),
            output_format("sunday", nz(r.sunday)),
            output_format("start_date", nz(r.startDate)),
            output_format("end_date", nz(r.endDate))
        );
        lines.add(line);
      }
    }
    writeAll(output, lines,true);
  }

  /**
   * CalendarDayTypeDtoのリストからGTFSのcalendar.txtに対応するCalendarRowレコードを構築します。
   *
   * @param maps 曜日別の運行情報を保持するCalendarDayTypeDtoのリスト
   * @return CalendarRowレコードのリスト
   */
  public static List<CalendarRow> buildCalendarRowsFromMaps(List<GtfsCalendarDayTypeDto> maps) {
    List<CalendarRow> rows = new ArrayList<>();
    if (maps == null) return rows;

    for (GtfsCalendarDayTypeDto m : maps) {
      CalendarRow r = CalendarRow.fromSqlMap(m);

      if (r == null) continue;

      rows.add(r);
    }

    return rows;
  }

  /**
   * CalendarDateRowのリストからGTFSのcalendar_dates.txt形式のキー・バリュー情報を構築し、
   * 指定されたパスに書き込みます。
   *
   * @param rows CalendarDateRowレコードのリスト
   * @param output 出力先のファイルパス
   * @throws Exception 書き込み処理中に発生した例外
   */
  public static void writeCalendarDatesTxtKeyValue(List<GtfsCalendarDayTypeDto.CalendarDateRow> rows, Path output) throws Exception {
    List<String> lines = new ArrayList<>();
    if (rows != null) {

      // ヘッダー
      lines.add(String.join(",",
          "service_id",
          "date",
          "exception_type"
      ));

      for (GtfsCalendarDayTypeDto.CalendarDateRow r : rows) {
        String line = String.join(",",
            output_format("service_id", r.serviceId),
            output_format("date", r.date),
            output_format("exception_type", r.exceptionType));
        lines.add(line);
      }
    }
    writeAll(output, lines,true);
  }

  /**
   * 祝日運行と平日運行のサービスIDを切り替えるためのCalendarDateRowレコードを構築
   *
   * @param cal          カレンダー情報DTO
   * @param holidayDates 祝日を表す日付のコレクション
   * @param calendarRows 登録済みリスト
   * @return CalendarDateRowレコードのリスト
   */
  public static List<CalendarDateRow> generateHolidaySwitchRows(GtfsCalendarDayTypeDto cal,
      Collection<?> holidayDates, List<CalendarDateRow> calendarRows) {
    List<CalendarDateRow> out = new ArrayList<>();
    if (holidayDates == null) return out;

    String serviceId = cal.getCalendarType();
    for (Object d : holidayDates) {
      String ymd = toYyyyMMdd(d);

      // 書込み済みの日（運行指定日、運行除外日）はスキップ
      if (calendarRows.stream().anyMatch(r -> r.serviceId.equals(serviceId) && r.date.equals(ymd)))
        continue;

      boolean isHoliday = FlagValueEnums.TRUE.getValue().equals(cal.getHolidaysFlag());
      boolean isHolidayPriority =
          WeekDesignationType.HOLIDAY_PRIORITY.getValue().equals(cal.getWeekDesignationType());
      boolean isOperating = isHolidayPriority ? isHoliday : isAvailableDayOfWeek(cal, ymd);

      // 運行・非運行の判定
      out.add(new CalendarDateRow(cal.getCalendarType(), ymd, isOperating ? "1" : "2"));
    }

    return dedup(out);
  }

  /**
   * 指定されたサービスIDと指定日情報から、GTFSのcalendar_dates.txtに対応する
   * サービス指定日のCalendarDateRowレコードを構築します。
   *
   * @param serviceId サービスID
   * @param designatedDates 運行指定日を表す日付のコレクション
   * @return サービス運休を表すCalendarDateRowレコードのリスト
   */
  public static List<CalendarDateRow> generateDesignatedDateRows(String serviceId, Collection<?> designatedDates) {
    List<CalendarDateRow> out = new ArrayList<>();
    if (designatedDates == null || isEmpty(serviceId)) return out;

    for (Object d : designatedDates) {
      String ymd = toYyyyMMdd(d);
      out.add(new CalendarDateRow(serviceId, ymd, "1")); // 運行区分適用
    }

    return dedup(out);
  }

  /**
   * 指定されたサービスIDと運休日情報から、GTFSのcalendar_dates.txtに対応する
   * サービス運休用のCalendarDateRowレコードを構築します。
   *
   * @param serviceId サービスID
   * @param suspendDates 運休日を表す日付のコレクション
   * @return サービス運休を表すCalendarDateRowレコードのリスト
   */
  public static List<CalendarDateRow> generateSuspensionRows(String serviceId, Collection<?> suspendDates) {
    List<CalendarDateRow> out = new ArrayList<>();
    if (suspendDates == null || isEmpty(serviceId)) return out;

    for (Object d : suspendDates) {
      String ymd = toYyyyMMdd(d);
      out.add(new CalendarDateRow(serviceId, ymd, "2")); // 運休（exceptionType = "2"）
    }

    return dedup(out);
  }

  /**
   * 指定日の曜日区分をチェックして運行対象日かどうか判定する
   * @param cal カレンダー情報DTO
   * @param ymd YYYYMMDD形式の日付文字列
   * @return True:運行日、False：非運行日
   */
  private static boolean isAvailableDayOfWeek(GtfsCalendarDayTypeDto cal, String ymd) {
    DayOfWeek dow = LocalDate.parse(ymd, DateTimeFormatter.BASIC_ISO_DATE).getDayOfWeek();
    return switch (dow) {
      case MONDAY -> cal.getDayType2Flag() == 1;
      case TUESDAY -> cal.getDayType3Flag() == 1;
      case WEDNESDAY -> cal.getDayType4Flag() == 1;
      case THURSDAY -> cal.getDayType5Flag() == 1;
      case FRIDAY -> cal.getDayType6Flag() == 1;
      case SATURDAY -> cal.getDayType7Flag() == 1;
      case SUNDAY -> cal.getDayType1Flag() == 1;
      default -> false;
    };
  }

  /**
   * CalendarDateRowのリストから重複する日付情報を除去し、ユニークなレコードのみを返します。
   *
   * @param rows CalendarDateRowのリスト
   * @return 重複除去済みのCalendarDateRowリスト
   */
  private static List<GtfsCalendarDayTypeDto.CalendarDateRow> dedup(List<GtfsCalendarDayTypeDto.CalendarDateRow> rows) {
    List<GtfsCalendarDayTypeDto.CalendarDateRow> out = new ArrayList<>();
    Set<String> seen = new HashSet<>();
    for (GtfsCalendarDayTypeDto.CalendarDateRow r : rows) {
      if (r == null)
        continue;
      if (isEmpty(r.serviceId) || isEmpty(r.date) || isEmpty(r.exceptionType))
        continue;
      String k = (r.serviceId + "|" + r.date + "|" + r.exceptionType).toLowerCase();
      if (seen.add(k))
        out.add(r);
    }
    return out;
  }
}
