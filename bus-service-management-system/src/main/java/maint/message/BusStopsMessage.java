package maint.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 停留所メッセージ
 *
 * @author g.ko
 */
@Getter
@AllArgsConstructor
public enum BusStopsMessage {

  /** ERROR_PAST_START_DATE */
  ERROR_PAST_START_DATE("利用開始日のチェックエラー（過去日であるか）",
      "validation.busStop.error.pastStartDate"),
  /** ERROR_PAST_END_DATE */
  ERROR_PAST_END_DATE("利用終了日のチェックエラー（過去日であるか）",
      "validation.busStop.error.pastEndDate"),
  /** ERROR_END_DATE_BEFORE_START_DATE */
  ERROR_END_DATE_BEFORE_START_DATE("利用終了日のチェックエラー（利用開始日より過去日であるか）",
      "validation.busStop.error.endDateBeforeStartDate"),
  /** ERROR_DATE_ROUTE */
  ERROR_DATE_ROUTE("利用期間チェックエラー（系統））",
      "validation.busStop.error.route.usingBusStopOutsideServicePeriod"),
  /** ERROR_LATITUDELONGITUDE_REQUIRED */
  ERROR_LATITUDELONGITUDE_REQUIRED("屋内設定時の緯度経度の入力チェックエラー",
      "validation.busStop.error.latitudeLongitude.required"),
  /** ERROR_BUS_STOP_CODE_DUPLE */
  ERROR_BUS_STOP_CODE_DUPLE("停留所コード重複エラー", "validation.busStop.error.code.duple"),
  /** ERROR_BUS_STOP_CODE_DUPLE */
  ERROR_INPUT_BUS_STOP_CODE_DUPLE("入力停留所コード重複エラー",
      "validation.busStop.error.input.code.duple");

  /** 概要 */
  private final String overview;
  /** メッセージ */
  private final String message;

}
