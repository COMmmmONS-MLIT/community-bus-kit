// Java
package maint.dto.integrate.gtfsReport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 停留所情報出力DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GtfsStopsInfoDto extends GtfsBaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -4852944324252772385L;

  /** 停留所コード */
  private String busStopCode;

  /** 停留所名称 */
  private String busStopName;

  /** 停留所名称 かな */
  private String busStopKanaName;

  /** 停留所名称 英語 */
  private String busStopEnName;

  /** 標柱名称 かな */
  private String signpostKanaName;

  /** 標柱名称 英語 */
  private String signpostEnName;

  /** 標柱枝番*/
  private String signpostBranchNumber;

  /** 緯度、経度 */
  private String latitudeLongitude;

  /** 停留所URL */
  private String busStopUrl;

  /** 停留所・標柱区分 */
  private String busStopType;

  /**
   * 緯度を取得する
   * @return 緯度
   */
  public Double getLatitude() {
    return Double.valueOf(latitudeLongitude.split(",")[0]);
  }

  /**
   * 経度を取得する
   * @return 経度
   */
  public Double getLongitude() {
    return Double.valueOf(latitudeLongitude.split(",")[1]);
  }

  public record StopRow(
      String stopId,              // 停留所・標柱ID
      String stopCode,            // 停留所・標柱番号
      String stopName,            // 停留所・標柱名称
      String stopDesc,            // 停留所・標柱付加情報
      String stopLat,             // 緯度
      String stopLon,             // 経度
      String zoneId,              // 運賃エリアID
      String stopUrl,             // 停留所・標柱URL
      String locationType,        // 停留所・標柱区分
      String parentStation,       // 親停留所情報
      String stopTimezone,        // タイムゾーン
      String wheelchairBoarding,  // 車椅子情報
      String platformCode         // のりば情報
  ) {}

  /**
   * StopRowのリストからGTFSのstops.txt形式のキー・バリュー情報を構築し、
   * 指定されたパスに書き込みます。
   *
   * @param rows StopRowレコードのリスト
   * @param output 出力先のファイルパス
   * @throws Exception 書き込み処理中に発生した例外
   */
  public static void writeStopsTxtKeyValue(List<StopRow> rows, Path output) throws Exception {
    List<String> lines = new ArrayList<>();
    if (rows != null) {

      // ヘッダー
      lines.add(String.join(",",
          "stop_id",
          "stop_code",
          "stop_name",
          "stop_desc",
          "stop_lat",
          "stop_lon",
          "zone_id",
          "stop_url",
          "location_type",
          "parent_station",
          "stop_timezone",
          "wheelchair_boarding",
          "platform_code"
      ));

      for (StopRow r : rows) {
        String line = String.join(",",
            output_format("stop_id", r.stopId),
            output_format("stop_code", r.stopCode),
            output_format("stop_name", r.stopName),
            output_format("stop_desc", r.stopDesc),
            output_format("stop_lat", r.stopLat),
            output_format("stop_lon", r.stopLon),
            output_format("zone_id", r.zoneId),
            output_format("stop_url", r.stopUrl),
            output_format("location_type", r.locationType),
            output_format("parent_station", r.parentStation),
            output_format("stop_timezone", r.stopTimezone),
            output_format("wheelchair_boarding", r.wheelchairBoarding),
            output_format("platform_code", r.platformCode)
        );
        lines.add(line);
      }
    }
    writeAll(output, lines,true);
  }

  /**
   * StopsInfoDtoのリストからGTFSのstops.txtに対応するStopRowレコードを構築します。
   * 重複する停留所情報は除去されます。
   *
   * @param maps 停留所情報のリスト
   * @return StopRowレコードのリスト（重複除去済み）
   */
  public static List<StopRow> buildStopRowsFromMaps(List<GtfsStopsInfoDto> maps) {
    List<StopRow> rows = new ArrayList<>();
    if (maps == null) return rows;

    Set<String> seen = new HashSet<>();

    for (GtfsStopsInfoDto m : maps) {
      String stopId            = nz(m.getBusStopCode());
      String stopCode          = "";
      String stopName          = nz(m.getBusStopName());
      String stopDesc          = "";
      String stopLat           = numStr(m.getLatitude());
      String stopLon           = numStr(m.getLongitude());
      String zoneId            = "";
      String stopUrl           = nz(m.getBusStopUrl());
      String locationType      = nz(m.getBusStopType());
      String parentStation     = "";
      String stopTimezone      = "";
      String wheelchairBoarding= "";
      String platformCode      = "";

      if (isEmpty(stopId)) continue;
      if (seen.contains(stopId)) continue;
      seen.add(stopId);

      StopRow r = new StopRow(
          stopId,
          stopCode,
          stopName,
          stopDesc,
          stopLat,
          stopLon,
          zoneId,
          stopUrl,
          locationType,
          parentStation,
          stopTimezone,
          wheelchairBoarding,
          platformCode
      );

      rows.add(r);
    }
    return rows;
  }

  /**
   * 停留所情報のリストから、停留所名称（英語・よみがな）に関するTranslationRowセクションを構築
   *
   * @param stopMaps 停留所情報のリスト
   * @param output 出力先のパス
   * @throws Exception 書き込み処理中に発生した例外
   */
  public static void writeStopNameSectionsFromMaps(List<GtfsStopsInfoDto> stopMaps, Path output) throws Exception {
    List<String> lines = new ArrayList<>();

    // ■停留所名称（よみがな）
    if (stopMaps != null) {
      for (GtfsStopsInfoDto m : stopMaps) {
        String stopId   = nz(m.getBusStopCode());
        String kanaName = nz(m.getBusStopKanaName());
        lines.add(toLine(stopNameKana(kanaName, stopId)));
      }
    }

    // ■停留所名称（英語）
    if (stopMaps != null) {
      for (GtfsStopsInfoDto m : stopMaps) {
        String stopId = nz(m.getBusStopCode());
        String enName = nz(m.getBusStopEnName());
        lines.add(toLine(stopNameEn(enName, stopId)));
      }
    }
    writeAllForAppend(output, lines, TRANSLATION_HEADER,false);
  }

  /**
   * 停留所名称（よみがな）に対応するTranslationRowレコードを構築します。
   *
   * @param kanaName 停留所名称（よみがな）
   * @param stopId 停留所ID
   * @return TranslationRowレコード
   */
  public static TranslationRow stopNameKana(String kanaName, String stopId) {
    return new TranslationRow(
        "stops",           // テーブル名
        "stop_name",       // フィールド名
        "ja-Hrkt",         // 言語
        kanaName,          // 翻訳先言語
        stopId,            // レコードID
        "",                // レコードサブID（指定なし）
        ""                 // キーワード（recordId があれば不要）
    );
  }

  /**
   * 停留所名称（英語）に対応するTranslationRowレコードを構築します。
   *
   * @param enName 停留所名称（英語）
   * @param stopId 停留所ID
   * @return TranslationRowレコード
   */
  public static TranslationRow stopNameEn(String enName, String stopId) {
    return new TranslationRow(
        "stops",           // テーブル名
        "stop_name",       // フィールド名
        "en",              // 言語
        enName,            // 翻訳先言語
        stopId,            // レコードID
        "",                // レコードサブID（固定）
        ""                 // キーワード（recordId があれば不要）
    );
  }
}
