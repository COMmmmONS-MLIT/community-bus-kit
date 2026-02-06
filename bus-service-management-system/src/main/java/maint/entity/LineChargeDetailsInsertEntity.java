package maint.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.operation.lineChargesInfoReport.LineChargeDetailsDto;

/**
 * 運賃明細 Entity
 *
 * @author jeon
 */

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineChargeDetailsInsertEntity extends BaseEntity {

  /** Serial version UID */
  private static final long serialVersionUID = -2374437114831903773L;

  /** 運賃改定ID */
  private Integer chargeRevisionId;

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
  /**
   * SignpostInsertDto から SignpostInsertDtoEntity へ値をコピーするコンストラクタ。
   *
   * @param insertDto 運賃明細情報の入力DTO
   */
  public LineChargeDetailsInsertEntity(LineChargeDetailsDto insertDto) {
    this.lineChargeDetailId = insertDto.getLineChargeDetailId();
    this.delFlag = insertDto.getDelFlag();
    this.lineChargeId = insertDto.getLineChargeId();
    this.rosenBusStopIdFrom = insertDto.getRosenBusStopIdFrom();
    this.rosenBusStopIdTo = insertDto.getRosenBusStopIdTo();
    this.lineCharge = insertDto.getLineCharge();
    this.icPrice = insertDto.getIcPrice();
    this.chargeRevisionId = insertDto.getLineChargeId();
  }

}
