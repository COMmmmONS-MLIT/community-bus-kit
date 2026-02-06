package maint.dto.planDaiya.dailyBusDaiyas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 日毎便ダイヤ操作履歴DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DailyDaiyaOperationHistoryDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 225635973801224214L;

  /** 日毎便ダイヤ操作履歴ID */
  private Long dailyDaiyaOperationHistoryId;
  /** 削除フラグ */
  private Integer delFlag;
  /** 日毎便ダイヤID */
  private Long dailyBusDaiyaId;
  /** 操作内容 */
  private String operation;
  /** 変更前 */
  private String before;
  /** 変更後 */
  private String after;
  /** 作成ユーザ */
  private String createdUser;
  /** 作成日時 */
  private String createdAt;
  /** 更新ユーザ */
  private String updatedUser;
  /** 更新日時 */
  private String updatedAt;

}
