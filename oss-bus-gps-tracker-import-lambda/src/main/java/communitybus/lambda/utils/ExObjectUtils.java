package communitybus.lambda.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * オブジェクト関連 Utils
 * 
 * @author administrator
 *
 */
public class ExObjectUtils extends StringUtils {

  /**
   * いずれかにnullを含むか判定
   *
   * @param values 値
   * @return ture：nullが存在する、false：nullなし
   */
  public static boolean isNullAny(Object... values) {
    for (Object value : values) {
      if (value == null) {
        return true;
      }
    }
    return false;
  }


  /**
   * default Constructor
   */
  private ExObjectUtils() {
    // default Constructor
  }

  /**
   * 渡されたリスト情報に指定オブジェクトを重複無しで追加する。 list.contains メソッドで判定するため、追加オブジェクトには equals / hashCode
   * メソッドを正しく実装しておく必要がある。
   *
   * @param list 対象リストオブジェクト
   * @param obj 追加オブジェクト
   * @return リスト内容が変更された場合は true, 変更されていない場合は false を返す。
   */
  public static <O> boolean addNoDuplication(List<O> list, O obj) {
    if (list == null || obj == null) {
      return false;
    }
    if (!list.contains(obj)) {
      return list.add(obj);
    }
    return false;
  }

  /** 空？ */
  public static boolean isBlank(Object o) {
    if (o instanceof String) {
      return StringUtils.isBlank((String) o);
    }
    if (o instanceof Collection<?>) {
      return ((Collection<?>) o).isEmpty();
    }
    if (o instanceof Map<?, ?>) {
      return ((Map<?, ?>) o).isEmpty();
    }
    if (o instanceof Object[]) {
      return ((Object[]) o).length == 0;
    }
    return o == null;
  }

  /** 空でない？ */
  public static boolean isNotBlank(Object o) {
    return !isBlank(o);
  }

  /**
   * 渡されたリスト情報の指定オブジェクトを削除する。 対象オブジェクトが存在しない場合は false を返す。
   *
   * @param list 対象リストオブジェクト
   * @param target 取得インデックス
   */
  public static <O> boolean remove(List<O> list, O target) {
    try {
      return list.remove(target);
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 渡されたリスト情報が空でないことをチェックする。
   *
   * @param list 判定対象リストオブジェクト
   * @return リストが nullでなく要素が空でない場合は真を返す。
   */
  public static boolean isNotEmpty(List<?> list) {
    return !isEmpty(list);
  }

  /**
   * 渡されたリスト情報が空であるかチェックする。
   *
   * @param list 判定対象リストオブジェクト
   * @return リストが null または、要素が空である場合は真を返す。
   */
  public static boolean isEmpty(List<?> list) {
    return list == null || list.isEmpty();
  }

  /**
   * 渡されたリスト情報が空でないことをチェックする。
   *
   * @param map 判定対象リストオブジェクト
   * @return マップが nullでなく要素が空でない場合は真を返す。
   */
  public static boolean isNotEmpty(Map<?, ?> map) {
    return !isEmpty(map);
  }

  /**
   * 渡されたマップ情報が空であるかチェックする。
   *
   * @param map 判定対象マップオブジェクト
   * @return マップが null または、要素が空である場合は真を返す。
   */
  public static boolean isEmpty(Map<?, ?> map) {
    return map == null || map.isEmpty();
  }

  /** 全て空でない？ */
  public static boolean isNotBlankAll(Object... os) {
    for (Object o : os) {
      if (isBlank(o)) {
        return false;
      }
    }
    return true;
  }

  /** 何れか空でない？ */
  public static boolean isNotBlankAny(Object... os) {
    for (Object o : os) {
      if (isNotBlank(o)) {
        return true;
      }
    }
    return false;
  }

  /** 全て空？ */
  public static boolean isBlankAll(Object... os) {
    for (Object o : os) {
      if (isNotBlank(o)) {
        return false;
      }
    }
    return true;
  }

  /** 何れか空？ */
  public static boolean isBlankAny(Object... os) {
    for (Object o : os) {
      if (isBlank(o)) {
        return true;
      }
    }
    return false;
  }

  /** 等価チェック(String,Numberなどの型変換付) */
  public static boolean equals(Object a, Object b) {
    if (a == null || b == null) {
      return false;
    }
    if (a.getClass() == b.getClass()) {
      return a.equals(b);
    }
    return a.toString().equals(b.toString()); // toStringでよい？
  }

  /**
   * 等価チェック(String,Numberなどの型変換付)<br>
   * 比較対象の両方がnullの場合はtrue
   *
   * @param a 比較対象1
   * @param b 比較対象2
   * @return true：一致、false：不一致
   */
  public static boolean equalsAllowNull(Object a, Object b) {
    if (a == null && b == null) {
      return true;
    }
    return equals(a, b);
  }

  /** 非等価チェック(String,Numberなどの型変換付) */
  public static boolean notEquals(Object a, Object b) {
    return !equals(a, b);
  }

  /** 等価チェック(第一引数とどれかが一致するか) */
  public static boolean equalsAny(Object target, Object... os) {
    return Arrays.asList(os).contains(target);
  }

  /** 等価チェック(第一引数とすべてが一致するか) */
  public static boolean equalsAll(Object target, Object... os) {
    return Stream.of(os).allMatch(e -> e.equals(target));
  }

  /**
   * 渡されたリスト情報に指定オブジェクトが null でない場合に追加する。
   *
   * @param list 対象リストオブジェクト
   * @param obj 追加オブジェクト
   * @return リスト内容が変更された場合は true, 変更されていない場合は false を返す。
   */
  public static <O> boolean addIfNotNull(List<O> list, O obj) {
    if (list == null || obj == null) {
      return false;
    }
    return list.add(obj);
  }

  /**
   * 渡されたリスト情報に指定リストオブジェクトが null でない場合に追加する。
   *
   * @param list 対象リストオブジェクト
   * @param addList 追加リストオブジェクト
   * @return リスト内容が変更された場合は true, 変更されていない場合は false を返す。
   */
  public static <O> boolean addAllIfNotNull(List<O> list, List<O> addList) {
    if (list == null || addList == null) {
      return false;
    }
    return list.addAll(addList);
  }

  /**
   * 渡されたリスト情報の指定インデックスのオブジェクトを取得する。 対象インデックスに値が存在しない場合は null を返す。
   *
   * @param list 対象リストオブジェクト
   * @param index 取得インデックス
   */
  public static <O> O get(List<O> list, int index) {
    try {
      return list.get(index);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 渡されたリスト情報の最初のオブジェクトを取得する。 対象インデックスに値が存在しない場合は null を返す。
   *
   * @param list 対象リストオブジェクト
   */
  public static <O> O getFirst(List<O> list) {
    return get(list, 0);
  }

  /**
   * 渡されたリスト情報の最後のオブジェクトを取得する。 対象インデックスに値が存在しない場合は null を返す。
   *
   * @param list 対象リストオブジェクト
   */
  public static <O> O getLast(List<O> list) {
    if (isBlank(list)) {
      return null;
    }
    return get(list, list.size() - 1);
  }

  /**
   * 渡されたリスト情報の指定オブジェクトを取得する。 同一のオブジェクトが複数存在する場合は、1つ目のものを返す。
   *
   * @param list 対象リストオブジェクト
   */
  public static <O> O getSingle(List<O> list, O target) {
    if (isEmpty(list) || target == null) {
      return null;
    }
    for (O obj : list) {
      if (obj.equals(target)) {
        return obj;
      }
    }
    return null;
  }

  /**
   * 2つのCollectionに、1つでも重複する要素が含まれているか
   *
   * @param col1 検索対象リスト
   * @param col2 検索値リスト
   * @return col1.contains.(col2の各要素)で1件でもtrueが存在すればtrueを返却
   */
  public static <T> boolean containEachElement(Collection<T> col1, Collection<T> col2) {

    // col2の何れかの要素がcol1に含まれているか否かをチェック
    for (T elm : col2) {

      if (col1.contains(elm)) {
        // 含まれている
        return true;
      }
    }
    // 含まれていない
    return false;
  }

  /**
   * ObjFilter
   *
   * @param <T>
   */
  public static interface ObjFilter<T> {

    /**
     * filter
     *
     * @param target
     * @return
     */
    boolean filter(T target);
  }

  /**
   * オブジェクトのディープコピーを行う． 例外が発生した場合は null を返す．
   *
   * @param object Serializable インタフェースを実装したオブジェクト
   * @return ディープコピーされたオブジェクト
   */
  public static <T extends Serializable> T deepCopy(final T object) {
    try {
      return (T) SerializationUtils.clone(object);
    } catch (Exception e) {
      return null;
    }
  }

  /** 文字列化(StaticImport用) */
  public static String toS(Object obj) {
    return ObjectUtils.toString(obj);
  }

  /** 文字列化(StaticImport用) */
  public static String toS(Object obj, String nullStr) {
    return ObjectUtils.toString(obj, nullStr);
  }

  /** 等価チェック(StaticImport用) */
  public static boolean eq(Object a, Object b) {
    return equals(a, b);
  }

  /** 非等価チェック(StaticImport用) */
  public static boolean ne(Object a, Object b) {
    return notEquals(a, b);
  }

  /**
   * 指定した条件がtrueの場合にリストに追加する
   *
   * @param list 対象リスト
   * @param obj 追加オブジェクト
   * @param condition 追加条件
   */
  public static <O> boolean add(List<O> list, O obj, boolean condition) {
    if (!condition) {
      return false;
    }
    return addIfNotNull(list, obj);
  }

  /**
   * KeyGetter
   *
   * @param <K> Keyの型
   * @param <V> 対象オブジェクトの型
   */
  public interface KeyGetter<K, V> {
    /** キーを取得 */
    K getKey(V v);
  }

  /**
   * CollectionをMap(HashMap)へ変換
   *
   * @param collection
   * @param keyGetter
   * @return
   */
  public static <K, V> Map<K, V> convertToMap(Collection<V> collection, KeyGetter<K, V> keyGetter) {
    if (isBlank(collection)) {
      return new HashMap<>();
    }
    Map<K, V> map = new HashMap<>();
    for (V v : collection) {
      map.put(keyGetter.getKey(v), v);
    }
    return map;
  }

  /**
   * DTOリスト → HashMap変換
   *
   * @param dtoList 変換するDTOリスト
   * @return 変換後のMapリスト
   */
  public static HashMap<String, Object> convertHashMap(Object dto) {
    ObjectMapper oMapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    HashMap<String, Object> map = oMapper.convertValue(dto, HashMap.class);
    for (String key : map.keySet()) {
      Object value = map.get(key);
      if (value == null) {
        map.put(key, "");
      }
    }

    return map;
  }

  /**
   * DTOリスト → HashMapリスト変換
   *
   * @param dtoList 変換するDTOリスト
   * @return 変換後のMapリスト
   */
  public static List<HashMap<String, Object>> convertHashMapList(List<?> dtoList) {
    List<HashMap<String, Object>> mapList = new ArrayList<>();
    ObjectMapper oMapper = new ObjectMapper();

    for (Object dto : dtoList) {
      @SuppressWarnings("unchecked")
      HashMap<String, Object> map = oMapper.convertValue(dto, HashMap.class);
      for (String key : map.keySet()) {
        Object value = map.get(key);
        if (value == null) {
          map.put(key, "");
        }
      }
      mapList.add(map);
    }

    return mapList;
  }

  /**
   * DTOリスト → HashMapリスト変換
   *
   * @param dtoList 変換するDTOリスト
   * @return 変換後のMapリスト
   */
  public static List<HashMap<String, Object>> convertHashMapListIfElseInsertBlankDto(
      List<?> dtoList) {
    List<HashMap<String, Object>> mapList = new ArrayList<>();
    ObjectMapper oMapper = new ObjectMapper();

    if (ExObjectUtils.isBlank(dtoList)) {
      mapList.add(new HashMap<String, Object>());
      return mapList;
    }

    for (Object dto : dtoList) {
      @SuppressWarnings("unchecked")
      HashMap<String, Object> map = oMapper.convertValue(dto, HashMap.class);
      for (String key : map.keySet()) {
        Object value = map.get(key);
        if (value == null) {
          map.put(key, "");
        }
      }
      mapList.add(map);
    }

    return mapList;
  }
}
