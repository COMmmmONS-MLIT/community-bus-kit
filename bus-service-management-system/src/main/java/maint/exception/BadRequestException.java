package maint.exception;

import lombok.NoArgsConstructor;

/**
 * 不正リクエスト Exception
 *
 * @author administrator
 */
@NoArgsConstructor
public class BadRequestException extends RuntimeException {

  /** Serial Version UID */
  private static final long serialVersionUID = 4430933325358778618L;

  /**
   * コンストラクタ
   *
   * @param msg メッセージ
   */
  public BadRequestException(String msg) {
    super(msg);
  }
}
