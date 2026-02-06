package maint.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理者Entity
 *
 * @author sangsoo.lee
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeMasterEntity implements Serializable {

  /** Serial version UID */
  private static final long serialVersionUID = 7935808126933379307L;

  /** コードマスタID */
  private Long codeMasterId;

  /** コード名 */
  private String codeName;

  /** メインコード */
  private String mainCode;

  /** サブコード */
  private String subCode;

  /** コードキー */
  private String codeKey;

  /** コード値 */
  private double codeValue;

  /** コードテキスト */
  private String codeText;

  /**
   * コード値をLong型に変換
   *
   * @return コード値
   */
  public Long getCodeValueToLong() {
    return Double.valueOf(this.codeValue).longValue();
  }

}
