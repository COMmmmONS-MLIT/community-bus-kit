package maint.entity;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maint.form.importIncomes.ImportIncomesForm;
import maint.session.UserSession;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 収入情報Entity
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeInfosEntity implements Serializable {

  /** Serial version UID */
  @Serial
  private static final long serialVersionUID = 56435436436L;

  /** 収入情報ID */
  private Long incomeInfoId;
  /** 日毎便ダイヤID */
  private Long dailyBusDaiyaId;
  /** 乗車人数 */
  private Long passengersCount;
  /** 定期券人数 */
  private Long regularCount;
  /** 現金収入 */
  private Long cashIncome;
  /** 回数券 */
  private Long couponTicket;
  /** ICカード金額 */
  private Long icAmount;
  /** UserSession */
  private UserSession session;

  /**
   * コンストラクタ
   *
   * @param form 収入情報インポートFORM
   */
  public IncomeInfosEntity(ImportIncomesForm form) {
    this.dailyBusDaiyaId = form.getDailyBusDaiyaId();
    this.incomeInfoId = form.getIncomeInfoId();
    this.passengersCount = Long.valueOf(form.getPassengersCount());
    this.regularCount = 0L;
    this.cashIncome = Long.valueOf(form.getCashIncome());
    this.couponTicket = Long.valueOf(form.getCouponTicket());
    this.icAmount = Long.valueOf(form.getIcAmount());
    this.session =
        ((UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }
}
