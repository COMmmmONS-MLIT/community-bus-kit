package maint.dto.operation.calendars;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 運行カレンダー除外日DTO
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CalendarCancelDateDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = -39821646896L;

  /** 運行カレンダー除外日ID */
  private Long calendarCancelDateId;

  /** 運行除外日 */
  private String operationCancelDate;

  /** 削除フラグ（チェックボックス） */
  private boolean isDelete;

  /** 画面側で行の存否を示す一時フラグ */
  private boolean keep = true;

}
