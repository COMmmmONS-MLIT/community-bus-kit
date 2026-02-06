package communitybus.lambda.enums;

/**
 * exit code
 *
 * @author administrator
 *
 */
public enum ExitCode {

  /** success */
  SUCCESS(0),
  /** failure */
  FAILED(1);

  /** code */
  private int code;

  /**
   * constructor
   *
   * @param code
   */
  private ExitCode(int code) {
    this.code = code;
  }

  /**
   * get code
   *
   * @return
   */
  public int getCode() {
    return this.code;
  }
}
