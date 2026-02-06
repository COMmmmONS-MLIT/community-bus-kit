package maint.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 日毎便ダイヤ管理メッセージ
 *
 * @author jeon
 */
@Getter
@AllArgsConstructor
public enum DailyBusDaiyasMessage {

  /** ERROR_NOT_SELECTED_PERMISSION */
  ERROR_NOT_SELECTED_DAILYBUSDAIYAS("更新対象データ存在しない", "message.error.dailybusdaiyas.select.blank");

  /** 概要 */
  private final String overview;
  /** メッセージ */
  private final String message;

}
