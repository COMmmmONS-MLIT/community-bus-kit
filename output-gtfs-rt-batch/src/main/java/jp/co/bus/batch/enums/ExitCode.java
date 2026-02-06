package jp.co.bus.batch.enums;

/**
 * exit code
 * 
 * @author koichi.iwata
 *
 */
public enum ExitCode {

  /** success */
  SUCCESS(0),
  /** failure */
  FAILED(1);

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
