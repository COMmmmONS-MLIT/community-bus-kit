package maint.form.lines;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.operation.lineRoutes.LineRouteBusStopsDto;
import maint.dto.operation.lines.LineBusStopsDto;
import maint.entity.LineBusStopsEntity;
import maint.entity.LinesEntity;
import maint.enums.DispMode;
import maint.form.BaseForm;

import maint.utils.ExObjectUtils;
import maint.utils.date.ExDateUtils;
import maint.validation.annonation.DateFormat;
import maint.validation.annonation.IllegalCharacters;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 路線詳細FORM
 *
 * @author y.kim
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LinesDetailForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = 1111124395803498502L;

  /** 路線ID */
  private Long rosenId;

  /** 路線停留所ID */
  private Long rosenBusStopId;

  /** 路線コード */
  @NotBlank
  @IllegalCharacters
  @Size(min = 1, max = 10)
  private String rosenCode;

  /** 利用開始日 */
  @NotBlank
  @DateFormat("yyyy-MM-dd")
  private String serviceStartDate;

  /** 利用終了日 */
  @DateFormat("yyyy-MM-dd")
  private String serviceEndDate;

  /** 路線名称 */
  @NotBlank
  @IllegalCharacters
  @Size(min = 1, max = 50)
  private String rosenName;

  /** 路線名称 かな */
  @NotBlank
  @IllegalCharacters
  @Size(min = 1, max = 50)
  private String rosenKanaName;

  /** 路線名称 英語 */
  @IllegalCharacters
  @Size(max = 100)
  private String rosenEnName;

  /** 利用可否区分（0: 利用不可, 1: 利用中） */
  @NotNull
  @Min(0)
  @Max(1)
  private Integer availableFlag;

  /** 路線停留所リスト */
  @NotEmpty
  private List<LineBusStopsDto> lineBusStopsList;

  /** 画面モード */
  private DispMode dispMode;

  // 利用終了日チェック
  @AssertTrue(message = "{validation.lines.error.validEndDate}")
  public boolean isValidServiceEndDate() {
    if (ExObjectUtils.isBlank(this.serviceEndDate)) {
      return true;
    }

    return ExDateUtils.compareStringDate(this.serviceEndDate, ExDateUtils.getCurrentDate()) >= 0;
  }

  /**
   * 路線 Entity取得
   *
   * @return 路線 Entity
   */
  public LinesEntity getLinesEntity() {
    LinesEntity linesEntity = new LinesEntity();
    linesEntity.setRosenId(this.rosenId);
    linesEntity.setRosenCode(this.rosenCode);
    linesEntity.setServiceStartDate(this.serviceStartDate);
    linesEntity.setServiceEndDate(this.serviceEndDate);
    linesEntity.setRosenName(this.rosenName);
    linesEntity.setRosenKanaName(this.rosenKanaName);
    linesEntity.setRosenEnName(this.rosenEnName);
    linesEntity.setAvailableFlag(this.availableFlag);
    return linesEntity;
  }

  /**
   * 路線停留所 Entity取得
   *
   *
   * @param rosenId 路線ID
   * @param userId userID
   * @return 路線停留所 Entity
   */
  public List<LineBusStopsEntity> getLineBusStopsEntity(Long rosenId, Long userId) {
    List<LineBusStopsEntity> entityList = new ArrayList<>();

    for (LineBusStopsDto dto : lineBusStopsList) {
      LineBusStopsEntity entity = new LineBusStopsEntity();

      entity.setRosenBusStopId(dto.getRosenBusStopId());
      entity.setRosenId(rosenId);
      entity.setBusStopId(dto.getBusStopId());
      entity.setRideAlightDivision(dto.getRideAlightDivision());
      entity.setSequenceNo(dto.getSequenceNo());
      entity.setCreatedUser(String.valueOf(userId));
      entity.setUpdatedUser(String.valueOf(userId));

      entityList.add(entity);
    }

    return entityList;
  }

  /**
   * 指定されたプロパティを このインスタンスに全て上書き。
   *
   * @param linesDetailForm linesDetailForm
   */
  public void setAllPropertiesFrom(LinesDetailForm linesDetailForm) {
    if (ExObjectUtils.isBlank(linesDetailForm)) return;

    this.rosenId = linesDetailForm.getRosenId();
    this.rosenCode = linesDetailForm.getRosenCode();
    this.serviceStartDate = linesDetailForm.getServiceStartDate();
    this.serviceEndDate = linesDetailForm.getServiceEndDate();
    this.rosenName = linesDetailForm.getRosenName();
    this.rosenKanaName = linesDetailForm.getRosenKanaName();
    this.rosenEnName = linesDetailForm.getRosenEnName();
    this.availableFlag = linesDetailForm.getAvailableFlag();
    List<LineBusStopsDto> sortedList = linesDetailForm.getLineBusStopsList() != null
        ? linesDetailForm.getLineBusStopsList().stream()
        .sorted(Comparator.comparing(LineBusStopsDto::getSequenceNo))
        .toList()
        : new ArrayList<>();

    this.lineBusStopsList = sortedList;
  }
}
