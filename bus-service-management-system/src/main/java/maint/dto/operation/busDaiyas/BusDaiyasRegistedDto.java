package maint.dto.operation.busDaiyas;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;
import maint.enums.BikesAllowedTypeDivision;
import maint.enums.WheelchairAccessibleDivision;
import maint.utils.ExRegUtils;
import maint.validation.annonation.IllegalCharacters;
import maint.validation.annonation.ValidEnumValue;

import java.util.List;

/**
 * 登録済み便ダイヤ情報取得用DTO
 *
 * @author Yuta Ishizaka
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusDaiyasRegistedDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 1111111111111111111L;

  /** 事業者ID */
  private Long businessCorpId;
  /** 路線ID */
  private Long rosenId;
  /** ダイヤ改正ID */
  private Long daiyaRevisionId;
  /** 路線名 */
  private String rosenName;
  /** 往復区分 */
  private Integer lineDetailServiceDivision;
  /** 運行期間_開始 */
  private String serviceStartDate;
  /** 運行期間_終了 */
  private String serviceEndDate;
  /** ダイヤ改正理由 */
  private String daiyaRevisionName;

  /** 系統ID */
  @NotBlank(message = "{validation.busdaiyas.error.linerouteid.notblank}")
  private String lineRouteId;
  /** 系統名 */
  private String lineRouteName;
  /** 運行カレンダーID */
  @NotBlank(message = "{validation.busdaiyas.error.calendarid.notblank}")
  private String calendarId;
  /** 運行カレンダー名 */
  private String calendarName;
  /** 便ID */
  private Long busServiceId;
  /** 便番号 */
  @IllegalCharacters(message = "{validation.busdaiyas.error.busservicetripid.IllegalCharacters}")
  @NotBlank(message = "{validation.busdaiyas.error.busservicetripid.notblank}")
  @Size(min = 1, max = 50, message = "{validation.busdaiyas.error.busservicetripid.size}")
  private String busServiceTripId;
  /** 便行先 */
  @IllegalCharacters(message = "{validation.busdaiyas.error.busservicetripheadsign.IllegalCharacters}")
  @Size(max = 50, message = "{validation.busdaiyas.error.busservicetripheadsign.size}")
  private String busServiceTripHeadsign;
  /** 便名称 */
  @IllegalCharacters(message = "{validation.busdaiyas.error.busservicename.IllegalCharacters}")
  @Size(max = 50, message = "{validation.busdaiyas.error.busservicename.size}")
  private String busServiceName;
  /** 車いす利用区分 */
//  @Pattern(regexp = ExRegUtils.zeroToTwoOptionalReg)
  @ValidEnumValue(enumClass = WheelchairAccessibleDivision.class)
  private Integer wheelchairAccessibleType;
  /** 自転車持ち込み区分 */
//  @Pattern(regexp = ExRegUtils.zeroToTwoOptionalReg)
  @ValidEnumValue(enumClass = BikesAllowedTypeDivision.class)
  private Integer bikesAllowedType;
  /** 便情報 */
  @IllegalCharacters(message = "{validation.busdaiyas.error.busservicetripdesc.IllegalCharacters}")
  @Size(max = 50, message = "{validation.busdaiyas.error.busservicetripdesc.size}")
  private String busServiceTripDesc;
  /** 便記号 */
  @IllegalCharacters(message = "{validation.busdaiyas.error.busservicetripdescsymbol.IllegalCharacters}")
  @Size(max = 50, message = "{validation.busdaiyas.error.busservicetripdescsymbol.size}")
  private String busServiceTripDescSymbol;
  /** 営業所ID */
  private String officeId;
  /** 仕業番号 */
  @IllegalCharacters(message = "{validation.busdaiyas.error.worknumber.IllegalCharacters}")
  @Size(max = 20, message = "{validation.busdaiyas.error.worknumber.size}")
  private String workNumber;
  /** 便結合区分 */
  @IllegalCharacters(message = "{validation.busdaiyas.error.blockid.IllegalCharacters}")
  @Size(max = 20, message = "{validation.busdaiyas.error.blockid.size}")
  private String blockId;
  /** 車両ID */
  private String vehicleId;
  /** 車両名 */
  private String vehicleName;
  /** 運行順序 */
  private Integer stopSequence;
  /** 系統停留所ID */
  private Long lineRouteBusStopId;
  /** 系統停留所名 */
  private String signpostName;
  /** 便ダイヤ明細ID */
  private Long busDaiyaDetailId;
  /** 到着時刻 */
  private String busStopArrivalTime;
  /** 出発時刻 */
  private String busStopDepartureTime;
  /** 便ダイヤ明細ID */
  private String busDaiyaId;
  /** 路線停留所ID */
  private Long rosenBusStopId;
  /** 便ダイヤ明細情報rスト */
  @NotNull(message = "{validation.busdaiyas.error.busadaiyasDetail.null}")
  private List<BusDaiyasRegistedDetailDto> detailList;
  /** 便ダイヤ画面表示列番号 */
  private Integer busDaiyaDispColNumber;
}

