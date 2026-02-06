package maint.dto.operation.busDaiyas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 便ダイヤ登録ダウンロード用DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BusDaiyasRegisterDownLoadDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 2572143723305543357L;

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
  private Long lineRouteId;
  /** 系統名 */
  private String lineRouteName;
  /** 運行カレンダーID */
  private Long calendarId;
  /** 運行カレンダー名 */
  private String calendarName;
  /** 便ID - 便.便ID */
  private Long busServiceId;
  /** 便番号 - 便.便番号 */
  private String busServiceTripId;
  /** 便行先 - 便.便行先 */
  private String busServiceTripHeadsign;
  /** 便名称 - 便.便名称 */
  private String busServiceName;
  /** 車いす利用区分 */
  private Integer wheelchairAccessibleType;
  /** 自転車持ち込み区分 */
  private Integer bikesAllowedType;
  /** 便情報  */
  private String busServiceTripDesc;
  /** 便記号  */
  private String busServiceTripDescSymbol;
  /** 営業所ID */
  private Long officeId;
  /** 仕業番号 */
  private String workNumber;
  /** 便結合区分 */
  private String blockId;
  /** 車両ID */
  private Long vehicleId;
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

}
