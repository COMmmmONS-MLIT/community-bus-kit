package maint.dto.sales.importIncomes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;

/**
 * 収入情報インポート情報DTO
 *
 * @author g.ko
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ImportIncomesDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 895342750230L;

  /** 日毎便ダイヤID */
  private Long dailyBusDaiyaId;

  /** 収入情報ID */
  private Long incomeInfoId;

  /** 乗車人数 */
  private String passengersCount;

  /** 現金 */
  private String cashIncome;

  /** ICカード金額 */
  private String icAmount;

  /** 回数券 */
  private String couponTicket;

}
