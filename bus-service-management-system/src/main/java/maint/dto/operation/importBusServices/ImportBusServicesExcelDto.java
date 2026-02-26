package maint.dto.operation.importBusServices;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;
import maint.utils.ExRegUtils;

/**
 * 便ダイヤ登録 EXCEL Dto すべて文字列で保持し、バリデーションはアノテーションで実施する。
 *
 * @author JEKAL.H
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ImportBusServicesExcelDto extends BaseDto {

  private static final long serialVersionUID = -8347533418575928411L;

  /** 事業者ID（1～11桁の数値） */
  @NotBlank(message = "{validation.busdaiyas.register.excel.businesscorpid.notblank}")
  @Pattern(regexp = ExRegUtils.numeric1to11Reg, message = "{validation.busdaiyas.register.excel.businesscorpid.pattern}")
  private String businessCorpId;

  /** ダイヤ改正ID（1～11桁の数値） */
  @NotBlank(message = "{validation.busdaiyas.register.excel.daiyarevisionid.notblank}")
  @Pattern(regexp = ExRegUtils.numeric1to11Reg, message = "{validation.busdaiyas.register.excel.daiyarevisionid.pattern}")
  private String daiyaRevisionId;

  /** 路線名 */
  private String rosenName;

  /** 往復区分 */
  private String lineDetailServiceDivision;

  /** 運行期間_開始（YYYY/MM/DD） */
  private String serviceStartDate;

  /** 運行期間_終了（YYYY/MM/DD） */
  private String serviceEndDate;

  /** ダイヤ改正名称 */
  private String daiyaRevisionName;

  /** 便番号（必須/1～50文字） */
  @NotBlank(message = "{validation.busdaiyas.register.excel.busservicetripid.notblank}")
  @Size(min = 1, max = 50, message = "{validation.busdaiyas.register.excel.busservicetripid.size}")
  private String busServiceTripId;

  /** 便行先（表示用 headsign/最大50文字） */
  @Size(max = 50, message = "{validation.busdaiyas.register.excel.busservicetripheadsign.size}")
  private String busServiceTripHeadsign;

  /** 便名称（最大50文字） */
  @Size(max = 50, message = "{validation.busdaiyas.register.excel.busservicename.size}")
  private String busServiceName;

  /** 運行カレンダーID（1～11桁の数値） */
  @NotBlank(message = "{validation.busdaiyas.register.excel.calendarid.notblank}")
  @Pattern(regexp = ExRegUtils.numeric1to11Reg, message = "{validation.busdaiyas.register.excel.calendarid.pattern}")
  private String calendarId;

  /** 車いす利用区分（0～2） */
  @Pattern(regexp = ExRegUtils.zeroToTwoOptionalReg, message = "{validation.busdaiyas.register.excel.wheelchairaccessibletype.pattern}")
  private String wheelchairAccessibleType;

  /** 自転車持ち込み区分（0～2） */
  @Pattern(regexp = ExRegUtils.zeroToTwoOptionalReg, message = "{validation.busdaiyas.register.excel.bikesallowedtype.pattern}")
  private String bikesAllowedType;

  /** 便情報（最大50文字） */
  @Size(max = 50, message = "{validation.busdaiyas.register.excel.busservicetripdesc.size}")
  private String busServiceTripDesc;

  /** 便記号（最大50文字） */
  @Size(max = 50, message = "{validation.busdaiyas.register.excel.busservicetripdescsymbol.size}")
  private String busServiceTripDescSymbol;

  /** 営業所ID（1～11桁の数値/任意） */
  @Pattern(regexp = ExRegUtils.optionalNumeric1to11Reg, message = "{validation.busdaiyas.register.excel.officeId.pattern}")
  private String officeId;

  /** 仕業番号（最大20文字） */
  @Size(max = 20, message = "{validation.busdaiyas.register.excel.worknumber.size}")
  private String workNumber;

  /** 便結合区分（最大20文字） */
  @Size(max = 20, message = "{validation.busdaiyas.register.excel.blockid.size}")
  private String blockId;

  /** 運行順序（数値文字列を想定） */
  private String stopSequence;

  /** 系統停留所ID（1～11桁の数値） */
  @NotBlank(message = "{validation.busdaiyas.register.excel.lineroutebusstopid.notblank}")
  @Pattern(regexp = ExRegUtils.numeric1to11Reg, message = "{validation.busdaiyas.register.excel.lineroutebusstopid.pattern}")
  private String lineRouteBusStopId;

  /** 系統停留所名 */
  private String signpostName;

  /** 到着時刻（HHMM/4桁） */
  @NotBlank(message = "{validation.busdaiyas.register.excel.busstoparrivaltime.notblank}")
  @Pattern(regexp = ExRegUtils.timeHHmmReg, message = "{validation.busdaiyas.register.excel.busstoparrivaltime.pattern}")
  private String busStopArrivalTime;

  /** 出発時刻（HHMM/4桁） */
  @NotBlank(message = "{validation.busdaiyas.register.excel.busstopdeparturetime.notblank}")
  @Pattern(regexp = ExRegUtils.timeHHmmReg, message = "{validation.busdaiyas.register.excel.busstopdeparturetime.pattern}")
  private String busStopDepartureTime;
}

