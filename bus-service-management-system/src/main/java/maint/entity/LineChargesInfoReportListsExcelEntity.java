package maint.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.operation.lineChargesInfoReport.LineChargesInfoReportInsertDto;

/**
 * 運賃Entity
 *
 * @author jeon
 */

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineChargesInfoReportListsExcelEntity extends BaseEntity {

  /** Serial version UID */
  private static final long serialVersionUID = 2996026700336513854L;

  /** 運賃ID */
  private Integer lineChargeId;

  /** 運賃改定ID */
  private String chargeRevisionId;

  /** 運賃区分 */
  private String lineChargeDivision;

  /** 均一運賃額 */
  private Integer uniformCharge;

  /** 均一運賃額_ICカード */
  private Integer uniformIcPrice;

  /** 均一運賃乗換可否 */
  private String uniformTransferType;

  /** 支払タイミング */
  private String paymentMethod;

  /**
   * LineChargesInfoReportInsertDto から LineChargesInfoReportListsExcelEntity へ値をコピーするコンストラクタ。
   *
   * @param lineBusStop Excelアップロード用の運賃データDTO
   */
  public LineChargesInfoReportListsExcelEntity(LineChargesInfoReportInsertDto lineBusStop) {
    this.lineChargeId = lineBusStop.getLineChargeId();
    this.chargeRevisionId = lineBusStop.getChargeRevisionId();
    this.lineChargeDivision = lineBusStop.getLineChargeDivision();
    this.uniformCharge = lineBusStop.getUniformCharge();
    this.uniformIcPrice = lineBusStop.getUniformIcPrice();
    this.uniformTransferType = lineBusStop.getUniformTransferType();
    this.paymentMethod = lineBusStop.getPaymentMethod();
  }

}
