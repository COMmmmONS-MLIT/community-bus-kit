package jp.co.bus.batch.exception;

/**
 * Exception of Validation
 * 
 * @author hr.kim
 *
 */
public class ValidationException extends RuntimeException {

  /** serialVersionUID */
  private static final long serialVersionUID = -7412759808059101587L;

  /**
   * constructor
   */
  public ValidationException() {
    super();
  }

  /**
   * constructor
   * 
   * @param message
   */
  public ValidationException(String message) {
    super(message);
  }

  /**
   * constructor
   * 
   * @param throwable
   */
  public ValidationException(Throwable throwable) {
    super(throwable);
  }

  /**
   * constructor
   * 
   * @param message
   * @param throwable
   */
  public ValidationException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
