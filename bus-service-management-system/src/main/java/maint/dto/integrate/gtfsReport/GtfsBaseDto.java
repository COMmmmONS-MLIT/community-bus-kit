package maint.dto.integrate.gtfsReport;

import maint.dto.BaseDto;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * GTFS-Scheduleダウンロード用DTO
 *
 * @author JEON
 */
public class GtfsBaseDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = 3058545306242947168L;

  public record TranslationRow(
      String tableName,     // テーブル名
      String fieldName,     // フィールド名
      String language,      // 言語
      String translation,   // 翻訳先言語
      String recordId,      // レコードID
      String recordSubId,   // レコードサブID
      String fieldValue     // キーワード
  ) {}

  /** 翻訳情報用ヘッダ文字列 */
  protected static final String TRANSLATION_HEADER = "table_name,field_name,language,translation,record_id,record_sub_id,field_value";

  /**
   * EXCELファイルのダウンロード処理を行います。
   *
   * 指定された出力パスに、与えられた文字列リストを元にEXCEL形式で書き込みます。
   *
   * @param output        出力先のパス
   * @param lines         書き込む内容の文字列リスト
   * @param delFlag       既存ファイル削除フラグ
   * @throws Exception    書き込み処理中に発生する可能性のある例外
   */
  protected static void writeAll(Path output, List<String> lines,boolean delFlag) throws Exception {
    if (output.getParent() != null) {
      Files.createDirectories(output.getParent());
    }
    if (delFlag) {
      if (Files.exists(output)) {
        Files.delete(output);
      }
     }
      Files.write(output, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
   }

  /**
   * EXCELファイルのダウンロード処理を行います。
   * 指定されたファイルが存在しない場合のみヘッダを書き込みます。
   *
   * 指定された出力パスに、与えられた文字列リストを元にEXCEL形式で書き込みます。
   *
   * @param output        出力先のパス
   * @param lines         書き込む内容の文字列リスト
   * @param header        ヘッダの文字列
   * @param delFlag       既存ファイル削除フラグ
   * @throws Exception    書き込み処理中に発生する可能性のある例外
   */
  protected static void writeAllForAppend(Path output, List<String> lines, String header,boolean delFlag) throws Exception {
    if (!Files.exists(output)) {
      lines.add(0, header);
    }
    writeAll(output, lines, delFlag);
  }

  /**
   * TranslationRow オブジェクトを1行の文字列形式に変換します。
   *
   * @param r TranslationRow オブジェクト
   * @return 変換された1行の文字列
   */
  protected static String toLine(TranslationRow r) {
    return String.join(",",
        output_format("table_name",   r.tableName),
        output_format("field_name",   r.fieldName),
        output_format("language",     r.language),
        output_format("translation",  r.translation),
        output_format("record_id",    nullToEmpty(r.recordId)),
        output_format("record_sub_id",nullToEmpty(r.recordSubId)),
        output_format("field_value",  nullToEmpty(r.fieldValue))
    );
  }

  /**
   * nullを空文字列に変換します。
   *
   * @param s 対象の文字列
   * @return nullの場合は空文字列、そうでなければ元の文字列
   */
  protected static String nullToEmpty(String s) {
    return s == null ? "" : s;
  }

  /**
   * routeId と serviceId を組み合わせて便ID（Trip ID）を生成します。
   *
   * @param routeId  路線ID
   * @param serviceId サービスID
   * @return 生成された便ID
   */
  protected static String generateTripId(String routeId, String serviceId) {
    String base = (nz(routeId) + "_" + nz(serviceId)).replaceAll("[^A-Za-z0-9_]", "_");
    String rand = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    return base + "_T" + rand;
  }


  /**
   * オブジェクトを数値文字列に変換します。
   *
   * @param v 対象のオブジェクト
   * @return nullの場合は空文字列、そうでなければ文字列に変換した値
   */
  protected static String numStr(Object v) {
    if (v == null)
      return "";
    if (v instanceof Number) {
      Number n = (Number) v;
      String s = n.toString();
      return s;
    }
    return String.valueOf(v);
  }

  /**
   * 文字列が null または空文字列かどうかを判定します。
   *
   * @param s 判定対象の文字列
   * @return null または空文字列の場合は true、それ以外は false
   */
  protected static boolean isEmpty(String s) {
    return s == null || s.isEmpty();
  }

  /**
   * ファイルに出力する文字列として整形して返します。
   *
   * @param key   キー文字列
   * @param value 値文字列
   * @return 整形された文字列
   */
  protected static String output_format(String key, String value) {
    return quote(value == null ? "" : value);
  }

  /**
   * キーと値のペアを文字列として整形して返します。
   *
   * @param key   キー文字列
   * @param value 値文字列
   * @return "key=value" の形式で整形された文字列
   */
  protected static String pair(String key, String value) {
    return key + ":" + quote(value == null ? "" : value);
  }

  /**
   * 文字列を引用符（ダブルクォーテーション）で囲んで返します。
   *
   * @param v 対象の文字列
   * @return 引用符で囲まれた文字列（例："value"）
   */
  private static String quote(String v) {
    return "\"" + v.replace("\"", "\"\"") + "\"";
  }

  /**
   * 指定されたキーの中から、最初に null でない値をマップから取得して返します。
   *
   * @param m    検索対象のマップ
   * @param keys チェックするキーの配列
   * @return 最初に null でない値。すべて null の場合は null を返す。
   */
  protected static Object pickObj(Map<String, Object> m, String... keys) {
    if (m == null || keys == null)
      return null;
    for (String k : keys) {
      if (m.containsKey(k) && m.get(k) != null)
        return m.get(k);
    }
    return null;
  }

  /**
   * オブジェクトを "yyyyMMdd" 形式の日付文字列に変換します。
   *
   * @param v 対象のオブジェクト
   * @return "yyyyMMdd" 形式に変換された日付文字列。変換できない場合は空文字列。
   */
  protected static String toYyyyMMdd(Object v) {
    if (v == null)
      return "";
    if (v instanceof LocalDate) {
      return ((LocalDate) v).format(DateTimeFormatter.BASIC_ISO_DATE);
    }
    String s = String.valueOf(v).trim();
    if (s.isEmpty())
      return "";
    String digits = s.replaceAll("\\D", "");
    if (digits.length() >= 8)
      return digits.substring(0, 8);
    return "";
  }

  /**
   * 文字列が null の場合、空文字列に変換して返します。
   *
   * @param s 対象の文字列
   * @return null の場合は空文字列、そうでなければ元の文字列
   */
  protected static String nz(String s) {
    return s == null ? "" : s;
  }

  /**
   * 文字列を論理値として判定し、"0" または "1" に変換します。
   *
   * @param v 判定対象の文字列
   * @return "1" または "0"（真なら "1"、偽なら "0"）
   */
  protected static String to01(String v) {
    if (v == null)
      return "0";
    String s = v.trim().toLowerCase();
    if (s.isEmpty())
      return "0";
    if (s.equals("1") || s.equals("true") || s.equals("y") || s.equals("yes"))
      return "1";
    if (s.equals("0") || s.equals("false") || s.equals("n") || s.equals("no"))
      return "0";
    try {
      return Integer.parseInt(s) != 0 ? "1" : "0";
    } catch (Exception ignore) {
    }
    return "0";
  }

  protected static Set<Integer> parseWeekdayCodeSet(GtfsCalendarDayTypeDto m) {
    Set<Integer> set = new HashSet<>();
    if (m == null)
      return set;

    Object v = m.getDayWeekType();
    if (v == null)
      return set;

    if (v instanceof Collection) {
      for (Object o : ((Collection<?>) v))
        addIfWeekCode(set, o);
    } else if (v.getClass().isArray()) {
      Object[] arr = (Object[]) v;
      for (Object o : arr)
        addIfWeekCode(set, o);
    } else {
      String s = String.valueOf(v);
      for (String token : s.split("[,\\s]+")) {
        addIfWeekCode(set, token);
      }
    }
    return set;
  }

  private static void addIfWeekCode(Set<Integer> set, Object o) {
    if (o == null)
      return;
    String s = String.valueOf(o).trim();
    if (s.isEmpty())
      return;
    try {
      int code = Integer.parseInt(s);
      if (code == 0 || (code >= 2 && code <= 7))
        set.add(code);
    } catch (NumberFormatException ignore) {
    }
  }

}
