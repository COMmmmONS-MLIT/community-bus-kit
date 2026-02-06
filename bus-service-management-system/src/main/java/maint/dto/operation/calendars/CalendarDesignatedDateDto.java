package maint.dto.operation.calendars;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 運行カレンダー指定日DTO
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CalendarDesignatedDateDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = -436237548254L;

  /** 運行カレンダー指定日ID */
  private Long calendarDesignatedDateId;

  /** 運行指定日 */
  private String operationDesignatedDate;

  /** 削除フラグ（チェックボックス） */
  private boolean isDelete;

  /** 画面側で行の存否を示す一時フラグ */
  private boolean keep = true;

}
