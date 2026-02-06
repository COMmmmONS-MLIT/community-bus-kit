package maint.dto.operation.lineCharges;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 運賃詳細DTO
 *
 * @author g.ko
 */
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class LineChargeDetailDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -847189374830L;

  /** 運賃ID */
  private Long lineChargeId;

  /** 路線ID */
  private Long rosenId;

  /** 路線名 */
  private String rosenName;

  /** 運賃改定ID */
  private Long chargeRevisionId;

  /** 運賃適用開始日From */
  private String serviceStartDate;

  /** 運賃適用開始日To */
  private String serviceEndDate;

  /** 運賃改定理由 */
  private String revisionReason;

  /** 支払タイミング */
  private Integer paymentMethod;

  /** 運賃区分 */
  private Integer lineChargeDivision;

  /** 均一運賃 */
  private Integer uniformCharge;

  /** ICカード均一運賃 */
  private Integer uniformIcPrice;

  /** 均一運賃乗換可否 */
  private Integer uniformTransferType;

  /** 運賃詳細ID */
  private Long lineChargeDetailId;

  /** 路線停留所from ID */
  private Long rosenBusStopIdFrom;

  /** 路線停留所to ID */
  private Long rosenBusStopIdTo;

  /** 路線停留所from */
  private String rosenBusStopNameFrom;

  /** 路線停留所to */
  private String rosenBusStopNameTo;

  /** 三角運賃 */
  private Integer lineCharge;

  /** ICカード三角運賃 */
  private Integer icPrice;
}
