package maint.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * コードマスタENUM
 *
 * @author sangsoo.lee
 *
 */
@Getter
@AllArgsConstructor
public enum CodeMasterEnums {

  /** 2要素認証有効期限 */
  SYSTEM_TWO_FACTOR_AUTH_LIMIT_DATE("2要素認証有効期限", "001", "0003"),
  /** 認証コード有効期限 */
  SYSTEM_AUTH_CODE_LIMIT_TIME("認証コード有効期限", "001", "0004"),
  ;

  /** コード名 */
  private final String name;
  /** メインコード */
  private final String mainCode;
  /** サブコード */
  private final String subCode;

}
