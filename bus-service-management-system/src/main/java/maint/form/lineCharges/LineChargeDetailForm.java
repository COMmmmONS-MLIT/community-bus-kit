package maint.form.lineCharges;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.operation.lineCharges.LineChargeDetailDto;
import maint.enums.DispMode;
import maint.enums.LineChargeDivision;
import maint.enums.PaymentMethod;
import maint.enums.UniformTranceferType;
import maint.form.BaseForm;
import maint.utils.ExRegUtils;
import maint.validation.annonation.ValidEnumValue;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LineChargeDetailForm extends BaseForm {

  @Serial
  private static final long serialVersionUID = 94712029742L;

  /** PKID */
  private Long lineChargeId;

  /** 画面モード */
  private DispMode dispMode;

  /** 登録フラグ */
  private Integer insertFlag;

  /** 路線名 */
  private String rosenName;

  /** 運賃改定ID */
  private Long chargeRevisionId;

  /** 適用開始日 */
  private String serviceStartDate;

  /** 適用終了日 */
  private String serviceEndDate;

  /** 運賃改定理由 */
  private String revisionReason;

  /** 支払タイミング（ 0=乗車後, 1=乗車前 ） */
  @NotNull(groups = CommonGroup.class)
  @ValidEnumValue(enumClass = PaymentMethod.class)
  private Integer paymentMethod;

  /** 運賃区分（ 1=均一, 2=三角 ） */
  @NotNull(groups = CommonGroup.class)
  @ValidEnumValue(enumClass = LineChargeDivision.class)
  private Integer lineChargeDivision;

  /** 均一運賃 */
  @NotBlank(groups = UniformGroup.class)
  @Pattern(regexp = ExRegUtils.optionalNumeric1to11Reg , groups = UniformGroup.class,
      message = "{validation.onlyNumber.invalid}")
  @Size(max = 11, groups = UniformGroup.class, message = "{validation.lineCharge.error.max.11}")
  private String uniformCharge;

  /** ICカード均一運賃 */
  @Pattern(regexp = ExRegUtils.optionalNumeric1to11Reg, groups = UniformGroup.class,
      message = "{validation.onlyNumber.invalid}")
  @Size(max = 11, groups = UniformGroup.class, message = "{validation.lineCharge.error.max.11}")
  private String uniformIcPrice;

  /** 均一運賃乗換可否（ 0=乗換不可 / 1=1度可能 / 2=2度可能 / 9=乗換制限なし可能 ） */
  @NotNull(groups = UniformGroup.class)
  @ValidEnumValue(enumClass = UniformTranceferType.class)
  private Integer uniformTransferType;

  /** 三角運賃リスト */
  @Valid
  private List<LineChargeTriangle> triangle;


  @Data
  @NotNull(groups = TriangleGroup.class)
  public static class LineChargeTriangle implements Serializable {

    /** Serial Version UId */
    private static final long serialVersionUID = -4950024777066409254L;

    /** 運賃詳細ID */
    private Long lineChargeDetailId;

    /** 路線停留所FORM */
    private Long rosenBusStopIdFrom;

    /** 路線停留所TO */
    private Long rosenBusStopIdTo;

    /** 路線停留所FORM */
    private String rosenBusStopNameFrom;

    /** 路線停留所TO */
    private String rosenBusStopNameTo;

    /** 三角運賃 */
    @NotBlank(groups = TriangleGroup.class)
    @Pattern(regexp = ExRegUtils.optionalNumeric1to11Reg, groups = TriangleGroup.class,
        message = "{validation.onlyNumber.invalid}")
    @Size(max = 11, groups = TriangleGroup.class, message = "{validation.lineCharge.error.max.11}")
    private String lineCharge;

    /** ICカード三角運賃 */
    @Pattern(regexp = ExRegUtils.optionalNumeric1to11Reg, groups = TriangleGroup.class,
        message = "{validation.onlyNumber.invalid}")
    @Size(max = 11, groups = TriangleGroup.class, message = "{validation.lineCharge.error.max.11}")
    private String icPrice;
  }


  /** 運賃区分別バリデーショングループ */
  public interface CommonGroup {
  }     // 均一・三角共通
  public interface UniformGroup {
  }    // 均一のときだけ
  public interface TriangleGroup {
  }   // 三角のときだけ

  /**
   * コンストラクタ
   *
   * @param dto 運賃詳細DTO
   * @param page ページ
   */
  public void setInfo(List<LineChargeDetailDto> dto, Long page) {
    this.page = page;
    if (dto == null || dto.isEmpty())
      return;

    LineChargeDetailDto firstDto = dto.get(0);

    this.rosenName = firstDto.getRosenName();
    this.lineChargeId = firstDto.getLineChargeId();
    this.chargeRevisionId = firstDto.getChargeRevisionId();
    this.serviceStartDate = firstDto.getServiceStartDate();
    this.serviceEndDate = firstDto.getServiceEndDate();
    this.revisionReason = firstDto.getRevisionReason();
    this.paymentMethod = firstDto.getPaymentMethod();
    this.lineChargeDivision = firstDto.getLineChargeDivision();
    this.uniformCharge =
        firstDto.getUniformCharge() == null ? "" : firstDto.getUniformCharge().toString();
    this.uniformIcPrice =
        firstDto.getUniformIcPrice() == null ? "" : firstDto.getUniformIcPrice().toString();
    this.uniformTransferType = firstDto.getUniformTransferType();

    // ① DTO -> Form内LineChargeTriangle へ詰め替え
    List<LineChargeTriangle> raw = dto.stream().map(d -> {
      LineChargeTriangle r = new LineChargeTriangle();
      r.setLineChargeDetailId(d.getLineChargeDetailId());
      r.setRosenBusStopIdFrom(d.getRosenBusStopIdFrom());
      r.setRosenBusStopIdTo(d.getRosenBusStopIdTo());
      r.setRosenBusStopNameFrom(d.getRosenBusStopNameFrom());
      r.setRosenBusStopNameTo(d.getRosenBusStopNameTo());
      r.setLineCharge(d.getLineCharge() == null ? "" : d.getLineCharge().toString());
      r.setIcPrice(d.getIcPrice() == null ? "" : d.getIcPrice().toString());
      return r;
    }).toList();

    // ② stopId一覧（順序保持、重複排除） + id->name
    Map<Long, String> idToName = new LinkedHashMap<>();
    for (LineChargeTriangle t : raw) {
      if (t.getRosenBusStopIdFrom() != null && t.getRosenBusStopNameFrom() != null) {
        idToName.putIfAbsent(t.getRosenBusStopIdFrom(), t.getRosenBusStopNameFrom());
      }
      if (t.getRosenBusStopIdTo() != null && t.getRosenBusStopNameTo() != null) {
        idToName.putIfAbsent(t.getRosenBusStopIdTo(), t.getRosenBusStopNameTo());
      }
    }
    List<Long> stopIds = new ArrayList<>(idToName.keySet());

    // ③ (fromId,toId) -> map（更新用に既存セルを引く）
    Map<String, LineChargeTriangle> map = raw.stream()
        .filter(t -> t.getRosenBusStopIdFrom() != null && t.getRosenBusStopIdTo() != null).collect(
            java.util.stream.Collectors.toMap(
                t -> t.getRosenBusStopIdFrom() + "->" + t.getRosenBusStopIdTo(),
                java.util.function.Function.identity(), (a, b) -> a));

    // ④ 上三角正規化（IDで回す）
    int n = stopIds.size();
    int need = n * (n - 1) / 2;

    List<LineChargeTriangle> normalized = new ArrayList<>(need);

    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {

        Long fromId = stopIds.get(i);
        Long toId = stopIds.get(j);

        LineChargeTriangle cell = map.get(fromId + "->" + toId);
        if (cell == null)
          cell = new LineChargeTriangle();

        cell.setRosenBusStopIdFrom(fromId);
        cell.setRosenBusStopIdTo(toId);

        // 名前は表示/hidden用（idToNameから埋める）
        cell.setRosenBusStopNameFrom(idToName.get(fromId));
        cell.setRosenBusStopNameTo(idToName.get(toId));

        if (cell.getLineCharge() == null)
          cell.setLineCharge("");
        if (cell.getIcPrice() == null)
          cell.setIcPrice("");

        normalized.add(cell);
      }
    }

    this.triangle = normalized;
  }
}
