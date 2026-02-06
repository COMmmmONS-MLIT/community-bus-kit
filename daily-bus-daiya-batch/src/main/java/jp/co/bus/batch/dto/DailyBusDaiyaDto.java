package jp.co.bus.batch.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 便情報 DTO
 *
 * @author g.ko
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DailyBusDaiyaDto extends BaseDto {

  /** Serial Version UID */
  private static final long serialVersionUID = -5238754207L;

  /** 日毎便ダイヤID */
  private Long dailyBusDaiyaId;

  /** 便ID */
  private Long busServiceId;

  /** 運行年月日 */
  private String serviceDate;

  /** 便休フラグ */
  private Integer salesCancelFlg;

  /** 運行対象外フラグ */
  private Integer operationCancelFlg;

  /** 車両ID */
  private Long vehicleId;

}
