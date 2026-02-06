// Java
package maint.dto.operation.importBusServices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

import java.time.LocalDateTime;

/**
 * 2-3) 便ダイヤ明細登録用 DTO 便ダイヤ明細IDはDBシーケンスで採番
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ImportBusServicesDetailInsertDto extends BaseDto {

  private static final long serialVersionUID = 8711264565797280074L;

  /** 日毎便ダイヤID（シーケンス採番） */
  private Long busDaiyaId;
  /** 削除フラグ */
  private Integer delFlag;
  /** 便ID */
  private Long busServiceId;
  /** 系統停留所ID */
  private Integer lineRouteBusStopId;
  /** 到着時刻(HHMM) */
  private String busStopArrivalTime;
  /** 出発時刻(HHMM) */
  private String busStopDepartureTime;
  /** 運行順序 */
  private Integer stopSequence;
  /** 作成者 */
  private String createdUser;
  /** 作成日時 */
  private LocalDateTime createdAt;
  /** 更新者 */
  private String updatedUser;
  /** 更新日時 */
  private LocalDateTime updatedAt;

}
