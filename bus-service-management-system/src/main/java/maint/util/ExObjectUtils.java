package maint.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * オブジェクト関連 Utils
 *
 * @author administrator
 */
public class ExObjectUtils extends StringUtils {
  /**
   * default Constructor
   */
  private ExObjectUtils() {
    // default Constructor
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
   * @param list   対象リストオブジェクト
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
   * 渡されたリスト情報が空であるかチェックする。
   *
   * @param list 判定対象リストオブジェクト
   * @return リストが null または、要素が空である場合は真を返す。
   */
  public static boolean isEmpty(List<?> list) {
    return list == null || list.isEmpty();
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
   * 渡されたリスト情報に指定オブジェクトが null でない場合に追加する。
   *
   * @param list 対象リストオブジェクト
   * @param obj  追加オブジェクト
   * @return リスト内容が変更された場合は true, 変更されていない場合は false を返す。
   */
  public static <O> boolean addIfNotNull(List<O> list, O obj) {
    if (list == null || obj == null) {
      return false;
    }
    return list.add(obj);
  }

  /**
   * 渡されたリスト情報の指定インデックスのオブジェクトを取得する。 対象インデックスに値が存在しない場合は null を返す。
   *
   * @param list  対象リストオブジェクト
   * @param index 取得インデックス
   */
  public static <O> O get(List<O> list, int index) {
    try {
      return list.get(index);
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

  /**
   * 指定した条件がtrueの場合にリストに追加する
   *
   * @param list      対象リスト
   * @param obj       追加オブジェクト
   * @param condition 追加条件
   */
  public static <O> boolean add(List<O> list, O obj, boolean condition) {
    if (!condition) {
      return false;
    }
    return addIfNotNull(list, obj);
  }
}
