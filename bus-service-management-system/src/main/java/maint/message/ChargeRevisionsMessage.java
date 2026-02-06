package maint.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 運賃改定メッセージ
 *
 * @author g.ko
 */
@Getter
@AllArgsConstructor
public enum ChargeRevisionsMessage {

  /** FARE_REVISION_INVALID_LINE_DATE */
  FARE_REVISION_INVALID_LINE_DATE("路線利用期間チェックエラー）",
      "validation.chargeRevision.error.lineDate")
  /** FARE_REVISION_INVALID_DATE */
  , FARE_REVISION_INVALID_DATE("運賃改定日チェックエラー）",
      "validation.chargeRevision.error.pastStartDate");

  /** 概要 */
  private final String overview;
  /** メッセージ */
  private final String message;

}
