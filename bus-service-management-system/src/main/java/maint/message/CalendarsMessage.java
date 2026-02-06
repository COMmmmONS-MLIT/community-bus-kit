package maint.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 運行カレンダーメッセージ
 *
 * @author g.ko
 */
@Getter
@AllArgsConstructor
public enum CalendarsMessage {

  /** ERROR_PAST_DATE */
  ERROR_PAST_DATE("過去日チェックエラー", "validation.calendar.past.date"),
  /** ERROR_DATE_FORMAT */
  ERROR_DATE_FORMAT("日付フォーマットチェックエラー）", "validation.calendar.date.format"),
  /** ERROR_OVERLAP_CANCEL_AND_DESIGNATED */
  ERROR_OVERLAP_CANCEL_AND_DESIGNATED("運行除外日と運行指定日の重複エラー）",
      "validation.calendar.overlap.cancel_and_designated"),
  /** ERROR_REQUIRED_OPERATION_DESIGNATED_DATE */
  ERROR_REQUIRED_OPERATION_DESIGNATED_DATE("運行指定日未入力エラー",
      "validation.calendar.designated.date.required"),
  /** ERROR_REQUIRED_FUTURE_OPERATION_DESIGNATED_DATE */
  ERROR_REQUIRED_FUTURE_OPERATION_DESIGNATED_DATE("未来日の運行指定日の未入力エラー",
      "validation.calendar.designated.futureDate.required");


  /** 概要 */
  private final String overview;
  /** メッセージ */
  private final String message;

}
