package jp.co.bus.batch.exception;

/**
 * S3アップロード例外
 *
 * @author s-fujioka
 *
 */
public class S3UploadException extends RuntimeException {

  /** Serial Version UID */
  private static final long serialVersionUID = 2904389648215033272L;

  /**
   * Constructor
   */
  public S3UploadException() {
    super();
  }

  /**
   * Constructor
   *
   * @param message
   */
  public S3UploadException(String message) {
    super(message);
  }

  /**
   * Constructor
   *
   * @param throwable
   */
  public S3UploadException(Throwable throwable) {
    super(throwable);
  }

  /**
   * Constructor
   *
   * @param message
   * @param throwable
   */
  public S3UploadException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
