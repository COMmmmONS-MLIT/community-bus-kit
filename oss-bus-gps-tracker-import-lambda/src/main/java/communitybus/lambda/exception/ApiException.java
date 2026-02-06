package communitybus.lambda.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * API Exception
 * 
 * @author masaru.miyamoto
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ApiException extends RuntimeException {

  /** Serial Version UID */
  private static final long serialVersionUID = 8125616094309367171L;

  /** ステータス */
  private final Integer status;

  /** エラーコード */
  private final String errCd;

  /** エラーメッセージ */
  private final String errMsg;

  /**
   * コンストラクタ
   * 
   * @param errMsg エラーメッセージ
   */
  public ApiException(String errMsg) {
    super(errMsg);
    this.status = null;
    this.errCd = null;
    this.errMsg = errMsg;
  }

  /**
   * コンストラクタ
   * 
   * @param status ステータス
   * @param errCd エラーコード
   * @param errMsg エラーメッセージ
   */
  public ApiException(Integer status, String errCd, String errMsg) {
    super(errMsg);
    this.status = status;
    this.errCd = errCd;
    this.errMsg = errMsg;
  }
}
