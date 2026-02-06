package communitybus.lambda.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * エラー情報Enums
 * 
 * @author y.kim
 *
 */
@Getter
public enum ErrorInfoEnums {

  /** Common Error E0000 ~ */
  /** E0001：パラメータ不正 */
  INVALID_PARAMETER("E0001", "パラメータに誤りがあります。", HttpStatus.BAD_REQUEST.value()),
  /** E0002：DBエラー */
  DATABASE_REGISTRATION_FAILED("E0002", "トラッカー情報が登録できませんでした。", HttpStatus.INTERNAL_SERVER_ERROR.value()),
  /** E0003：GTFS-RT情報送信エラー */
  GTFS_RT_SEND_FAILED("E0003", "GTFS-RT情報が送信できませんでした。", HttpStatus.INTERNAL_SERVER_ERROR.value()),
  /** E0004：アクセスキー整合性チェックエラー */
  INVALID_ACCESS_KEY("E0004", "アクセスキーに誤りがあります。", HttpStatus.BAD_REQUEST.value()),
  /** E9999：システムエラー */
  SYSTEM_ERROR("E9999", "システムエラーが発生しました。", HttpStatus.INTERNAL_SERVER_ERROR.value()),

  ;

  /**
   * コンストラクタ
   * 
   * @param code エラーコード
   * @param message メッセージ
   */
  ErrorInfoEnums(String code, String message, int status) {
    this.code = code;
    this.message = message;
    this.status = status;
  }

  /** コード */
  private final String code;
  /** メッセージ */
  private final String message;
  /** ステータス */
  private final int status;
}
