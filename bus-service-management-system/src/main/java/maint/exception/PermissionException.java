package maint.exception;

/**
 * 権限 Exception
 *
 * @author administrator
 */
public class PermissionException extends RuntimeException {

  /** Serial Version UID */
  private static final long serialVersionUID = 6401731520901303572L;

  /**
   * コンストラクタ
   *
   * @param msg メッセージ
   */
  public PermissionException(String msg) {
    super(msg);
  }

}
