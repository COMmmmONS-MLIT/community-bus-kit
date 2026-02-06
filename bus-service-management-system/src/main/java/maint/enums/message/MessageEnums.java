package maint.enums.message;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 画面メッセージEnum
 * 
 * @author sangsoo.lee
 * 
 */
@Getter
@AllArgsConstructor
public enum MessageEnums {
  /**
   * 共通
   **/
  /** 入力項目に誤りがあります。 */
  C00002("C00002", "", "message.C00002"),
  /** 登録完了しました。 */
  C00006("C00006", "", "message.success.register"),
  /** 変更完了しました。 */
  C00007("C00007", "", "message.success.update"),
  /** 削除完了しました。 */
  C00008("C00008", "", "message.success.delete"),
  /** 登録・変更完了しました。 */
  C00009("C00009", "", "message.C00009"),
  /**
   * メール送信に失敗しました。お手数ですが、再度お手続きください。<br>
   * 継続してエラーが発生する場合は、管理者にお問い合わせください。
   */
  C00014("C00014", "", "message.C00014"),
  /**
   * ログイン
   **/
  /** 無効な認証コード又は期限切れです。もう一度確認の上、再入力してください。(AM01003） */
  AM01003("AM01003", "", "message.AM01003"),
  ;

  /** メッセージID */
  private final String messageId;

  /** タイトル */
  private final String title;

  /** メッセージ */
  private final String message;

  /**
   * 成功確認
   * 
   * @return true: 成功, false: エラー
   */
  public boolean isSuccess() {
    List<MessageEnums> successMessages = new ArrayList<>();
    successMessages.add(C00006);
    successMessages.add(C00007);
    successMessages.add(C00008);
    successMessages.add(C00009);
    return successMessages.stream().anyMatch(c -> c.equals(this));
  }
}
