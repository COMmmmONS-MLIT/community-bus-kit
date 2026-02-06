package maint.form.daiyaRevisions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.form.BaseForm;

/**
 * 便ダイヤ改正検索FORM
 *
 * @author jeon
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DaiyaRevisionsSearchForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = -7622555655717056036L;

  /** 便ダイヤ改正ID */
  private Long searchDaiyaRevisionId;
  /** 路線ID */
  private Long searchRosenId;
  /** ダイヤ改正名称 */
  private String searchDaiyaRevisionName;
  /** 便ダイヤ改正開始日 */
  private String searchServiceStartDate;
  /** 便ダイヤ改正終了日 */
  private String searchServiceEndDate;
  /** ユーザーID */
  private Long userId;

}
