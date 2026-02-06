package maint.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import maint.dto.operation.busDaiyas.BusDaiyasRegistedDto;

/**
 * 便 Entity
 *
 * @author g.ko
 */

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusServiceEntity extends BaseEntity {
  /** Serial version UID */
  private static final long serialVersionUID = -264354362354767L;

  /** 便ID */
  private Long busServiceId;

  /** 便ダイヤ改正ID */
  private Long daiyaRevisionId;

  /** 便番号 */
  private String busServiceTripId;

  /** 便行先 */
  private String busServiceTripHeadsign;

  /** 便名称 */
  private String busServiceName;

  /** 便名称　かな */
  private String busServiceKanaName;

  /** 便名称　英語 */
  private String busServiceEnName;

  /** 運行カレンダーID */
  private Long calendarId;

  /** 経路形状ID */
  private String shapeId;

  /** 車いす利用区分 */
  private Integer wheelchairAccessibleType;

  /** 自転車持ち込み区分 */
  private Integer bikesAllowedType;

  /** 便情報 */
  private String busServiceTripDesc;

  /** 便記号 */
  private String busServiceTripDescSymbol;

  /** 営業所ID */
  private Long officeId;

  /** 仕業番号 */
  private String workNumber;

  /** 便結合区分 */
  private String blockId;

  /** 系統ID */
  private Long lineRouteId;

  /** 車両ID */
  private Long vehicleId;

  /**
   * コンストラクタ
   *
   * @param dto 登録済み便ダイヤ情報取得用DTO
   */
  public BusServiceEntity(BusDaiyasRegistedDto dto) {
    this.busServiceId = dto.getBusServiceId();
    this.daiyaRevisionId = dto.getDaiyaRevisionId();
    this.lineRouteId = Long.valueOf(dto.getLineRouteId());
    this.busServiceTripHeadsign = dto.getBusServiceTripHeadsign();
    this.busServiceName = dto.getBusServiceName();
    this.calendarId = Long.valueOf(dto.getCalendarId());
    this.wheelchairAccessibleType = Integer.valueOf(dto.getWheelchairAccessibleType());
    this.bikesAllowedType = Integer.valueOf(dto.getBikesAllowedType());
    this.busServiceTripDesc = dto.getBusServiceTripDesc();
    this.busServiceTripDescSymbol = dto.getBusServiceTripDescSymbol();
    this.busServiceTripId = dto.getBusServiceTripId();
    if (dto.getOfficeId() != null && !dto.getOfficeId().isEmpty()) {
      this.officeId = Long.valueOf(dto.getOfficeId());
    }
    this.workNumber = dto.getWorkNumber();
    this.blockId = dto.getBlockId();
    if (dto.getVehicleId() != null && !dto.getVehicleId().isEmpty()) {
      this.vehicleId = Long.valueOf(dto.getVehicleId());
    }
  }

}
