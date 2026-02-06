package maint.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 収入情報インポートメッセージ
 *
 * @author g.ko
 */
@Getter
@AllArgsConstructor
public enum ImportIncomesMessage {

  /** ERROR_REQUIRED */
  ERROR_REQUIRED("必須チェックエラー", "validation.importIncomes.error.required"),
  /** ERROR_MAX_LENGTH */
  ERROR_MAX_LENGTH("最大桁数チェックエラー）", "validation.importIncomes.error.maxLength"),
  /** ERROR_NUMERIC */
  ERROR_NUMERIC("数字型チェックエラー）", "validation.importIncomes.error.numeric");

  /** 概要 */
  private final String overview;
  /** メッセージ */
  private final String message;

}
