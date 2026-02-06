package jp.co.bus.batch.exception;

/**
 * Database fatal exception
 * 
 * @author g.ko
 *
 */
public class DatabaseFatalException extends RuntimeException {

  /** serialVersionUID */
  private static final long serialVersionUID = -2219531764878467195L;

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
