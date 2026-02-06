package maint.entity;

import static maint.utils.date.ExDateUtils.addDaysToDate;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maint.form.ChargeRevisions.ChargeRevisionsDetailForm;
import maint.session.UserSession;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 運賃改定Entity
 *
 * @author g.ko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargeRevisionEntity implements Serializable {

  /** Serial version UID */
  @Serial
  private static final long serialVersionUID = 122348990543L;

  /** 運賃改定ID */
  private Long chargeRevisionId;
  /** 路線ID */
  private Long rosenId;
  /** 運賃改定開始日 */
  private String serviceStartDate;
  /** 運賃改定終了日 */
  private String serviceEndDate;
  /** 運賃改定名称 */
  private String revisionReason;
  /** UserSession */
  private UserSession session;

  /**
   * コンストラクタ
   *
   * @param session ユーザーセッション
   * @param form 運賃改定詳細FORM
   * @param serviceEndDate 利用終了日
   */
  public ChargeRevisionEntity(UserSession session, ChargeRevisionsDetailForm form,
      String serviceEndDate) {
    this.rosenId = form.getRosenId();
    this.serviceStartDate = form.getServiceStartDate();
    this.serviceEndDate = serviceEndDate;
    this.revisionReason = form.getRevisionReason();
    this.session =
        ((UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }

  /**
   * コンストラクタ
   *
   * @param session ユーザーセッション
   * @param chargeRevisionId 運賃改定ID
   * @param serviceStartDate 利用開始日
   */
  public ChargeRevisionEntity(UserSession session, Long chargeRevisionId, String serviceStartDate) {
    this.chargeRevisionId = chargeRevisionId;
    // 利用開始日の前日を設定
    this.serviceStartDate = addDaysToDate(serviceStartDate, -1);
    this.session = session;
  }

  /**
   * コンストラクタ
   *
   * @param session ユーザーセッション
   * @param chargeRevisionId 運賃改定ID
   */
  public ChargeRevisionEntity(UserSession session, Long chargeRevisionId) {
    this.chargeRevisionId = chargeRevisionId;
    this.session = session;
  }
}
