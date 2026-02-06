package maint.dto.operation.lineChargesInfoReport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

import java.util.ArrayList;

/**
 * 運賃情報インポート登録DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LineChargesInfoReportInsertDto extends BaseDto {

  private static final long serialVersionUID = -5326657281948471768L;

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

  /** 運賃明細 */
  private ArrayList<LineChargeDetailsDto> details;

  /**
   * 運賃明細を取得
   * @return 運賃明細
   */
  public ArrayList<LineChargeDetailsDto> getDetails() {
    return details == null ? new ArrayList<>() : details;
  }

  /**
   * 運賃明細を追加
   * @param detailsDto 運賃明細DTO
   */
  public void addDetails(LineChargeDetailsDto detailsDto) {
    if (this.details == null) {
      this.details = new ArrayList<>();
    }
    this.details.add(detailsDto);
  }
}

