package maint.exception;

/**
 * 共通DB Exception
 *
 * @author administrator
 */
public class CommonDbException extends RuntimeException {

  /** Serial Version UID */
  private static final long serialVersionUID = -9219255264708904925L;

  /**
   * コンストラクタ
   *
   * @param msg メッセージ
   */
  public CommonDbException(String msg) {
    super(msg);
  }

}
