package maint.dto.operation.displayDaiyas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;
import java.util.List;
import java.util.Map;

/**
 * 棒ダイヤDTO
 *
 * @author y.kim
 */

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DisplayTimelineResult extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 33457444443458708L;

  /** グループ化されたデータ（仕業番号ごと） */
  private Map<String, List<DisplayDaiyasDto>> grouped;

  /** 各仕業番号に対応する色データのマップ（1分単位） */
  private Map<String, List<String>> colorsMap;

  /** 各仕業番号に対応する出発時刻ラベルのマップ（1分単位） */
  private Map<String, List<String>> departureTimeLabelsMap;

  /** 各仕業番号に対応する到着時刻ラベルのマップ（1分単位） */
  private Map<String, List<String>> arrivalTimeLabelsMap;

  /** 表示対象の合計分数 */
  private int totalMinutes;
}
