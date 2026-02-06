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
 * 運賃詳細Entity
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineChargeDetailsEntity implements Serializable {

  /** Serial version UID */
  @Serial
  private static final long serialVersionUID = 3892742308325L;

  /** 運賃明細ID */
  private Long lineChargeDetailId;
  /** 運賃ID */
  private Long lineChargeId;
  /** 路線停留所ID From */
  private Long rosenBusStopIdFrom;
  /** 路線停留所ID To */
  private Long rosenBusStopIdTo;
  /** 路線停留所名 From */
  private String rosenBusStopNameFrom;
  /** 路線停留所名 To */
  private String rosenBusStopNameTo;
  /** 運賃 */
  private Integer lineCharge;
  /** ICカード運賃 */
  private Integer icPrice;
  /** UserSession */
  private UserSession session;

  /**
   * コンストラクタ（三角運賃用）
   *
   * @param lineChargeId 運賃ID
   * @param triangle 三角運賃情報
   * @param session ユーザーセッション
   */
  public LineChargeDetailsEntity(Long lineChargeId,
      LineChargeDetailForm.LineChargeTriangle triangle, UserSession session) {

    this.lineChargeDetailId = triangle.getLineChargeDetailId();
    this.lineChargeId = lineChargeId;

    if (triangle.getRosenBusStopIdFrom() == null) {
      throw new IllegalArgumentException("路線停留所ID from がnullです。");
    }

    this.rosenBusStopIdFrom = triangle.getRosenBusStopIdFrom();

    if (triangle.getRosenBusStopIdTo() == null) {
      throw new IllegalArgumentException("路線停留所ID To がnullです。");
    }
    this.rosenBusStopIdTo = triangle.getRosenBusStopIdTo();

    this.rosenBusStopNameFrom = triangle.getRosenBusStopNameFrom();
    this.rosenBusStopNameTo = triangle.getRosenBusStopNameTo();
    this.lineCharge = ExObjectUtils.toI(triangle.getLineCharge());
    this.icPrice = ExObjectUtils.toI(triangle.getIcPrice());
    this.session = session;
  }
}
