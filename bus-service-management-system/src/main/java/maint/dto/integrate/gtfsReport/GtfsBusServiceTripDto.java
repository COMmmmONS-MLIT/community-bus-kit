// Java
package maint.dto.integrate.gtfsReport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 便・通過時刻・帰属情報出力DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GtfsBusServiceTripDto extends GtfsBaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = 3821944048191172114L;

  /** 路線コード */
  private String rosenCode;

  /** 便ID */
  private Long busServiceId;

  /** 削除フラグ */
  private Integer delFlag;

  /** 便ダイヤ改正ID */
  private Long daiyaRevisionId;

  /** 便番号 */
  private String busServiceTripId;

  /** 便行先 */
  private String busServiceTripHeadsign;

  /** 便名称 */
  private String busServiceName;

  /** 運行カレンダーID */
  private Long calendarId;

  /** 運行カレンダー区分 */
  private String calendarType;

  /** 描画ID */
  private String shapeId;

  /** 車いす利用区分 */
  private String wheelchairAccessibleType;

  /** 自転車持ち込み区分 */
  private String bikesAllowedType;

  /** 便情報 */
  private String busServiceTripDesc;

  /** 便記号 */
  private String busServiceTripDescSymbol;

  /** 便結合区分 */
  private String blockId;

  /** 系統ID */
  private Long lineRouteId;

  /** 系統コード */
  private String lineRouteCode;

  /** 到着時刻 */
  private String busStopArrivalTime;

  /** 出発時刻 */
  private String busStopDepartureTime;

  /** 乗降区分 */
  private String rideAlightDivision;

  /** 下車区分 */
  private String dropOffDivision;

  /** 停留所行先 */
  private String stopHeadsign;

  /** 停留所行先（英語） */
  private String stopHeadsignEn;

  /** 通算距離 */
  private String shapeDistTraveled;

  /** 運行順序 */
  private Integer stopSequence;

  /** 往路、復路区分 */
  private String lineDetailServiceDivision;

  /** 標柱枝番 */
  private String signpostBranchNumber;

  /** 停留所コード */
  private String busStopCode;

  /** 運行会社ID */
  private Long operatingCompanyId;

  /** 運行会社名 */
  private String operatingName;

  /** 電話番号 */
  private String phoneNumber;

  /** 連絡先メール */
  private String mailAddress;

  /** URL */
  private String url;

  public record TripRow(
      String routeId,              // 経路ID
      String serviceId,            // 運行日ID
      String tripId,               // 便ID
      String tripHeadsign,         // 便行先
      String tripShortName,        // 便名称
      String directionId,          // 往復区分
      String blockId,              // 便結合区分
      String shapeId,              // 描画ID
      String wheelchairAccessible, // 車いす利用区分
      String bikesAllowed,         // 自転車持込区分
      String jpTripDesc,           // 便情報
      String jpTripDescSymbol,     // 便記号
      String jpOfficeId,           // 営業所ID
      String jpPatternId           // 停車パターンID
  ) {}

  /**
   * BusServiceTripDto のリストから GTFS の trips.txt に対応する TripRow オブジェクトを構築
   *
   * @param maps BusServiceTripDto のリスト
   * @return     有効な TripRow オブジェクトのリスト
   */
  public static List<TripRow> buildTripRowsFromMaps(List<GtfsBusServiceTripDto> maps) {
    List<TripRow> rows = new ArrayList<>();
    if (maps == null) return rows;

    Set<String> seenTripIds = new HashSet<>();
    for (GtfsBusServiceTripDto m : maps) {
      String routeId   = numStr(m.getRosenCode() + "_" + m.getLineRouteCode());
      String serviceId = nz(m.getCalendarType());

      if (isEmpty(routeId) || isEmpty(serviceId)) continue;

      String tripId = numStr(m.getRosenCode() + "_" + m.getBusServiceTripId());
      if (isEmpty(tripId)) {
        tripId = generateTripId(routeId, serviceId);
      }
      if (isEmpty(tripId)) continue;
      if (!seenTripIds.add(tripId)) continue;

      TripRow r = new TripRow(
          routeId,
          serviceId,
          tripId,
          nz(m.getBusServiceTripHeadsign()),
          nz(m.getBusServiceName()),
          nz(m.getLineDetailServiceDivision()),
          nz(m.getBlockId()),
          "",
          nz(m.getWheelchairAccessibleType()),
          nz(m.getBikesAllowedType()),
          nz(m.getBusServiceTripDesc()),
          nz(m.getBusServiceTripDescSymbol()),
          "",
          numStr(m.getLineRouteId())
      );

      rows.add(r);
    }
    return rows;
  }

  /**
   * TripRow のリストを GTFS の trips.txt 形式に準じた key-value ペアとして整形
   *
   * @param rows   書き出す対象となる便情報のリスト
   * @param output 出力先のファイルパス
   * @throws Exception ファイル書き込み時にエラーが発生した場合
   */
  public static void writeTripsTxtKeyValue(List<TripRow> rows, Path output) throws Exception {
    List<String> lines = new ArrayList<>();
    if (rows != null) {

      // ヘッダー行を追加
      lines.add(String.join(",",
          "route_id",
          "service_id",
          "trip_id",
          "trip_headsign",
          "trip_short_name",
          "direction_id",
          "block_id",
          "shape_id",
          "wheelchair_accessible",
          "bikes_allowed",
          "jp_trip_desc",
          "jp_trip_desc_symbol",
          "jp_office_id",
          "jp_pattern_id"
      ));

      for (TripRow r : rows) {
        String line = String.join(",",
            output_format("route_id",               r.routeId),
            output_format("service_id",             r.serviceId),
            output_format("trip_id",                r.tripId),
            output_format("trip_headsign",          r.tripHeadsign),
            output_format("trip_short_name",        r.tripShortName),
            output_format("direction_id",           r.directionId),
            output_format("block_id",               r.blockId),
            output_format("shape_id",               r.shapeId),
            output_format("wheelchair_accessible",  r.wheelchairAccessible),
            output_format("bikes_allowed",          r.bikesAllowed),
            output_format("jp_trip_desc",           r.jpTripDesc),
            output_format("jp_trip_desc_symbol",    r.jpTripDescSymbol),
            output_format("jp_office_id",           r.jpOfficeId),
            output_format("jp_pattern_id",           r.jpPatternId)
        );
        lines.add(line);
      }
    }
    writeAll(output,lines,true);
  }

  /**
   * BusServiceTripDtoのリストからGTFSのstop_times.txt形式のキー・バリュー情報を構築し、
   * 指定されたパスに書き込みます。
   *
   * @param maps バス運行情報のリスト
   * @param output 出力先のファイルパス
   * @throws Exception 書き込み処理中に発生した例外
   */
  public static void writeStopTimesTxtKeyValueFromMaps(List<GtfsBusServiceTripDto> maps, Path output) throws Exception {
    List<String> lines = new ArrayList<>();
    if (maps != null) {

      // ヘッダー
      lines.add(String.join(",",
          "trip_id",
          "arrival_time",
          "departure_time",
          "stop_id",
          "stop_sequence",
          "stop_headsign",
          "pickup_type",
          "drop_off_type",
          "shape_dist_traveled",
          "timepoint"
      ));

      for (GtfsBusServiceTripDto m : maps) {
        String line = buildStopTimesLine(m);
        if (!line.isEmpty()) {
          lines.add(line);
        }
      }
    }
    writeAll(output,lines,true);
  }

  /**
   * BusServiceTripDtoの情報からGTFSのstop_times.txtに対応する1行の文字列を構築します。
   *
   * @param m バス運行情報を保持するBusServiceTripDtoオブジェクト
   * @return stop_times.txt形式の1行文字列
   */
  private static String buildStopTimesLine(GtfsBusServiceTripDto m) {
    String tripId = numStr(m.getRosenCode() + "_" + m.getBusServiceTripId());
    String stopId = composeStopId(m);
    String stopSequence = numStr(m.getStopSequence());

    if (isEmpty(tripId) || isEmpty(stopId))
      return "";

    String arrivalTime = nz(m.getBusStopArrivalTime());
    String departureTime = nz(m.getBusStopDepartureTime());
    String stopHeadsign = nz(m.getStopHeadsign());
    String pickupType = nz(m.getRideAlightDivision());
    String dropOffType = nz(m.getDropOffDivision());
    String shapeDistTraveled = "";

    String timepoint = "1";

    return String.join(",",
        output_format("trip_id", tripId),
        output_format("arrival_time", arrivalTime),
        output_format("departure_time", departureTime),
        output_format("stop_id", stopId),
        output_format("stop_sequence", stopSequence),
        output_format("stop_headsign", stopHeadsign),
        output_format("pickup_type", pickupType),
        output_format("drop_off_type", dropOffType),
        output_format("shape_dist_traveled", shapeDistTraveled),
        output_format("timepoint", timepoint)
    );
  }

  /**
   * BusServiceTripDtoの情報から一意な停留所IDを構築します。
   *
   * @param m バス運行情報を保持するBusServiceTripDtoオブジェクト
   * @return 停留所ID（文字列形式）
   */
  private static String composeStopId(GtfsBusServiceTripDto m) {
    String stopCode = nz(m.getBusStopCode());
    String branch   = nz(m.getSignpostBranchNumber());
    if (!isEmpty(stopCode)) {
      return isEmpty(branch) ? stopCode : (stopCode + "_" + branch);
    }
    return "";
  }

  /**
   * BusServiceTripDtoのリストからGTFSのattributions.txt形式のキー・バリュー情報を構築し、
   * 指定されたパスに書き込みます。
   *
   * @param maps バス運行情報のリスト
   * @param output 出力先のファイルパス
   * @throws Exception 書き込み処理中に発生した例外
   */
  public static void writeAttributionsTxtKeyValueFromMaps(List<GtfsBusServiceTripDto> maps, Path output) throws Exception {
    List<String> lines = new ArrayList<>();
    if (maps != null) {

      // ヘッダー
      lines.add(String.join(",",
          "attribution_id",
          "agency_id",
          "route_id",
          "trip_id",
          "organization_name",
          "is_producer",
          "is_operator",
          "is_authority",
          "attribution_url",
          "attribution_email",
          "attribution_phone"
      ));

      // 運行会社IDで重複削除
      List<GtfsBusServiceTripDto> unique = maps.stream().filter(Objects::nonNull).collect(
          Collectors.toMap(GtfsBusServiceTripDto::getOperatingCompanyId, Function.identity(),
              (a, b) -> a, LinkedHashMap::new)).values().stream().toList();

      for (GtfsBusServiceTripDto m : unique) {
        String line = buildAttributionsLine(m);
        if (!line.isEmpty()) {
          lines.add(line);
        }
      }
    }
    writeAll(output, lines, true);
  }

  /**
   * BusServiceTripDtoの情報からGTFSのattributions.txtに対応する1行の文字列を構築します。
   *
   * @param m バス運行情報を保持するBusServiceTripDtoオブジェクト
   * @return attributions.txt形式の1行文字列
   */
  private static String buildAttributionsLine(GtfsBusServiceTripDto m) {
    String attributionId     = numStr(m.getOperatingCompanyId());
    String agencyId          = "";
    String routeId           = "";
    String tripId            = numStr(m.getRosenCode() + "_" + m.getBusServiceTripId());
    String organizationName  = nz(m.getOperatingName());
    String attributionUrl    = nz(m.getUrl());
    String attributionEmail  = nz(m.getMailAddress());
    String attributionPhone  = nz(m.getPhoneNumber());

    if (isEmpty(organizationName)) return "";
    if (isEmpty(agencyId) && isEmpty(routeId) && isEmpty(tripId)) return "";

    String isProducer  = "0";
    String isOperator  = "1";
    String isAuthority = "0";

    return String.join(",",
        output_format("attribution_id",   attributionId),
        output_format("agency_id",        agencyId),
        output_format("route_id",         routeId),
        output_format("trip_id",          tripId),
        output_format("organization_name",organizationName),
        output_format("is_producer",      isProducer),
        output_format("is_operator",      isOperator),
        output_format("is_authority",     isAuthority),
        output_format("attribution_url",  attributionUrl),
        output_format("attribution_email",attributionEmail),
        output_format("attribution_phone",attributionPhone)
    );
  }

  /**
   * 通過時刻情報のリストから、停留所行先（英語）に関するTranslationRowセクションを構築
   *
   * @param stopTimeMaps 通過時刻情報のリスト
   * @param output 出力先のパス
   * @throws Exception 書き込み処理中に発生した例外
   */
  public static void writeStopTimeNameSectionsFromMaps(List<GtfsBusServiceTripDto> stopTimeMaps, Path output) throws Exception {
    List<String> lines = new ArrayList<>();

    // ■停留所行先（英語）
    if (stopTimeMaps != null) {
      for (GtfsBusServiceTripDto m : stopTimeMaps) {
        String stopHeadSign   = nz(m.getStopHeadsignEn());
        // 前行と同じ場合は出力しない
        if (!stopHeadSign.isEmpty() && lines.stream().noneMatch(l -> l.contains(stopHeadSign))) {
          lines.add(toLine(stopHeadSignEn(stopHeadSign, nz(m.getStopHeadsign()))));
        }
      }
    }
    writeAllForAppend(output, lines, TRANSLATION_HEADER,false);
  }

  /**
   * 停留所行先（英語）に対応するTranslationRowレコードを構築します。
   *
   * @param stopHeadsignEn 停留所行先（英語）
   * @param stopHeadsign   停留所行先
   * @return TranslationRowレコード
   */
  public static TranslationRow stopHeadSignEn(String stopHeadsignEn, String stopHeadsign) {
    return new TranslationRow(
        "stop_times", // テーブル名
        "stop_headsign",       // フィールド名
        "en",                  // 言語
        stopHeadsignEn,        // 翻訳先言語
        "",                    // レコードID
        "",                    // レコードサブID（指定なし）
        stopHeadsign           // キーワード（recordId があれば不要）
    );
  }
}
