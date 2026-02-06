package maint.form.lineRoutes;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.operation.lineRoutes.LineRouteBusStopsDto;
import maint.entity.LineRouteBusStopsEntity;
import maint.entity.LineRoutesEntity;
import maint.enums.DispMode;
import maint.enums.RideAlightDivision;
import maint.form.BaseForm;
import maint.utils.ExObjectUtils;
import maint.utils.ExRegUtils;
import maint.utils.date.ExDateUtils;
import maint.validation.annonation.DateFormat;
import maint.validation.annonation.IllegalCharacters;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 系統詳細FORM
 *
 * @author y.kim
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LineRoutesDetailForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = 1111124395803498502L;

  /** 系統ID */
  private Long lineRouteId;

  /** 路線ID */
  @NotNull
  private Long rosenId;

  /** 路線名（表示用） */
  private String rosenName;

  /** 往復区分（0: 下り運行, 1: 上り運行） */
  @NotNull
  @Min(0)
  @Max(1)
  private Integer lineDetailServiceDivision;

  /** 系統コード */
  @NotBlank
  @IllegalCharacters
  @Size(min = 1, max = 10)
  private String lineRouteCode;

  /** 系統番号 */
  @IllegalCharacters
  @Size(max = 20)
  private String lineRouteShortName;

  /** 系統番号名称 */
  @IllegalCharacters
  @Size(max = 50)
  private String lineRouteNumber;

  /** 系統番号名称 英語*/
  @IllegalCharacters
  @Size(max = 100)
  private String lineRouteNumberEn;

  /** 利用開始日 */
  private String serviceStartDate;

  /** 利用終了日 */
  @DateFormat("yyyy-MM-dd")
  private String serviceEndDate;

  /** 系統名称 */
  @NotBlank
  @IllegalCharacters
  @Size(min = 1, max = 50)
  private String lineRouteName;

  /** 主な経由地 */
  @IllegalCharacters
  @Size(max = 50)
  private String mainStopoverPoint;

  /** 系統URL */
  @Size(max = 256)
  private String lineRouteUrl;

  /** 系統背景色 */
  @Pattern(
      regexp = ExRegUtils.hexColorReg,
      message = "{validation.lineRoutes.error.checkHexColor}"
  )
  private String lineRouteColor;

  /** 系統文字色 */
  @Pattern(
      regexp = ExRegUtils.hexColorReg,
      message = "{validation.lineRoutes.error.checkHexColor}"
  )
  private String lineRouteTextColor;

  /** 系統備考 */
  @IllegalCharacters
  @Size(max = 50)
  private String lineRouteRemarks;

  /** 系統停留所リスト */
  @NotEmpty
  @Valid
  private List<LineRouteBusStopsDto> lineRouteBusStopList;

  /** 利用可否区分（0: 利用不可, 1: 利用中） */
  @NotNull
  @Min(0)
  @Max(1)
  private Integer availableFlag;

  /** 画面モード */
  private DispMode dispMode;

  // 運行期間チェック
  @AssertTrue(message = "{validation.lineRoutes.error.invalidRange}")
  public boolean isValidDateRange() {
    if (ExObjectUtils.isAnyBlank(this.serviceStartDate,this.serviceEndDate)) {
      return true;
    }

    return ExDateUtils.compareStringDate(this.serviceStartDate,
        ExObjectUtils.isBlank(this.serviceEndDate) ? "9999-12-31" : this.serviceEndDate) <= 0;
  }

  // 利用開始日チェック
  @AssertTrue(message = "{validation.lineRoutes.error.validStartDate}")
  public boolean isValidServiceStartDate() {
    if (ExObjectUtils.isBlank(this.serviceStartDate) || DispMode.UPDATE_MODE.equals(this.dispMode)) {
      return true;
    }

    return ExDateUtils.compareStringDate(this.serviceStartDate, ExDateUtils.getCurrentDate()) >= 0;
  }

  /**
   * 系統 Entity取得
   *
   * @return 系統 Entity
   */
  public LineRoutesEntity getLineRoutesEntity() {
    LineRoutesEntity lineRoutesEntity = new LineRoutesEntity();
    lineRoutesEntity.setLineRouteId(this.lineRouteId);
    lineRoutesEntity.setRosenId(this.rosenId);
    lineRoutesEntity.setLineDetailServiceDivision(this.lineDetailServiceDivision);
    lineRoutesEntity.setLineRouteCode(this.lineRouteCode);
    lineRoutesEntity.setServiceStartDate(this.serviceStartDate);
    lineRoutesEntity.setServiceEndDate(this.serviceEndDate);
    lineRoutesEntity.setLineRouteName(this.lineRouteName);
    lineRoutesEntity.setMainStopoverPoint(this.mainStopoverPoint);
    lineRoutesEntity.setLineRouteUrl(this.lineRouteUrl);
    lineRoutesEntity.setLineRouteColor(this.lineRouteColor);
    lineRoutesEntity.setLineRouteTextColor(this.lineRouteTextColor);
    lineRoutesEntity.setLineRouteRemarks(this.lineRouteRemarks);
    lineRoutesEntity.setAvailableFlag(this.availableFlag);
    lineRoutesEntity.setLineRouteShortName(this.lineRouteShortName);
    lineRoutesEntity.setLineRouteNumber(this.lineRouteNumber);
    lineRoutesEntity.setLineRouteNumberEn(this.lineRouteNumberEn);
    return lineRoutesEntity;
  }

  /**
   * 系統停留所 Entity取得
   *
   * @param lineRouteId 系統ID
   * @param userId      userID
   * @return 系統停留所 Entity
   */
  public List<LineRouteBusStopsEntity> getLineRouteBusStopsEntity(Long lineRouteId, Long userId) {
    List<LineRouteBusStopsEntity> entityList = new ArrayList<>();

    for (LineRouteBusStopsDto dto : this.lineRouteBusStopList) {
      LineRouteBusStopsEntity entity = new LineRouteBusStopsEntity();
      entity.setLineRouteBusStopId(dto.getLineRouteBusStopId());
      entity.setLineRouteId(lineRouteId);
      entity.setRosenBusStopId(dto.getRosenBusStopId());
      entity.setSignpostId(dto.getSignpostId());
      // 乗降区分が「降車」の場合、「1：乗車不可」、それ以外は「0：乗車」
      entity.setRideAlightDivision(
          RideAlightDivision.ALIGHT_ONLY.getValue().equals(dto.getRideAlightDivision()) ? 1 : 0);
      // 乗降区分が「乗車」の場合、「1：降車不可」、それ以外は「0：降車」
      entity.setDropOffDivision(
          RideAlightDivision.RIDE_ONLY.getValue().equals(dto.getRideAlightDivision()) ? 1 : 0);
      entity.setShapeDistTraveled(dto.getShapeDistTraveled());
      entity.setStopSequence(dto.getSequenceNo());
      entity.setStopHeadsign(dto.getStopHeadsign());
      entity.setStopHeadsignEn(dto.getStopHeadsignEn());
      entity.setCreatedUser(String.valueOf(userId));
      entity.setUpdatedUser(String.valueOf(userId));

      entityList.add(entity);
    }

    return entityList;
  }

  /**
   * 指定されたプロパティを このインスタンスに全て上書き。
   *
   * @param lineRoutesDetailForm lineRoutesDetailForm
   */
  public void setAllPropertiesFrom(LineRoutesDetailForm lineRoutesDetailForm) {
    if (ExObjectUtils.isBlank(lineRoutesDetailForm))
      return;

    this.lineRouteId = lineRoutesDetailForm.getLineRouteId();
    this.rosenId = lineRoutesDetailForm.getRosenId();
    this.rosenName = lineRoutesDetailForm.getRosenName();
    this.lineDetailServiceDivision = lineRoutesDetailForm.getLineDetailServiceDivision();
    this.lineRouteCode = lineRoutesDetailForm.getLineRouteCode();
    this.serviceStartDate = lineRoutesDetailForm.getServiceStartDate();
    this.serviceEndDate = lineRoutesDetailForm.getServiceEndDate();
    this.lineRouteName = lineRoutesDetailForm.getLineRouteName();
    this.lineRouteShortName = lineRoutesDetailForm.getLineRouteShortName();
    this.lineRouteNumber = lineRoutesDetailForm.getLineRouteNumber();
    this.lineRouteNumberEn = lineRoutesDetailForm.getLineRouteNumberEn();
    this.mainStopoverPoint = lineRoutesDetailForm.getMainStopoverPoint();
    this.lineRouteUrl = lineRoutesDetailForm.getLineRouteUrl();
    this.lineRouteColor = lineRoutesDetailForm.getLineRouteColor();
    this.lineRouteTextColor = lineRoutesDetailForm.getLineRouteTextColor();
    this.lineRouteRemarks = lineRoutesDetailForm.getLineRouteRemarks();
    this.availableFlag = lineRoutesDetailForm.getAvailableFlag();
    this.dispMode = DispMode.UPDATE_MODE;
    // 条件に応じて乗車区分を設定
    this.lineRouteBusStopList =
        lineRoutesDetailForm.getLineRouteBusStopList().stream().peek(dto -> {
          Integer original = dto.getOriginalRideAlightDivision(); // 路線停留所の乗降区分
          Integer ride = dto.getLineRouteBusStopsRideAlightDivision(); // 系統停留所の乗車区分
          Integer drop = dto.getDropOffDivision(); // 系統停留所の降車区分
          // 乗車・降車両方ではない場合、上がりの場合 そのまま、下りの場合, original 0 → 1, 1 → 0 反転
          if (ExObjectUtils.isNotBlank(original) && (original != 2) && lineRoutesDetailForm.getLineDetailServiceDivision() == 0) {
            original = (original == 0) ? 1 : 0;
          }
          dto.setOriginalRideAlightDivision(original);
          // 路線停留所の乗降区分 が 2（乗車・降車両方）の場合、ride / drop の組み合わせで分岐
          if (ExObjectUtils.isNotBlank(original) && original == 2) {
            // 乗車区分: 1（乗車不可）、降車区分: 0（降車可）
            if (ride == 1 && drop == 0) {
              dto.setRideAlightDivision(1); // 降車のみ
            }
            // 乗車区分: 0（乗車可）、降車区分: 1（降車不可）
            else if (ride == 0 && drop == 1) {
              dto.setRideAlightDivision(0); // 乗車のみ
            }
            // その他の組み合わせ → 両方可
            else {
              dto.setRideAlightDivision(2); // 乗降車両方可
            }
          }
          // 路線停留所の乗降区分 が 0 または 1 の場合 そのまま設定
          else if (original != 2) {
            dto.setRideAlightDivision(original);
          }
        }).sorted(Comparator.comparing(LineRouteBusStopsDto::getSequenceNo)).toList();
  }
}
