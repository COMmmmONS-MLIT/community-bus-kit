package communitybus.lambda.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

/**
 * フラグEnum
 *
 * @author administrator
 *
 */
@Getter
public enum FlagValueEnums {

  /** FALSE */
  FALSE(0, "false", false),
  /** TRUE */
  TRUE(1, "true", true);

  FlagValueEnums(Integer value, String text, Boolean bool) {
    this.value = value;
    this.text = text;
    this.bool = bool;
  }

  /** 値 */
  private final Integer value;
  /** 名称 */
  private final String text;
  /** ブーリアン */
  private final Boolean bool;
  /** Enum名 */
  public static final String ENUM_NAME = "FlagValue";

  /**
   * 値からENUM取得
   *
   * @param value
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
   * @param value
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
   * ENUM一覧をLIST形で取得
   *
   * @return FlagValueEnumsリスト
   */
  public static List<Object> getValueList() {
    return Stream.of(FlagValueEnums.values()).map(FlagValueEnums::getValue)
        .collect(Collectors.toList());
  }
}
