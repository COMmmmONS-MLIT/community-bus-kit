package maint.dto.operation.importBusServices;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

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
  @NotBlank(message = "{validation.agency.error.agencyId}")
  @Pattern(regexp = "^\\d{1,11}$", message = "{validation.agency.error.agencyIdDigit}")
  private String businessCorpId;

  /** ダイヤ改正ID（1～11桁の数値） */
  @NotBlank(message = "{validation.daiyaRevisions.error.daiyaRevisionId}")
  @Pattern(regexp = "^\\d{1,11}$", message = "{validation.daiyaRevisions.error.daiyaRevisionIdDigit}")
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
  @NotBlank(message = "{validation.busServices.error.busServiceTripId}")
  @Size(min = 1, max = 50, message = "{validation.busServices.error.busServiceTripIdDigit}")
  private String busServiceTripId;

  /** 便行先（表示用 headsign/最大50文字） */
  @Size(max = 50, message = "{validation.busServices.error.busServiceTripHeadsign}")
  private String busServiceTripHeadsign;

  /** 便名称（最大50文字） */
  @Size(max = 50, message = "{validation.busServices.error.busServiceName}")
  private String busServiceName;

  /** 運行カレンダーID（1～11桁の数値） */
  @NotBlank(message = "{validation.busServices.error.calendarId}")
  @Pattern(regexp = "^\\d{1,11}$", message = "{validation.busServices.error.calendarIdDigit}")
  private String calendarId;

  /** 車いす利用区分（0～2） */
  @Pattern(regexp = "^[0-2]?$", message = "{validation.busServices.error.wheelchairAccessibleTypeDigit}")
  private String wheelchairAccessibleType;

  /** 自転車持ち込み区分（0～2） */
  @Pattern(regexp = "^[0-2]?$", message = "{validation.busServices.error.bikesAllowedTypeDigit}")
  private String bikesAllowedType;

  /** 便情報（最大50文字） */
  @Size(max = 50, message = "{validation.busServices.error.busServiceTripDescDigit}")
  private String busServiceTripDesc;

  /** 便記号（最大50文字） */
  @Size(max = 50, message = "{validation.busServices.error.busServiceTripDescSymbolDigit}")
  private String busServiceTripDescSymbol;

  /** 営業所ID（1～11桁の数値/任意） */
  @Pattern(regexp = "^(\\d{1,11})?$", message = "{validation.busServices.error.officeIdDigit}")
  private String officeId;

  /** 仕業番号（最大20文字） */
  @Size(max = 20, message = "{validation.busServices.error.workNumberDigit}")
  private String workNumber;

  /** 便結合区分（最大20文字） */
  @Size(max = 20, message = "{validation.busServices.error.blockIdDigit}")
  private String blockId;

  /** 運行順序（数値文字列を想定） */
  private String stopSequence;

  /** 系統停留所ID（1～11桁の数値） */
  @NotBlank(message = "{validation.lineRouteBusStops.error.lineRouteBusStopId}")
  @Pattern(regexp = "^\\d{1,11}$", message = "{validation.lineRouteBusStops.error.lineRouteBusStopIdDigit}")
  private String lineRouteBusStopId;

  /** 系統停留所名 */
  private String signpostName;

  /** 到着時刻（HHMM/4桁） */
  @NotBlank(message = "{validation.common.error.busStopArrivalTime}")
  @Size(min = 4, max = 4, message = "{validation.common.error.busStopArrivalTimeSize}")
  @Pattern(regexp = "^([01]\\d|2[0-3])[0-5]\\d$", message = "{validation.common.error.busStopArrivalTimeDigit}")
  private String busStopArrivalTime;

  /** 出発時刻（HHMM/4桁） */
  @NotBlank(message = "{validation.common.error.busStopDepartureTime}")
  @Size(min = 4, max = 4, message = "{validation.common.error.busStopDepartureTimeSize}")
  @Pattern(regexp = "^([01]\\d|2[0-3])[0-5]\\d$", message = "{validation.common.error.busStopDepartureTimeDigit}")
  private String busStopDepartureTime;


}

