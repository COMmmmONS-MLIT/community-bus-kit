// Java
package maint.dto.integrate.gtfsReport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.utils.ExObjectUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * 経路情報出力DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GtfsLineRouteDto extends GtfsBaseDto  {

  /** Serial Version UID */
  private static final long serialVersionUID = -5521970670719545936L;

  /** 路線コード */
  private String rosenCode;

  /** 系統コード */
  private String lineRouteCode;

  /** 系統番号 */
  private String lineRouteShortName;

  /** 系統番号名称 */
  private String lineRouteNumber;

  /** 系統名称 英語 */
  private String lineRouteNumberEn;

  /** 系統URL */
  private String lineRouteUrl;

  /** 系統背景色 */
  private String lineRouteColor;

  /** 系統文字色 */
  private String lineRouteTextColor;

  /** 系統備考 */
  private String lineRouteRemarks;

  /** 法人番号 */
  private String operationCompanyNo;

  public record RouteRow(
      String routeId,           // 路線ID
      String agencyId,          // 事業者ID
      String routeShortName,    // 路線略称（例：A線）
      String routeLongName,     // 路線正式名称（例：東京駅〜渋谷駅）
      String routeDesc,         // 路線説明
      String routeType,         // 路線種別（例：3 = バス）
      String routeUrl,          // 路線情報URL
      String routeColor,        // 路線カラー（HEX）
      String routeTextColor,    // 路線文字色（HEX）
      String jpParentRouteId    // 親路線ID（日本独自拡張）
  ) {}

  /**
   * GTFS の routes.txt に準じた形式で、RouteRow のリストを key-value ペアとして整形
   *
   * @param rows   書き出す対象となる路線情報のリスト
   * @param output 出力先のファイルパス
   * @throws Exception ファイル書き込み時にエラーが発生した場合
   */
  public static void writeRoutesTxtKeyValue(List<RouteRow> rows, Path output) throws Exception {
    List<String> lines = new ArrayList<>();
    if (rows != null) {

      // ヘッダー
      lines.add(String.join(",",
          "route_id",
          "agency_id",
          "route_short_name",
          "route_long_name",
          "route_desc",
          "route_type",
          "route_url",
          "route_color",
          "route_text_color",
          "jp_parent_route_id"
      ));

      for (RouteRow r : rows) {
        String routeType = (r.routeType == null || r.routeType.isEmpty()) ? "3" : r.routeType;
        String line = String.join(",",
            output_format("route_id", r.routeId),
            output_format("agency_id", r.agencyId),
            output_format("route_short_name", r.routeShortName),
            output_format("route_long_name", r.routeLongName),
            output_format("route_desc", r.routeDesc),
            output_format("route_type", routeType),
            output_format("route_url", r.routeUrl),
            output_format("route_color", r.routeColor),
            output_format("route_text_color", r.routeTextColor),
            output_format("jp_parent_route_id", r.jpParentRouteId)
        );
        lines.add(line);
      }
    }
    writeAll(output,lines,true);
  }

  /**
   * LineRouteDto のリストから GTFS の routes.txt に対応する RouteRow オブジェクトを構築
   *
   * @param maps 路線情報を含む LineRouteDto のリスト
   * @return     重複除外された RouteRow のリスト
   */
  public static List<RouteRow> buildRouteRowsFromMaps(List<GtfsLineRouteDto> maps) {
    List<RouteRow> rows = new ArrayList<>();
    if (maps == null) return rows;

    for (GtfsLineRouteDto m : maps) {
      String routeId        = m.getRosenCode() + "_" + m.getLineRouteCode();
      if (isEmpty(routeId)) continue;

      String agencyId       = nz(m.getOperationCompanyNo());
      String routeShortName = nz(m.getLineRouteShortName());
      String routeLongName  = nz(m.getLineRouteNumber());
      String routeDesc      = nz(m.getLineRouteRemarks());
      String routeType      = "";
      String routeUrl       = nz(m.getLineRouteUrl());
      String routeColor     = nz(m.getLineRouteColor());
      String routeTextColor = nz(m.getLineRouteTextColor());
      String jpParentRouteId = "";

      rows.add(new RouteRow(
          routeId,
          agencyId,
          routeShortName,
          routeLongName,
          routeDesc,
          routeType,
          routeUrl,
          routeColor,
          routeTextColor,
          jpParentRouteId
      ));
    }
    return rows;
  }

  /**
   * 指定された路線IDに対応する英語の路線名称（route_long_name）を TranslationRow として生成
   *
   * @param enRouteLongName 英語の路線名称
   * @param routeId         対象となる路線ID
   * @return                英語名称を含む TranslationRow オブジェクト
   */
  public static TranslationRow routeLongNameEn(String enRouteLongName, String routeId) {
    return new TranslationRow(
        "routes",
        "route_long_name",
        "en",
        nz(enRouteLongName),
        nz(routeId),
        "",
        ""
    );
  }

  /**
   * LineRouteDto のリストから英語の路線名称（route_long_name）を抽出
   *
   * @param routeMaps 英語名称を含む路線情報のリスト
   * @param output    出力先のファイルパス
   * @throws Exception ファイル書き込み時にエラーが発生した場合
   */
  public static void writeRouteLongNameEnFromMaps(List<GtfsLineRouteDto> routeMaps, Path output) throws Exception {
    List<String> lines = new ArrayList<>();
    Set<String> writtenRouteIds = new HashSet<>();
    if (routeMaps != null) {
      for (GtfsLineRouteDto m : routeMaps) {
        String routeId = m.getRosenCode() + "_" + m.getLineRouteCode();
        String enName  = nz(m.getLineRouteNumberEn());

        // route_idが空の場合、スキップ
        if (ExObjectUtils.isBlank(routeId)) {
          continue;
        }

        // すでに同じ route_id を書いている場合、スキップ
        if (!writtenRouteIds.add(routeId)) {
          continue;
        }

        // 英語名が存在しない場合、スキップ
        if (enName.isEmpty()) {
          continue;
        }

        lines.add(toLine(routeLongNameEn(enName, routeId)));
      }
    }
    writeAllForAppend(output,lines, TRANSLATION_HEADER,false);
  }

}
