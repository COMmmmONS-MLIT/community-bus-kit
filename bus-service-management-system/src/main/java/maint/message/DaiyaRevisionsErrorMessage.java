package maint.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 便タイヤ改正メッセージ
 *
 * @author jeon
 */
@Getter
@AllArgsConstructor
public enum DaiyaRevisionsErrorMessage {

  /** 運行年月開始日が路線の利用期間外 */
  ERROR_ROUTE_PERIOD("路線利用期間外", "message.error.daiyarevisions.outside.route.period"),
  /** 運行年月開始日が登録済みの便ダイヤ改正開始日より前 */
  ERROR_REVISION_START("改正開始日前", "message.error.daiyarevisions.start.before.revision.start"),
  /** 運行年月開始日が未来日になっていない */
  ERROR_REVISION_START_BEFORE_TOMORROW("改正開始日付入力", "message.error.daiyarevisions.start.date.tomorrow"),
  /** 運行年月開始日が終了日以降 */
  ERROR_AFTER_END("終了日以降", "message.error.daiyarevisions.start.after.end"),
  /** (別キー) 運行年月開始日が登録済みの便ダイヤ改正開始日より前 */
  ERROR_REVISION_START_REGISTERED("改正開始日前", "message.error.daiyarevisions.start.before.revision.start.registered");
  /** 概要 */
  private final String overview;
  /** メッセージ */
  private final String message;

}
