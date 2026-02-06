package maint.exception;

/**
 * APIエラーException
 *
 * @author ryuichi.kashiwai
 */
public class ApiException extends RuntimeException {

  /** Serial Version UID */
  private static final long serialVersionUID = 8739480097274248201L;

  private final Integer status;

  private final String errCd;

  private final String errMsg;

  public ApiException(String errMsg) {
    super(errMsg);
    this.status = null;
    this.errCd = null;
    this.errMsg = errMsg;
  }

  public ApiException(Integer status, String errCd, String errMsg) {
    super(errMsg);
    this.status = status;
    this.errCd = errCd;
    this.errMsg = errMsg;
  }

  public int getStatus() {
    return this.status;
  }

  public String getErrCd() {
    return this.errCd;
  }

  public String getErrMsg() {
    return this.errMsg;
  }
}
