// Java
package maint.dto.integrate.gtfsReport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.enums.IndoorDivision;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  標柱情報出力DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GtfsStopsOtherInfoDto extends GtfsBaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -86601449500378474L;

  /** 標柱ID */
  private Long lrsSignpostId;

  /** 標柱ID */
  private Long signpostId;

  /** 停留所ID */
  private Long busStopId;

  /** 標柱枝番 */
  private String signpostBranchNumber;

  /** 標柱番号 */
  private String signpostCode;

  /** 標柱名称 */
  private String signpostName;

  /** 標柱名称 かな */
  private String signpostKanaName;

  /** 標柱名称 英語 */
  private String signpostEnName;

  /** 標柱副名称 */
  private String signpostSubName;

  /** 標柱付加情報 */
  private String signpostDesc;

  /** 緯度、経度 */
  private String latitudeLongitude;

  /** 標柱URL */
  private String signpostUrl;

  /** 停留所・標柱区分 */
  private String busStopType;

  /** のりば情報 */
  private String platformCode;

  /** 車いす利用区分 */
  private String wheelchairAccessibleType;

  /** 停留所コード */
  private String busStopCode;

  /** 屋内バスターミナルフラグ */
  private Integer indoorBusTerminalFlag;

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

  /**
   * 親停留所情報を取得する
   * 屋内バスターミナルフラグが「1：屋内」の場合のみ停留所コードを返却する
   *
   * @return 親停留所情報（屋内でない場合は空文字）
   */
  public String getParentStation() {
    return IndoorDivision.INDOOR.getValue().equals(indoorBusTerminalFlag) ? busStopCode : "";
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
   * GTFSのstops.txt形式で停留所情報をkey=value形式でファイルに書き出します。
   *
   * @param rows   停留所情報のリスト
   * @param output 出力先ファイルパス
   * @throws Exception ファイル操作中の例外
   */
  public static void writeStopsTxtKeyValue(List<StopRow> rows, Path output) throws Exception {
    List<String> lines = new ArrayList<>();
    String header = "";
    if (rows != null) {

      // ヘッダー
      header = String.join(",",
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
      );

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
    writeAllForAppend(output, lines, header,false);
  }

  /**
   * StopsOtherInfoDtoのリストからGTFSのstops.txtに対応するStopRowレコードを構築
   *
   * @param maps 標柱情報のリスト
   * @return StopRowのリスト
   */
  public static List<StopRow> buildStopRowsFromMaps(List<GtfsStopsOtherInfoDto> maps) {
    List<StopRow> rows = new ArrayList<>();
    if (maps == null) return rows;

    Set<String> seen = new HashSet<>();

    for (GtfsStopsOtherInfoDto m : maps) {
      String stopId = nz(m.getBusStopCode()) + "_" + nz(m.getSignpostBranchNumber());
      if (isEmpty(stopId) || seen.contains(stopId)) continue;
      seen.add(stopId);
      String stopName = nz(m.getSignpostName()) + nz(m.getSignpostSubName());

      StopRow row = new StopRow(
          stopId,                                           // stopId
          nz(m.getSignpostCode()),                          // stopCode
          stopName,                                         // stopName
          nz(m.getSignpostDesc()),                          // stopDesc
          numStr(m.getLatitude()),                          // stopLat
          numStr(m.getLongitude()),                         // stopLon
          stopId,                                           // zoneId
          nz(m.getSignpostUrl()),                           // stopUrl
          nz(m.getBusStopType()),                           // locationType
          nz(m.getParentStation()),                         // parentStation
          "",                                               // stopTimezone
          nz(m.getWheelchairAccessibleType()),              // wheelchairBoarding
          nz(m.getPlatformCode())                           // platformCode
      );

      rows.add(row);
    }

    return rows;
  }

  /**
   * 標柱名称の翻訳情報を key=value 形式でファイルに書き出します。
   *
   * @param signpostMaps 標柱情報のリスト
   * @param output       出力先ファイルパス
   * @throws Exception   ファイル操作中の例外
   */
  public static void writeSignpostNameSectionsFromMaps(List<GtfsStopsOtherInfoDto> signpostMaps, Path output) throws Exception {
    List<String> lines = new ArrayList<>();

    // 標柱名称（よみがな）
    if (signpostMaps != null) {
      String beforeCode = "";
      String beforeBranch = "";
      String beforeKana = "";
      for (GtfsStopsOtherInfoDto m : signpostMaps) {
        String code = m.getBusStopCode();
        String branch = m.getSignpostBranchNumber();
        String kana = m.getSignpostKanaName();
        if (!code.isEmpty() && !branch.isEmpty() && !kana.isEmpty() &&
          !(beforeCode.equals(code) && beforeBranch.equals(branch) && beforeKana.equals(kana))) {
          lines.add(toLine(signpostNameKana(kana, code, branch)));
        }
        beforeCode = code;
        beforeBranch = branch;
        beforeKana = kana;
      }
    }

    // 標柱名称（英語）
    if (signpostMaps != null) {
      String beforeCode = "";
      String beforeBranch = "";
      String beforeEn = "";
      for (GtfsStopsOtherInfoDto m : signpostMaps) {
        String code = m.getBusStopCode();
        String branch = m.getSignpostBranchNumber();
        String en = m.getSignpostEnName();
        if (!code.isEmpty() && !branch.isEmpty() && !en.isEmpty() &&
          !(beforeCode.equals(code) && beforeBranch.equals(branch) && beforeEn.equals(en))) {
          lines.add(toLine(signpostNameEn(en, code, branch)));
        }
        beforeCode = code;
        beforeBranch = branch;
        beforeEn = en;
      }
    }
    writeAllForAppend(output, lines, TRANSLATION_HEADER,false);
  }

  /**
   * 停留所コードと標柱枝番を結合して、標柱用のstop_idを生成します。
   *
   * @param busStopCode 停留所コード
   * @param signpostBranchNumber 標柱枝番
   * @return 結合されたstop_id
   */
  private static String buildSignpostStopId(String busStopCode, String signpostBranchNumber) {
    String code = nz(busStopCode);
    String branch = nz(signpostBranchNumber);
    if (code.isEmpty() || branch.isEmpty()) return "";
    return code + "_" + branch;
  }

  /**
   * 停留所名称（カナ）の翻訳情報を生成します。
   *
   * @param kanaName 停留所名称（カナ）
   * @param busStopCode 停留所コード
   * @param signpostBranchNumber 標柱枝番
   * @return TranslationRow（translations.txt用）
   */
  public static TranslationRow signpostNameKana(String kanaName, String busStopCode, String signpostBranchNumber) {
    return new TranslationRow(
        "stops",                                 // テーブル名
        "stop_name",                             // フィールド名
        "ja-Hrkt",                               // 言語
        nz(kanaName),                            // 翻訳先言語
        buildSignpostStopId(busStopCode, signpostBranchNumber), // レコードID
        "",                                      // レコードサブID
        ""                                       // キーワード
    );
  }

  /**
   * 停留所名称（英語）の翻訳情報を生成します。
   *
   * @param enName 英語名称
   * @param busStopCode 停留所コード
   * @param signpostBranchNumber 標柱枝番
   * @return TranslationRow
   */
  public static TranslationRow signpostNameEn(String enName, String busStopCode, String signpostBranchNumber) {
    return new TranslationRow(
        "stops",                                 // テーブル名
        "stop_name",                             // フィールド名
        "en",                                    // 言語
        nz(enName),                              // 翻訳先言語
        buildSignpostStopId(busStopCode, signpostBranchNumber), // レコードID
        "",                                      // レコードサブID
        ""                                       // キーワード
    );
  }
}
