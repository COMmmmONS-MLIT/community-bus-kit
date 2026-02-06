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
 * 事業者情報出力DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GtfsAgencyInfoDto extends GtfsBaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -7525660780609290589L;

  /** 事業者名・自治体表示名 */
  private String dispName;

  /** 電話番号 */
  private String phoneNumber;

  /** 連絡先メール */
  private String mailAddress;

  /** 事業者名・自治体名 */
  private String name;

  /** 事業者名・自治体名 かな */
  private String kanaName;

  /** 事業者名・自治体名 英語 */
  private String enName;

  /** 運賃情報URL */
  private String fareUrl;

  /** 法人番号 */
  private String operationCompanyNo;

  /** 提供組織URL */
  private String providerOrganizationUrl;

  public record AgencyRow(
      String agencyId,        // 事業者ID
      String agencyName,      // 事業者名称
      String agencyUrl,       // 事業者URL
      String agencyPhone,     // 電話番号
      String agencyFareUrl,   // オンライン購入URL
      String agencyEmail      // 事業者Eメール
  ) {}

  /**
   * AgencyRow のリストを CSV形式の key-value ペアとして整形し、指定されたファイルに書き出します。
   *
   * @param rows   書き出す対象となる事業者情報のリスト
   * @param output 出力先のファイルパス（.txt など）
   * @throws Exception ファイル書き込み時にエラーが発生した場合
   */
  public static void writeAgencyTxtKeyValue(List<AgencyRow> rows, Path output) throws Exception {
    List<String> lines = new ArrayList<>();
    if (rows != null) {

      // ヘッダー
      lines.add(String.join(",",
          "agency_id",
          "agency_name",
          "agency_url",
          "agency_timezone",
          "agency_lang",
          "agency_phone",
          "agency_fare_url",
          "agency_email"
      ));

      for (AgencyRow r : rows) {
        String line = String.join(",",
            output_format("agency_id", r.agencyId()),
            output_format("agency_name", r.agencyName()),
            output_format("agency_url", r.agencyUrl()),
            output_format("agency_timezone", "Asia/Tokyo"),
            output_format("agency_lang", "ja"),
            output_format("agency_phone", r.agencyPhone()),
            output_format("agency_fare_url", r.agencyFareUrl()),
            output_format("agency_email", r.agencyEmail()));
        lines.add(line);
      }
    }
    writeAll(output, lines,true);
  }

  /**
   * AgencyInfoDto のリストから、重複しない事業者情報（AgencyRow）を抽出してリストとして返します。
   *
   * @param maps AgencyInfoDto のリスト
   * @return 重複除外された AgencyRow のリスト
   */
  public static List<AgencyRow> buildStopRowsFromMaps(List<GtfsAgencyInfoDto> maps) {
    List<AgencyRow> rows = new ArrayList<>();
    if (maps == null) return rows;

    Set<String> seenAgencyIds = new HashSet<>();

    for (GtfsAgencyInfoDto m : maps) {
      String agencyId      = nz(m.getOperationCompanyNo());
      String agencyName    = nz(m.getDispName());
      String agencyUrl     = nz(m.getProviderOrganizationUrl());
      String agencyPhone   = nz(m.getPhoneNumber());
      String agencyFareUrl = nz(m.getFareUrl());
      String agencyEmail   = nz(m.getMailAddress());

      if (isEmpty(agencyId) || isEmpty(agencyName)) continue;
      if (!seenAgencyIds.add(agencyId)) continue;

      rows.add(new AgencyRow(agencyId, agencyName, agencyUrl, agencyPhone, agencyFareUrl, agencyEmail));
    }
    return rows;
  }

  /**
   * 事業者名称（ひらがな表記）を TranslationRow として構築します。
   *
   * @param kanaName        事業者名のひらがな表記
   * @param corpNo          法人番号
   * @param originalNameJa  元の日本語表記の事業者名
   * @return                生成された TranslationRow オブジェクト
   */
  public static TranslationRow agencyNameKana(String kanaName, String corpNo, String originalNameJa) {
    return new TranslationRow(
        "agency",        // tableName
        "agency_name",            // fieldName
        "ja-Hrkt",                // language
        nz(kanaName),             // translation
        nz(corpNo),               // recordId
        "",                       // recordSubId
        ""                        // fieldValue
    );
  }

  /**
   * 事業者名称（英語表記）を TranslationRow として構築します。
   *
   * @param enName           英語表記の事業者名
   * @param corpNo           法人番号
   * @param originalNameJa   元の日本語表記の事業者名
   * @return                 生成された TranslationRow オブジェクト
   */
  public static TranslationRow agencyNameEn(String enName, String corpNo, String originalNameJa) {
    return new TranslationRow(
        "agency",        // tableName
        "agency_name",            // fieldName
        "en",                     // language
        nz(enName),               // translation
        nz(corpNo),               // recordId
        "",                       // recordSubId
        ""                        // fieldValue
    );
  }

  /**
   * AgencyInfoDto のリストから事業者名称の翻訳情報（かな・英語）を抽出
   *
   * @param feedInfoRowMaps AgencyInfoDto のリスト
   * @param output          出力先のファイルパス（
   * @throws Exception      ファイル書き込み時にエラーが発生した場合
   */
  public static void writeAgencyNameSectionsFromMaps(List<GtfsAgencyInfoDto> feedInfoRowMaps, Path output) throws Exception {
    List<String> lines = new ArrayList<>();

    // ■事業者名称（よみがな）
    if (feedInfoRowMaps != null) {
      for (GtfsAgencyInfoDto m : feedInfoRowMaps) {
        String agencyJa = nz(m.getName());
        String agencyKana = nz(m.getKanaName());
        String corpNo = nz(m.getOperationCompanyNo());
        if (!agencyKana.isEmpty()) {
          GtfsBaseDto.TranslationRow r = agencyNameKana(agencyKana, corpNo, agencyJa);
          lines.add(toLine(r));
        }
      }
    }

    // ■事業者名称（英語）
    if (feedInfoRowMaps != null) {
      for (GtfsAgencyInfoDto m : feedInfoRowMaps) {
        String agencyJa = nz(m.getName());
        String agencyEn = nz(m.getEnName());
        String corpNo = nz(m.getOperationCompanyNo());
        if (!agencyEn.isEmpty()) {
          GtfsBaseDto.TranslationRow r = agencyNameEn(agencyEn, corpNo, agencyJa);
          lines.add(toLine(r));
        }
      }
    }
    writeAllForAppend(output, lines, TRANSLATION_HEADER,false);
  }

}
