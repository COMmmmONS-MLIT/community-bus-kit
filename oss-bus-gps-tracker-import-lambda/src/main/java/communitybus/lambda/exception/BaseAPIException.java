package communitybus.lambda.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import communitybus.lambda.enums.ErrorInfoEnums;

/**
 * 基本Exception
 * 
 * @author masaru.miyamoto
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class BaseAPIException extends RuntimeException {

  /** Serial Version UID */
  private static final long serialVersionUID = -1798270458284445025L;

  /** ErrorInfoEnums */
  ErrorInfoEnums errorInfoEnum;
  /** ErrorMessage */
  String errorMessage;
  /** Request文字列 */
  String requestString;

  /**
   * コンストラクタ
   *
   * @param message メッセージ
   */
  public BaseAPIException(String message) {
    super(message);
  }

  /**
   * コンストラクタ
   *
   * @param message メッセージ
   * @param cause Throwable
   */
  public BaseAPIException(String message, Throwable cause) {
    super(message, cause);
  }

}
