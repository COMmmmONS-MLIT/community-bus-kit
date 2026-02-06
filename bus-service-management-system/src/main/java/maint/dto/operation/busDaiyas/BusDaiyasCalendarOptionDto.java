package maint.dto.operation.busDaiyas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 便ダイヤ変更画面の運行カレンダーselectBox情報取得用DTO
 *
 * @author Yuta Ishizaka
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusDaiyasCalendarOptionDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 1111111111111111113L;

  /** 運行カレンダーID */
  private String calendarId;
  /** 運行カレンダー名 */
  private String calendarName;

}
