package jp.co.bus.batch.exception;

/**
 * Exception of Usage
 * 
 * @author hr.kim
 *
 */
public class UsageException extends RuntimeException {

  /** serialVersionUID */
  private static final long serialVersionUID = 5745114178226487091L;

  /**
   * constructor
   */
  public UsageException() {
    super();
  }

  /**
   * constructor
   * 
   * @param message
   */
  public UsageException(String message) {
    super(message);
  }

  /**
   * constructor
   * 
   * @param cause
   */
  public UsageException(Throwable cause) {
    super(cause);
  }

  /**
   * constructor
   * 
   * @param message
   * @param cause
   */
  public UsageException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * print usage to system
   */
  public void printUsage() {
    System.out.println("----------------------- usage ------------------------------------");
    System.out.println("-i : order id");
    System.out.println("------------------------------------------------------------------");
  }

}
