package maint.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 共通メッセージ
 *
 * @author masaya.tanaka
 */
@Getter
@AllArgsConstructor
public enum CommonMessage {

  /** SUCCESS_REGISTER */
  SUCCESS_REGISTER("登録完了", "message.success.register"),
  /** SUCCESS_UPDATE */
  SUCCESS_UPDATE("変更完了", "message.success.update"),
  /** SUCCESS_DELETE */
  SUCCESS_DELETE("削除完了", "message.success.delete"),
  /** メール送信成功 */
  SUCCESS_SEND_MAIL("メール送信成功", "message.success.send.mail"),
  /** FILE_DOWNLOAD_ERROR_EXCEL */
  FILE_DOWNLOAD_SUCCESS_EXCEL("EXCELダウンロード完了", "message.success.file.download.excel"),
  /** ERROR_SYSTEM */
  ERROR_SYSTEM("システムエラー", "message.error.system"),
  /** ERROR_SEARCH */
  ERROR_GET_DATA("データ取得エラー", "message.error.get.data"),
  /** ERROR_SEARCH_CORRECT_DATA */
  ERROR_GET_CORRECT_DATA("対象データ取得エラー", "message.error.get.correct.data"),
  /** ERROR_REGISTER */
  ERROR_REGISTER("登録エラー", "message.error.register"),
  /** ERROR_UPDATE */
  ERROR_UPDATE("変更エラー", "message.error.update"),
  /** ERROR_DELETE */
  ERROR_DELETE("削除エラー", "message.error.delete"),
  /** ERROR_INPUT */
  ERROR_INPUT("入力エラー", "message.error.input"),
  /** ERROR_API */
  ERROR_API("APIエラー", "message.error.api"),
  /** FAIL_SESSION_ID */
  ERROR_SESSION("セッションエラー", "message.error.session"),
  /** FILE_DOWNLOAD_ERROR_CSV */
  FILE_DOWNLOAD_ERROR_CSV("CSVダウンロードエラー", "message.error.file.download.csv"),
  /** FILE_DOWNLOAD_ERROR_EXCEL */
  FILE_DOWNLOAD_ERROR_EXCEL("EXCELダウンロードエラー", "message.error.file.download.excel"),
  /** メール送信エラー */
  ERROR_SEND_MAIL("メール送信エラー", "message.error.send.mail"),
  /** 必須項目チェックエラー */
  ERROR_REQUIRED("必須項目チェックエラー", "message.error.required"),
  ;

  /** 概要 */
  private final String overview;
  /** メッセージ */
  private final String message;

}
