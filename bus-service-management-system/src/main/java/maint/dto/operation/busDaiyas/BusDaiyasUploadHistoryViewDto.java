// Java
package maint.dto.operation.busDaiyas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

import java.time.LocalDateTime;

/**
 * 便ダイヤアップロード履歴 表示DTO
 *
 * @author jeon
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusDaiyasUploadHistoryViewDto extends BaseDto {

  private static final long serialVersionUID = 834535625840761117L;

  /** 履歴ID */
  private Long uploadHistoryId;
  /** 削除フラグ */
  private Integer delFlag;
  /** 便ID */
  private Long busServiceId;
  /** ファイル名 */
  private String fileName;
  /** 作成ユーザ */
  private String createdUser;
  /** 作成日 */
  private LocalDateTime createdAt;
  /** 更新者 */
  private String updatedUser;
  /** 更新日 */
  private LocalDateTime updatedAt;

}
