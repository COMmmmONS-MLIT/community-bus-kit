package maint.form.importIncomes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.sales.importIncomes.ImportIncomesDto;
import maint.form.BaseForm;
import maint.validation.annonation.DateFormat;

/**
 * 収入情報インポートFORM
 *
 * @author g.ko
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ImportIncomesForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = 25308947509280L;

  /** 路線ID（検索用） */
  @NotNull
  private Long rosenId;

  /** 便ID（検索用） */
  @NotNull
  private Long busServiceId;

  /** 利用期間（検索用） */
  @NotBlank
  @DateFormat("yyyy-MM-dd")
  private String serviceDate;

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

  /**
   * コンストラクタ
   *
   * @param form 収入情報インポートFORM
   * @param dto 収入情報インポート情報DTO
   */
  public void setInfo(ImportIncomesForm form, ImportIncomesDto dto) {
    this.rosenId = form.getRosenId();
    this.busServiceId = form.getBusServiceId();
    this.serviceDate = form.getServiceDate();
    this.dailyBusDaiyaId = dto.getDailyBusDaiyaId();
    this.incomeInfoId = dto.getIncomeInfoId();
    this.passengersCount = dto.getPassengersCount();
    this.cashIncome = dto.getCashIncome();
    this.icAmount = dto.getIcAmount();
    this.couponTicket = dto.getCouponTicket();
  }

}
