package jp.co.bus.batch.enums;

import org.apache.commons.lang3.StringUtils;
import lombok.Getter;

/**
 * メール送信区分ENUM
 * 
 * @author hr.kim
 *
 */
@Getter
public enum SendMailTypeEnums {

  /** SES */
  SES("ses", "SES")
  /** SMTP */
  ,SMTP("smtp", "SMTP");

  SendMailTypeEnums(String type, String text) {
    this.type = type;
    this.text = text;
  }

  /** 値 */
  private final String type;

  /** 名称 */
  private final String text;

  /**
   * 名称で取得
   * 
   * @param text
   * @return
   */
  public static SendMailTypeEnums getByText(String text) {
    if (StringUtils.isBlank(text))
      return null;
    for (SendMailTypeEnums e : SendMailTypeEnums.values()) {
      if (e.getText().equals(text))
        return e;
    }
    return null;
  }

  /**
   * 区分で取得
   * 
   * @param type
   * @return
   */
  public static SendMailTypeEnums getByType(String type) {
    if (StringUtils.isBlank(type))
      return null;
    for (SendMailTypeEnums e : SendMailTypeEnums.values()) {
      if (e.getType().equals(type))
        return e;
    }
    return null;
  }

  /**
   * 区分で名称取得
   * 
   * @param type
   * @return
   */
  public static String getTextByType(String type) {
    if (StringUtils.isBlank(type))
      return null;
    for (SendMailTypeEnums e : SendMailTypeEnums.values()) {
      if (e.getType().equals(type))
        return e.getText();
    }
    return null;
  }

}
