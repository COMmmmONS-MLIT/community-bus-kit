package communitybus.lambda.dto;

import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import communitybus.lambda.enums.ErrorInfoEnums;

/**
 * 出力DTO
 *
 * @author y.kim
 *
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OutputDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -4993503148758872763L;

  /** ステータス */
  private Integer status;
  /** エラーコード */
  private String errCd;
  /** エラーメッセージ */
  private String errMsg;

  /**
   * コンストラクタ
   */
  public OutputDto() {
    this.status = HttpStatus.OK.value();
  }

  /**
   * コンストラクタ
   *
   * @param errorInfo エラー情報Enums
   */
  public OutputDto(ErrorInfoEnums errorInfo) {
    this.errCd = errorInfo.getCode();
    this.errMsg = errorInfo.getMessage();
    this.status = errorInfo.getStatus();
  }

}
