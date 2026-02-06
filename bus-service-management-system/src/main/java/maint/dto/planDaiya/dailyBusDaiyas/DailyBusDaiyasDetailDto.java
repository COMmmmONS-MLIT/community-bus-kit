package maint.dto.planDaiya.dailyBusDaiyas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.BaseDto;

/**
 * 日毎便ダイヤ情報DTO
 *
 * @author jeon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DailyBusDaiyasDetailDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 225635973801224214L;

  /** 日毎便ダイヤID */
  private Long dailyBusDaiyaId;
  /** 路線名称 */
  private String rosenName;
  /** 復路区分 */
  private Integer lineDetailServiceDivision;
  /** 系統名称 */
  private String lineRouteName;
  /** 運行年月日 */
  private String serviceDate;
  /** 運行年月日の00:00 */
  private Long serviceStartTime;
  /** 運行年月日の23:59 */
  private Long serviceEndTime;
  /** 運休フラグ */
  private Integer salesCancelFlg;
  /** 始発停留所時刻 */
  private String busStopDepartureTime;
  /** 運行会社 */
  private String operatingCompanyName;
  /** 運行対象外フラグ */
  private Integer operationCancelFlg;
  /** 運行順序 */
  private Long stopSequence;
  /** 標柱名称 */
  private String signpostName;
  /** 乗降区分 */
  private Long rideAlightDivision;
  /** 下車区分 */
  private Long dropOffDivision;
  /** 車両ID */
  private Long vehicleId;
  /** 車両名 */
  private String vehicleName;
  /** お知らせ */
  private String notification;
  /** 便番号 */
  private String busServiceTripId;
}
