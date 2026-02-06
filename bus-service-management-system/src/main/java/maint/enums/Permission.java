package maint.enums;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 権限フラグ
 *
 * @author y.kim
 */
@Getter
public enum Permission implements ValidateEnumsInterFace {
  /** 権限なし */
  NOT_HAVE_PERMISSION("無", "0"),
  /** 権限あり */
  HAVE_PERMISSION("有", "1");

  /** 名称 */
  private final String text;
  /** 値 */
  private final String value;

  /**
   * コンストラクタ
   *
   * @param text  名称
   * @param value 値
   */
  Permission(String text, String value) {
    this.text = text;
    this.value = value;
  }

  /**
   * 値で名称取得
   *
   * @param value 値
   * @return 名称
   */
  public static String getTextByValue(String value) {
    for (Permission v : values()) {
      if (v.getValue().equals(value)) {
        return v.getText();
      }
    }
    return "";
  }

  /**
   * Valueを全て取得
   *
   * @return 値のリスト
   */
  public static List<Object> getValueList() {
    return Stream.of(Permission.values()).map(Permission::getValue).collect(Collectors.toList());
  }
}
