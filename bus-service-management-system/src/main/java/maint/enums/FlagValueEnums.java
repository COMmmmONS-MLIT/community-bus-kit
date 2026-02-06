package maint.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;

/**
 * フラグEnum
 *
 * @author administrator
 */
@Getter
public enum FlagValueEnums implements ValidateEnumsInterFace {

  /** FALSE */
  FALSE(0, "false", false, "off"),
  /** TRUE */
  TRUE(1, "true", true, "on");

  /**
   * コンストラクタ
   *
   * @param value 数値
   * @param text 文字列値
   * @param bool ブール値
   * @param flagValue フラグ文字列
   */
  FlagValueEnums(Integer value, String text, Boolean bool, String flagValue) {
    this.value = value;
    this.text = text;
    this.bool = bool;
    this.flagValue = flagValue;
  }

  /** 値 */
  private final Integer value;
  /** 名称 */
  private final String text;
  /** ブーリアン */
  private final Boolean bool;
  /** フラグON/OFF */
  private final String flagValue;
  /** Enum名 */
  public static final String ENUM_NAME = "FlagValue";

  /**
   * 値からENUM取得
   *
   * @param value 値
   * @return FlagValueEnums
   */
  public static FlagValueEnums getByValue(Integer value) {
    for (FlagValueEnums e : values()) {
      if (e.getValue().equals(value)) {
        return e;
      }
    }
    return null;
  }

  /**
   * 値から名称取得
   *
   * @param value 値
   * @return 名称
   */
  public static String getTextByValue(Integer value) {
    for (FlagValueEnums e : values()) {
      if (e.getValue().equals(value)) {
        return e.getText();
      }
    }
    return "";
  }

  /**
   * ブーリアンから値取得
   *
   * @param bool ブーリアン
   * @return value 値
   */
  public static Integer getValueByBool(Boolean bool) {
    for (FlagValueEnums e : values()) {
      if (e.getBool().equals(bool)) {
        return e.getValue();
      }
    }
    return Integer.MIN_VALUE;
  }

  /**
   * ENUM一覧をLIST形で取得
   *
   * @return FlagValueEnumsリスト
   */
  public static List<Object> getValueList() {
    return Stream.of(FlagValueEnums.values()).map(FlagValueEnums::getValue)
        .collect(Collectors.toList());
  }
}
