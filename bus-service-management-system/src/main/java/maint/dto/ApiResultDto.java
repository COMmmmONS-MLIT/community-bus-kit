package maint.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * API通信結果DTO
 *
 * @author administrator
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ApiResultDto implements Serializable {

  /** Serial Version UID */
  private static final long serialVersionUID = 2326716752230225941L;

  /** API通信結果 */
  private boolean result;

  /** メッセージ */
  private String message;

  /**
   * コンストラクタ
   *
   * @param result  API通信結果
   * @param message メッセージ
   */
  public ApiResultDto(boolean result, String message) {
    this.result = result;
    this.message = message;
  }
}
