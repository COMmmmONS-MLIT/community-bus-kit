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
 * 提供情報出力DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GtfsDaiyaRevisionDto extends GtfsBaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -310710658029541682L;

  /** 路線ID */
  private Long rosenId;

  /** 便ダイヤ改正ID */
  private Long daiyaRevisionId;

  /** 便ダイヤ改正開始日 */
  private String serviceStartDate;

  /** 便ダイヤ改正終了日 */
  private String serviceEndDate;

  /** 提供組織名 */
  private String providerOrganizationName;

  /** 提供組織URL */
  private String providerOrganizationUrl;

  /** 提供組織連絡先メールアドレス */
  private String providerOrganizationContactEmail;

  /** 提供組織連絡先URL */
  private String providerOrganizationContactUrl;

  public record FeedInfoRow(
      String feedPublisherName,   // 提供組織名
      String feedPublisherUrl,    // 提供組織URL
      String feedLang,            // 提供言語
      String feedStartDate,       // 有効期間開始日
      String feedEndDate,         // 有効期間終了日
      String feedVersion,         // 提供データバージョン
      String feedContactEmail,    // 提供組織連絡先メールアドレス
      String feedContactUrl       // 提供組織連絡先URL
  ) {}

  /**
   * FeedInfoRowのリストからGTFSのfeed_info.txt形式のキー・バリュー情報を構築し、
   * 指定されたパスに書き込みます。
   *
   * @param rows FeedInfoRowレコードのリスト
   * @param output 出力先のファイルパス
   * @throws Exception 書き込み処理中に発生した例外
   */
  public static void writeFeedInfoTxtKeyValue(List<FeedInfoRow> rows, Path output) throws Exception {
    List<String> lines = new ArrayList<>();
    if (rows != null) {
      // ヘッダー
      lines.add(String.join(",",
          "feed_publisher_name",
          "feed_publisher_url",
          "feed_lang",
          "feed_start_date",
          "feed_end_date",
          "feed_version",
          "feed_contact_email",
          "feed_contact_url"
      ));

      for (FeedInfoRow r : rows) {
      String line = String.join(",",
          output_format("feed_publisher_name", r.feedPublisherName),
          output_format("feed_publisher_url", r.feedPublisherUrl),
          output_format("feed_lang", r.feedLang),
          output_format("feed_start_date", r.feedStartDate),
          output_format("feed_end_date", r.feedEndDate),
          output_format("feed_version", r.feedVersion),
          output_format("feed_contact_email", r.feedContactEmail),
          output_format("feed_contact_url", r.feedContactUrl)
       );
      lines.add(line);
     }
    writeAll(output, lines,true);
    }
  }

  /**
   * DaiyaRevisionDtoのリストからGTFSのfeed_info.txtに対応するFeedInfoRowレコードを構築します。
   *
   * @param maps ダイヤ改正情報を保持するDaiyaRevisionDtoのリスト
   * @return FeedInfoRowレコードのリスト
   */
  public static List<FeedInfoRow> buildStopRowsFromMaps(List<GtfsDaiyaRevisionDto> maps) {
    List<FeedInfoRow> rows = new ArrayList<>();
    if (maps == null) return rows;

    Set<String> seen = new HashSet<>();

    for (GtfsDaiyaRevisionDto m : maps) {
      String feedPublisherName = nz(m.getProviderOrganizationName());
      String feedPublisherUrl  = nz(m.getProviderOrganizationUrl());
      String feedLang          = "ja";
      String feedStartDate     = toYyyyMMdd(m.getServiceStartDate());
      String feedEndDate       = toYyyyMMdd(m.getServiceEndDate());
      String feedVersion       = toYyyyMMdd(m.getServiceStartDate()) + "_" + m.getDaiyaRevisionId();
      String feedContactEmail  = nz(m.getProviderOrganizationContactEmail());
      String feedContactUrl    = nz(m.getProviderOrganizationContactUrl());

      if (isEmpty(feedPublisherName) || isEmpty(feedPublisherUrl)) continue;

      String dedupKey = (feedPublisherName + "|" + feedPublisherUrl + "|" + feedLang + "|" + feedVersion).toLowerCase();
      if (!seen.add(dedupKey)) continue;

      rows.add(new FeedInfoRow(
          feedPublisherName,
          feedPublisherUrl,
          feedLang,
          feedStartDate,
          feedEndDate,
          feedVersion,
          feedContactEmail,
          feedContactUrl
      ));
    }

    return rows;
  }

}
