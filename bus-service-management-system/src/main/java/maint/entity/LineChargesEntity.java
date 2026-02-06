package maint.entity;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maint.form.lineCharges.LineChargeDetailForm;
import maint.session.UserSession;
import maint.utils.ExObjectUtils;

/**
 * 運賃Entity
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineChargesEntity implements Serializable {

  /** Serial version UID */
  @Serial
  private static final long serialVersionUID = 7842309740235L;

  /** 運賃ID */
  private Long lineChargeId;
  /** 運賃改定ID */
  private Long chargeRevisionId;
  /** 運賃区分 */
  private Integer lineChargeDivision;
  /** 均一運賃額 */
  private Integer uniformCharge;
  /** 均一運賃額_ICカード */
  private Integer uniformIcPrice;
  /** 均一運賃乗換可否 */
  private Integer uniformTransferType;
  /** 支払タイミング */
  private Integer paymentMethod;
  /** UserSession */
  private UserSession session;

  /**
   * 画面入力用コンストラクタ
   *
   * @param form 運賃詳細FORM
   * @param session ユーザーセッション
   */
  public LineChargesEntity(LineChargeDetailForm form, UserSession session) {
    this.lineChargeId = form.getLineChargeId();
    this.chargeRevisionId = form.getChargeRevisionId();
    this.lineChargeDivision = form.getLineChargeDivision();
    this.uniformCharge = ExObjectUtils.toI(form.getUniformCharge());
    this.uniformIcPrice = ExObjectUtils.toI(form.getUniformIcPrice());
    this.uniformTransferType = form.getUniformTransferType();
    this.paymentMethod = form.getPaymentMethod();
    this.session = session;
  }
}
