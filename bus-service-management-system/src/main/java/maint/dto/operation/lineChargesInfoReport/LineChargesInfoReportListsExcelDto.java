package maint.dto.operation.lineChargesInfoReport;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;
import maint.utils.ExRegUtils;

/**
 * 便ダイヤ登録 EXCEL Dto すべて文字列で保持し、バリデーションはアノテーションで実施する。
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LineChargesInfoReportListsExcelDto extends BaseDto {

  private static final long serialVersionUID = 6611739612557282780L;

  /** 事業者ID（1～11桁の数値） */
  @NotBlank(message = "{validation.linefares.report.businesscorpid.notblank}")
  @Pattern(regexp = ExRegUtils.numeric1to11Reg, message = "{validation.linefares.report.businesscorpid.pattern}")
  private String businessCorpId;

  /** 運賃改定ID（1～11桁の数値） */
  @NotBlank(message = "{validation.linefares.report.chargerevisionid.notblank}")
  @Pattern(regexp = ExRegUtils.numeric1to11Reg, message = "{validation.linefares.report.chargerevisionid.pattern}")
  private String chargeRevisionId;

  /** 運賃区分（1桁、1:均一運賃、2:三角運賃） */
  @NotBlank(message = "{validation.linefares.report.linechargedivision.notblank}")
  @Pattern(regexp = "^[12]$", message = "{validation.linefares.report.linechargedivision.pattern}")
  private String lineChargeDivision;

  /** 均一運賃額（円） ※均一運賃のみ */
  @NotBlank(message = "{validation.linefares.report.uniformcharge.notblank}", groups = FlatFareGroup.class)
  @Pattern(regexp = ExRegUtils.numeric1to11Reg, message = "{validation.linefares.report.uniformcharge.pattern}", groups = FlatFareGroup.class)
  private String uniformCharge;

  /** ICカード運賃額（円） ※均一運賃のみ */
  @Pattern(regexp = ExRegUtils.optionalNumeric1to11Reg, message = "{validation.linefares.report.uniformicprice.pattern}", groups = FlatFareGroup.class)
  private String uniformIcPrice;

  /** 均一運賃乗換可否（0:不可、1:1度、2:2度、9:制限なし） ※均一運賃のみ */
  @NotBlank(message = "{validation.linefares.report.uniformtransfertype.notblank}", groups = FlatFareGroup.class)
  @Pattern(regexp = "^[0129]$", message = "{validation.linefares.report.uniformtransfertype.pattern}", groups = FlatFareGroup.class)
  private String uniformTransferType;

  /** 支払いタイミング（0:乗車後、1:乗車前） */
  @NotBlank(message = "{validation.linefares.report.paymentmethod.notblank}")
  @Pattern(regexp = ExRegUtils.zeroToOneOptionalReg, message = "{validation.linefares.report.paymentmethod.pattern}")
  private String paymentMethod;

  /** 路線停留所IDFrom（1～11桁） ※三角運賃のみ */
  @NotBlank(message = "{validation.linefares.report.rosenbusstopidfrom.notblank}", groups = TriangleFareGroup.class)
  @Pattern(regexp = ExRegUtils.numeric1to11Reg, message = "{validation.linefares.report.rosenbusstopidfrom.pattern}", groups = TriangleFareGroup.class)
  private String rosenBusStopIdFrom;

  /** 路線停留所IDTo（1～11桁） ※三角運賃のみ */
  @NotBlank(message = "{validation.linefares.report.rosenbusstopidto.notblank}", groups = TriangleFareGroup.class)
  @Pattern(regexp = ExRegUtils.numeric1to11Reg, message = "{validation.linefares.report.rosenbusstopidto.pattern}", groups = TriangleFareGroup.class)
  private String rosenBusStopIdTo;

  /** 運賃（円） ※三角運賃のみ */
  @NotBlank(message = "{validation.linefares.report.linecharge.notblank}", groups = TriangleFareGroup.class)
  @Pattern(regexp = ExRegUtils.numeric1to11Reg, message = "{validation.linefares.report.linecharge.pattern}", groups = TriangleFareGroup.class)
  private String lineCharge;

  /** ICカード運賃（円） ※三角運賃のみ */
  @Pattern(regexp = ExRegUtils.optionalNumeric1to11Reg, message = "{validation.linefares.report.icprice.pattern}", groups = TriangleFareGroup.class)
  private String icPrice;

  public interface FlatFareGroup {}
  public interface TriangleFareGroup {}

}

