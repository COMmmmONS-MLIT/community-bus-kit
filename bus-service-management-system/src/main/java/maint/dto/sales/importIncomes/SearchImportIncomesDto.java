package maint.dto.sales.importIncomes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.dto.BaseDto;
import maint.form.importIncomes.ImportIncomesForm;
import maint.session.UserSession;

/**
 * 収入情報インポート検索DTO
 *
 * @author g.ko
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SearchImportIncomesDto extends BaseDto {

  /** Serial Version UId */
  private static final long serialVersionUID = 9485278490L;

  /** 路線ID（検索用） */
  private Long rosenId;

  /** 便ID（検索用） */
  private Long busServiceId;

  /** 利用期間（検索用） */
  private String serviceDate;

  /** 事業者ID */
  private Long businessCorpId;

  /**
   * コンストラクタ
   *
   * @param form 収入情報インポートFORM
   * @param userSession ユーザーセッション
   */
  public SearchImportIncomesDto(ImportIncomesForm form, UserSession userSession) {
    if (form == null) {
      return;
    }
    this.rosenId = form.getRosenId();
    this.busServiceId = form.getBusServiceId();
    this.serviceDate = form.getServiceDate();
    this.businessCorpId = userSession.getBusinessCorpId();
  }

}
