package communitybus.lambda.exception;

/**
 * Database fatal exception
 *
 * @author administrator
 *
 */
public class DatabaseFatalException extends RuntimeException {

  /** Serial version UID */
  private static final long serialVersionUID = -7449603526410616378L;

  /**
   * constructor
   */
  public DatabaseFatalException() {
    super();
  }

  /**
   * constructor
   *
   * @param message
   */
  public DatabaseFatalException(String message) {
    super(message);
  }

  /**
   * constructor
   *
   * @param throwable
   */
  public DatabaseFatalException(Throwable throwable) {
    super(throwable);
  }

  /**
   * constructor
   *
   * @param message
   * @param throwable
   */
  public DatabaseFatalException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
