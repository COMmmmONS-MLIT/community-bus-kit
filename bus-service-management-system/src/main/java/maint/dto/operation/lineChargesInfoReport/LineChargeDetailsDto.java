package maint.dto.operation.lineChargesInfoReport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 標柱情報インポート登録DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LineChargeDetailsDto extends BaseDto {

  private static final long serialVersionUID = 1893423045382932774L;

  /** 運賃明細ID */
  private Integer lineChargeDetailId;

  /** 削除フラグ */
  private Integer delFlag;

  /** 運賃ID */
  private Integer lineChargeId;

  /** 路線停留所ID From */
  private Integer rosenBusStopIdFrom;

  /** 路線停留所ID To */
  private Integer rosenBusStopIdTo;

  /** 運賃 */
  private Integer lineCharge;

  /** ICカード運賃 */
  private Integer icPrice;

}

