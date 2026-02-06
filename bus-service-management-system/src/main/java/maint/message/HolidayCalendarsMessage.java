package maint.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 祝日カレンダーメッセージ
 *
 * @author g.ko
 */
@Getter
@AllArgsConstructor
public enum HolidayCalendarsMessage {

  /** ERROR_FILE_FORMAT */
  ERROR_FILE_FORMAT("ファイルフォーマットチェックエラー",
      "validation.holidayCalendar.error.file.format"),
  /** ERROR_EMPTY_FILE */
  ERROR_EMPTY_FILE("ファイル空チェックエラー",
      "validation.holidayCalendar.error.file.empty"),
  /** HEADER_FIRST */
  HEADER_FIRST("一つ目のファイルヘッダー",
      "validation.holidayCalendar.header.first"),
  /** HEADER_SECOND */
  HEADER_SECOND("二つ目のファイルヘッダー",
      "validation.holidayCalendar.header.second"),
  /** ERROR_HEADER */
  ERROR_HEADER("ヘッダーチェックエラー",
      "validation.holidayCalendar.error.header"),
  /** ERROR_COLUMN_COUNT */
  ERROR_COLUMN_COUNT("行数のチェックエラー",
      "validation.holidayCalendar.error.column.count"),
  /** ERROR_DATE_FORMAT */
  ERROR_DATE_FORMAT("日付チェックエラー",
      "validation.holidayCalendar.error.date.format"),
  /** ERROR_INPUT_FILE */
  ERROR_INPUT_FILE("ファイル読み込み失敗",
      "validation.holidayCalendar.error.input.file");

  /** 概要 */
  private final String overview;
  /** メッセージ */
  private final String message;

}
