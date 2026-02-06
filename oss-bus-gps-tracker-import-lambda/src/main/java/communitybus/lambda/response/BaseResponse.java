package communitybus.lambda.response;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * BaseResponse
 * 
 * @author masaru.miyamoto
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class BaseResponse implements Serializable {

  /** Serial Version UID */
  private static final long serialVersionUID = 6474622812701360364L;

  /** 処理結果 */
  protected Integer status;

  /** エラーコード */
  private String errCd;

  /** エラーメッセージ */
  private String errMsg;

}
