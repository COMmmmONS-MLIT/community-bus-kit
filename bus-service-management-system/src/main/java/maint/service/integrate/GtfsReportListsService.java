package maint.service.integrate;

import maint.dto.integrate.gtfsReport.GtfsAgencyInfoDto;
import maint.dto.integrate.gtfsReport.GtfsBusServiceTripDto;
import maint.dto.integrate.gtfsReport.GtfsCalendarDayTypeDto;
import maint.dto.integrate.gtfsReport.GtfsDaiyaRevisionDto;
import maint.dto.integrate.gtfsReport.GtfsFareInfoDto;
import maint.dto.integrate.gtfsReport.GtfsLineRouteDto;
import maint.dto.integrate.gtfsReport.GtfsStopsInfoDto;
import maint.dto.integrate.gtfsReport.GtfsStopsOtherInfoDto;
import maint.entity.AgenciesEntity;
import maint.enums.WeekDesignatableFlag;
import maint.form.gtfsReport.GtfsReportListsExcelForm;
import maint.mapper.*;
import maint.service.BaseService;
import maint.utils.ExObjectUtils;
import maint.utils.date.ExDateFormat;
import maint.utils.date.ExDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static maint.util.date.ExDateUtils.parseLocalDate;

/**
 * GTFS-ScheduleSERVICE
 *
 * @author JEON
 */

@Service
public class GtfsReportListsService extends BaseService {

  /** 事業者MAPPER */
  @Autowired
  AgenciesMapper agenciesMapper;

  /** 系統Mapper */
  @Autowired
  LineRoutesMapper lineRoutesMapper;

  /** 系統停留所Mapper */
  @Autowired
  LineRouteBusStopsMapper lineRouteBusStopsMapper;

  /** 路線Mapper */
  @Autowired
  LinesMapper linesMapper;

  /** 便MAPPER */
  @Autowired
  BusServicesMapper busServicesMapper;

  /** 祝日カレンダーMAPPER */
  @Autowired
  HolidayCalendarMapper holidayCalendarMapper;

  /** 運行カレンダー除外日設定MAPPER */
  @Autowired
  CalendarCancelDatesMapper calendarCancelDatesMapper;

  /** 運行カレンダー指定日設定MAPPER */
  @Autowired
  CalendarDesignatedDatesMapper calendarDesignatedDatesMapper;

  /** 運賃改正Mapper */
  @Autowired
  ChargeRevisionsMapper chargeRevisionsMapper;

  /**
   * 出力ファイル名の生成
   * 事業者名(自治体表示名) + 処理対象日(YYYYMMDD) + "改正" + 作成日(MMDD) + "作"
   * @param targetDate 処理対象日
   * @return 出力ファイル名
   */
  public String getDownloadFileName(String targetDate) {
    // 出力ファイル名の生成
    AgenciesEntity agency = agenciesMapper.getAgencyByPkId(getUserSession().getBusinessCorpId());
    String currentDate =
        ExDateUtils.convertDateTimeFormat(ExDateUtils.getCurrentDateTime(), ExDateFormat.MMdd);
    return String.format("%s%s改正%s作.zip", agency.getDispName(),
        parseLocalDate(targetDate).format(ExDateFormat.uuuuMMdd), currentDate);
  }

  /**
   * GTFSテキストファイルZIPを生成
   *
   * @param form GTFS-ScheduleFORM
   * @param gtfsFiles 生成ファイル名リスト
   * @return 生成されたZIPファイルのPath
   */
  public Path exportGtfsFeedZip(GtfsReportListsExcelForm form, List<String> gtfsFiles)
      throws Exception {
    // 共通・事業者情報
    List<GtfsDaiyaRevisionDto> daiyaRevisionList =
        linesMapper.getDaiyaRevisionList(getUserSession().getBusinessCorpId(),
            form.getTargetDate());
    List<GtfsAgencyInfoDto> agencyInfoList =
        agenciesMapper.getAgencyInfoList(getUserSession().getBusinessCorpId());

    // 共通・事業者情報をGTFS-Schedule形式にする
    GtfsAgencyInfoDto.writeAgencyTxtKeyValue(
        GtfsAgencyInfoDto.buildStopRowsFromMaps(agencyInfoList),
        Paths.get("output/gtfs/agency.txt"));
    GtfsDaiyaRevisionDto.writeFeedInfoTxtKeyValue(
        GtfsDaiyaRevisionDto.buildStopRowsFromMaps(daiyaRevisionList),
        Paths.get("output/gtfs/feed_info.txt"));
    GtfsAgencyInfoDto.writeAgencyNameSectionsFromMaps(agencyInfoList,
        Paths.get("output/gtfs/translations.txt"));

    // 共通から「路線ID」、「便ダイヤ改正ID」を取得
    List<Long> rosenIdList = null;
    List<Long> daiyaRevisionIdList = null;
    if (ExObjectUtils.isNotBlank(daiyaRevisionList)) {
      rosenIdList =
          daiyaRevisionList.stream().filter(Objects::nonNull).map(GtfsDaiyaRevisionDto::getRosenId)
              .filter(Objects::nonNull).distinct().toList();

      daiyaRevisionIdList = daiyaRevisionList.stream().filter(Objects::nonNull)
          .map(GtfsDaiyaRevisionDto::getDaiyaRevisionId).filter(Objects::nonNull).distinct()
          .toList();
    }

    // 停留所情報
    List<GtfsStopsInfoDto> stopsInfoList = lineRouteBusStopsMapper.getStopsInfoList(rosenIdList);

    // 停留所情報をGTFS-Schedule形式にする
    GtfsStopsInfoDto.writeStopsTxtKeyValue(GtfsStopsInfoDto.buildStopRowsFromMaps(stopsInfoList),
        Paths.get("output/gtfs/stops.txt"));
    GtfsStopsInfoDto.writeStopNameSectionsFromMaps(stopsInfoList,
        Paths.get("output/gtfs/translations.txt"));

    // 標柱情報
    List<GtfsStopsOtherInfoDto> stopsOtherInfoList =
        lineRouteBusStopsMapper.getStopsOtherInfoList(rosenIdList);

    // 標柱情報をGTFS-Schedule形式にする
    GtfsStopsOtherInfoDto.writeStopsTxtKeyValue(
        GtfsStopsOtherInfoDto.buildStopRowsFromMaps(stopsOtherInfoList),
        Paths.get("output/gtfs/stops.txt"));
    GtfsStopsOtherInfoDto.writeSignpostNameSectionsFromMaps(stopsOtherInfoList,
        Paths.get("output/gtfs/translations.txt"));

    // 経路情報
    List<GtfsLineRouteDto> lineRouteList = lineRoutesMapper.getLineRouteList(rosenIdList);

    // 経路情報をGTFS-Schedule形式にする
    GtfsLineRouteDto.writeRoutesTxtKeyValue(GtfsLineRouteDto.buildRouteRowsFromMaps(lineRouteList),
        Paths.get("output/gtfs/routes.txt"));
    GtfsLineRouteDto.writeRouteLongNameEnFromMaps(lineRouteList,
        Paths.get("output/gtfs/translations.txt"));

    // 便・通過時刻・帰属情報
    List<GtfsBusServiceTripDto> busServiceTripList =
        busServicesMapper.getBusServiceTripList(daiyaRevisionIdList);

    // 便・通過時刻・帰属情報をGTFS-Schedule形式にする
    GtfsBusServiceTripDto.writeTripsTxtKeyValue(
        GtfsBusServiceTripDto.buildTripRowsFromMaps(busServiceTripList),
        Paths.get("output/gtfs/trips.txt"));
    GtfsBusServiceTripDto.writeStopTimesTxtKeyValueFromMaps(busServiceTripList,
        Paths.get("output/gtfs/stop_times.txt"));
    GtfsBusServiceTripDto.writeAttributionsTxtKeyValueFromMaps(busServiceTripList,
        Paths.get("output/gtfs/attributions.txt"));
    GtfsBusServiceTripDto.writeStopTimeNameSectionsFromMaps(busServiceTripList,
        Paths.get("output/gtfs/translations.txt"));

    // カレンダー情報
    List<GtfsCalendarDayTypeDto> calendarDayTypeList =
        busServicesMapper.getCalendarDayTypeList(daiyaRevisionIdList);

    // カレンダー情報をGTFS-Schedule形式にする
    GtfsCalendarDayTypeDto.writeCalendarTxtKeyValue(
        GtfsCalendarDayTypeDto.buildCalendarRowsFromMaps(calendarDayTypeList),
        Paths.get("output/gtfs/calendar.txt"));

    // 祝日情報
    List<String> holidays =
        holidayCalendarMapper.getCalendarDatesList(getUserSession().getBusinessCorpId());
    List<GtfsCalendarDayTypeDto.CalendarDateRow> calDateRows = new ArrayList<>();

    for (GtfsCalendarDayTypeDto cal : calendarDayTypeList) {
      // 指定日情報の書き出し
      calDateRows.addAll(GtfsCalendarDayTypeDto.generateDesignatedDateRows(cal.getCalendarType(),
          calendarDesignatedDatesMapper.getCalendarDesignatedDatesList(cal.getCalendarId())));
      // 曜日指定可否区分が「指定可」の場合、除外日と祝日を書き出す
      if (cal.getWeekDesignatableFlag().equals(WeekDesignatableFlag.DESIGNATION_ENABLED.getValue())) {
        // 除外日情報の書き出し
        calDateRows.addAll(GtfsCalendarDayTypeDto.generateSuspensionRows(cal.getCalendarType(),
            calendarCancelDatesMapper.getCalendarCancelDatesList(cal.getCalendarId())));
        // 祝日情報の書き出し
        calDateRows.addAll(
            GtfsCalendarDayTypeDto.generateHolidaySwitchRows(cal, holidays, calDateRows));
      }
    }

    // 指定日・除外日・祝日情報をGTFS-Schedule形式にする
    GtfsCalendarDayTypeDto.writeCalendarDatesTxtKeyValue(calDateRows,
        Paths.get("output/gtfs/calendar_dates.txt"));

    // 運賃情報
    List<GtfsFareInfoDto> fareInfoList =
        chargeRevisionsMapper.getFareInfoList(rosenIdList, form.getTargetDate());

    // 運賃情報をGTFS-Schedule形式にする
    GtfsFareInfoDto.writeFareAttributesTxtKeyValue(
        GtfsFareInfoDto.buildFareAttributesRowsFromMaps(fareInfoList),
        Paths.get("output/gtfs/fare_attributes.txt"));
    GtfsFareInfoDto.writeFareRulesTxtKeyValue(
        GtfsFareInfoDto.buildFareRulesRowsFromMaps(fareInfoList),
        Paths.get("output/gtfs/fare_rules.txt"));

    Path zipPath = Files.createTempFile("reports_", ".zip");

    try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
      for (String fileName : gtfsFiles) {
        Path filePath = Paths.get("output/gtfs", fileName);
        zos.putNextEntry(new ZipEntry(fileName));
        Files.copy(filePath, zos);
        zos.closeEntry();
      }
    }

    return zipPath;
  }

}
