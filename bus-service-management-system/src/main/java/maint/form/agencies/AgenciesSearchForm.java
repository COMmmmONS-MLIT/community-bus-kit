package maint.form.agencies;

import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.form.BaseForm;
import org.hibernate.validator.constraints.Range;
import jakarta.validation.constraints.Size;

/**
 * 事業者検索FORM
 *
 * @author y.kim
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AgenciesSearchForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = 2222240987239487120L;

  /** PKID */
  private Long businessCorpId;

  /** 事業者名・自治体名 (検索用)*/
  @Size(max = 100)
  private String searchName;

  /** 法人番号 (検索用)*/
  @Size(max = 13)
  private String searchOperationCompanyNo;

  /** 利用区分 (検索用)*/
  @Range(min = 0, max = 1)
  private Integer searchAvailableFlag;
}
