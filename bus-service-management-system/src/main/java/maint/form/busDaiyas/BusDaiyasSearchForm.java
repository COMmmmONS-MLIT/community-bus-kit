package maint.form.busDaiyas;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.enums.LineDetailServiceDivision;
import maint.form.BaseForm;
import maint.validation.annonation.ValidEnumValue;

/**
 * 便ダイヤ検索FORM
 *
 * @author jeon
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BusDaiyasSearchForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = -7622555655717056036L;

  /** 便ダイヤID */
  private Long searchDaiyaRevisionId;
  /** 路線ID */
  private Long searchRosenId;
  /** ダイヤ改正名称 */
  private String searchDaiyaRevisionName;
  /** 便ダイヤ開始日 */
  private String searchServiceStartDate;
  /** 便ダイヤ終了日 */
  private String searchServiceEndDate;
  /** 往復区分 */
  @NotNull
  @ValidEnumValue(enumClass = LineDetailServiceDivision.class)
  private Integer searchLineDetailServiceDivisionFlg;
  /** 系統ID */
  private Long lineRouteId;
  /** ユーザーID */
  private Long userId;

}
