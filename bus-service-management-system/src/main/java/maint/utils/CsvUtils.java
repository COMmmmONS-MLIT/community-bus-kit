package maint.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

/**
 * CSV Utilities
 *
 * @author syuuichi.tsubakihara
 */
public class CsvUtils {

  private static final CsvMapper mapper = new CsvMapper();

  /**
   * CSVから該当クラスに変換
   *
   * @param clazz
   * @param stream
   * @param isHeader true:ヘッダーあり false:ヘッダーなし
   * @return
   * @throws IOException
   */
  public static <T> List<T> read(Class<T> clazz, InputStreamReader stream, Boolean isHeader)
      throws IOException {
    CsvSchema schema = isHeader ?
        mapper.schemaFor(clazz).withHeader().withColumnReordering(true) :
        mapper.schemaFor(clazz).withColumnReordering(true);
    ObjectReader reader = mapper.readerFor(clazz).with(schema);
    return reader.<T>readValues(stream).readAll();
  }
}
