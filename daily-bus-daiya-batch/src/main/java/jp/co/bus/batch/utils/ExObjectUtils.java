package jp.co.bus.batch.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/** Object Utility */
public class ExObjectUtils extends StringUtils {

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

  /**
   * NULLの場合、0L返す
   * 
   * @param source
   * @return
   */
  public static Long nullToZero(Long source) {
    return Objects.isNull(source) ? 0L : source;
  }

}
