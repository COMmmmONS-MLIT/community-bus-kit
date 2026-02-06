// Java
package maint.dto.integrate.gtfsReport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.enums.LineChargeDivision;
import maint.utils.ExObjectUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * 運賃情報出力DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GtfsFareInfoDto extends GtfsBaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = 4961282460323320775L;

  /** 路線コード */
  private String rosenCode;

  /** 運賃改定ID */
  private String lineChargeDivision;

  /** 運賃ID */
  private String lineChargeId;

  /** 運賃明細ID */
  private String lineChargeDetailId;

  /** 支払いタイミング */
  private String paymentMethod;

  /** 均一運賃乗換可否 */
  private String uniformTransferType;

  /** 法人番号 */
  private String operationCompanyNo;

  /** 停留所コード */
  private String busStopCode;
  /** 停留所コード From */
  private String busStopCodeFrom;
  /** 停留所コード To */
  private String busStopCodeTo;

  /** 系統ID */
  private String lineRouteId;
  /** 系統コード */
  private String lineRouteCode;

  /** 乗降区分 */
  private String rideAlightDivision;

  /** 標柱枝番 */
  private String signpostBranchNumber;
  /** 標柱枝番 From */
  private String signpostBranchNumberFrom;
  /** 標柱枝番 To */
  private String signpostBranchNumberTo;

  /** 均一運賃額 */
  private String uniformCharge;

  /** 運賃 */
  private String lineCharge;

  /** 均一運賃額_ICカード */
  private String uniformIcPrice;

  /** ICカード運賃 */
  private String icPrice;

  /** 標柱ID */
  private String signpostId;

  /** 運賃ID */
  private String fareId;

  /** 経路ID */
  private String routeId;

  /** 出発地ID */
  private String originId;

  /** 到着地ID */
  private String destinationId;

  /** 経由地ID */
  private String containsId;

  /** 運賃改定ID */
  private String chargeRevisionId;

  public record FareAttributesRow(
      String fareId,           // 運賃ID
      String price,            // 通常価格
      String currencyType,     // 通貨（例：JPY）
      String paymentMethod,    // 支払い方法（0: 乗車前, 1: 乗車時）
      String transfers,        // 乗り換え回数（例：0, 1, 2）
      String transferDuration, // 乗り換え有効時間（秒）
      String icPrice,          // ICカード価格（任意）
      String agencyId          // 事業者ID
  ) {}

  public record FareRulesRow(
      String fareId,         // 運賃ID（fare_id）
      String routeId,        // 路線ID（route_id）
      String originId,       // 出発地ID（origin_id）
      String destinationId,  // 到着地ID（destination_id）
      String containsId      // 経由地ID（contains_id）
  ) {}

  /**
   * FareAttributesRowのリストからGTFSのfare_attributes.txt形式のキー・バリュー情報を構築し、
   * 指定されたパスに書き込みます。
   *
   * @param rows FareAttributesRowレコードのリスト
   * @param output 出力先のファイルパス
   * @throws Exception 書き込み処理中に発生した例外
   */
  public static void writeFareAttributesTxtKeyValue(List<FareAttributesRow> rows, Path output) throws Exception {
    List<String> lines = new ArrayList<>();
    if (rows != null) {

      // ヘッダー
      lines.add(String.join(",",
          "fare_id",
          "price",
          "currency_type",
          "payment_method",
          "transfers",
          "transfer_duration",
          "ic_price",
          "agency_id"
      ));

      for (FareAttributesRow r : rows) {
        String line = String.join(",",
            output_format("fare_id", r.fareId),
            output_format("price", r.price),
            output_format("currency_type", r.currencyType),
            output_format("payment_method", r.paymentMethod),
            output_format("transfers", r.transfers),
            output_format("transfer_duration", r.transferDuration),
            output_format("ic_price", r.icPrice),
            output_format("agency_id", r.agencyId)
        );
        lines.add(line);
      }
    }
    writeAll(output, lines,true);
  }

  /**
   * FareInfoDtoのリストからGTFSのfare_attributes.txtに対応するFareAttributesRowレコードを構築します。
   *
   * @param maps 運賃情報のリスト
   * @return FareAttributesRowレコードのリスト
   */
  public static List<FareAttributesRow> buildFareAttributesRowsFromMaps(
      List<GtfsFareInfoDto> maps) {
    List<FareAttributesRow> rows = new ArrayList<>();
    if (ExObjectUtils.isBlank(maps)) {
      return rows;
    }

    Set<String> seenFareIds = new HashSet<>();

    for (GtfsFareInfoDto m : maps) {
      String price = "";
      String icPrice = "";

      if (String.valueOf(LineChargeDivision.FLAT_FARE.getValue())
          .equals(m.getLineChargeDivision())) {
        price = nz(m.getUniformCharge());
        icPrice = nz(m.getUniformIcPrice());
      } else if (String.valueOf(LineChargeDivision.TRIANGULAR_FARE.getValue())
          .equals(m.getLineChargeDivision())) {
        price = nz(m.getLineCharge());
        icPrice = nz(m.getIcPrice());
      }

      String fareId = nz(m.getRosenCode()) + "_" + price + "_" + icPrice;

      if (ExObjectUtils.isBlank(fareId) || !seenFareIds.add(fareId)) {
        continue;
      }

      String currencyType = "JPY";
      String paymentMethod = nz(m.getPaymentMethod());
      String transfers = nz(m.getUniformTransferType());
      String transferDuration = "";
      String agencyId = nz(m.getOperationCompanyNo());

      rows.add(new FareAttributesRow(fareId, price, currencyType, paymentMethod, transfers,
          transferDuration, icPrice, agencyId));
    }

    return rows;
  }

  /**
   * FareRulesRowのリストからGTFSのfare_rules.txt形式のキー・バリュー情報を構築
   *
   * @param rows FareRulesRowレコードのリスト
   * @param output 出力先のファイルパス
   * @throws Exception 書き込み処理中に発生した例外
   */
  public static void writeFareRulesTxtKeyValue(List<FareRulesRow> rows, Path output) throws Exception {
    List<String> lines = new ArrayList<>();
    if (rows != null) {

      // ヘッダー
      lines.add(String.join(",",
          "fare_id",
          "route_id",
          "origin_id",
          "destination_id",
          "contains_id"
      ));

      for (FareRulesRow r : rows) {
        String line =
            String.join(",",
                output_format("fare_id", r.fareId),
                output_format("route_id", r.routeId),
                output_format("origin_id", r.originId),
                output_format("destination_id", r.destinationId),
                output_format("contains_id", r.containsId));
        lines.add(line);
      }
    }
    writeAll(output,lines,true);
  }

  /**
   * FareInfoDtoのリストからGTFSのfare_rules.txtに対応するFareRulesRowレコードを構築
   *
   * @param maps 運賃ルール情報を保持するFareInfoDtoのリスト
   * @return FareRulesRowレコードのリスト
   */
  public static List<FareRulesRow> buildFareRulesRowsFromMaps(List<GtfsFareInfoDto> maps) {
    List<FareRulesRow> rows = new ArrayList<>();
    if (maps == null)
      return rows;

    Set<String> processedUniformLineChargeIds = new HashSet<>();

    for (GtfsFareInfoDto m : maps) {
      String fareId = nz(m.getRosenCode()) + "_" + nz(m.getLineCharge()) + "_" + nz(m.getIcPrice());
      String routeId = nz(m.getRosenCode()) + "_" + nz(m.getLineRouteCode());
      String originId = nz(m.getBusStopCodeFrom()) + "_" + nz(m.getSignpostBranchNumberFrom());
      String destinationId = nz(m.getBusStopCodeTo()) + "_" + nz(m.getSignpostBranchNumberTo());
      String containsId = "";

      //'運賃.運賃区分が「1：均一運賃」の場合、任意項目は未設定で1パターンのみ作成する
      if ("1".equals(nz(m.getLineChargeDivision()))) {
        String lineChargeId = routeId + "_" + nz(m.getLineChargeId());
        if (!processedUniformLineChargeIds.add(lineChargeId)) {
          continue;
        }
        rows.add(new FareRulesRow(fareId, routeId, "", "", containsId));
        continue;
      }

      rows.add(new FareRulesRow(fareId, routeId, originId, destinationId, containsId));
    }
    return rows;
  }
}
