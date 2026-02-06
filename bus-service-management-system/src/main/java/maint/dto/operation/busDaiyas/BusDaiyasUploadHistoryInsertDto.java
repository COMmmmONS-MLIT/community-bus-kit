// Java
package maint.dto.operation.busDaiyas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 便ダイヤアップロード履歴 登録DTO
 *
 * @author jeon
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusDaiyasUploadHistoryInsertDto extends BaseDto implements Serializable {

  private static final long serialVersionUID = 3062581331095561676L;

  /** 便ダイヤアップロード履歴ID */
  private Long uploadHistoryId;
  /** 削除フラグ */
  private Integer delFlag;
  /** 便ID */
  private Long busServiceId;
  /** 選択ファイル名 */
  private String fileName;
  /** 作成ユーザ */
  private String createdUser;
  /** 作成日時 */
  private LocalDateTime createdAt;
  /** 更新ユーザ */
  private String updatedUser;
  /** 更新日時 */
  private LocalDateTime updatedAt;
}
