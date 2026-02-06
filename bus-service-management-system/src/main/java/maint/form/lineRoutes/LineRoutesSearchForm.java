package maint.form.lineRoutes;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.form.BaseForm;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;

/**
 * 系統検索FORM
 *
 * @author y.kim
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LineRoutesSearchForm extends BaseForm {

  /** Serial Version UID */
  @Serial
  private static final long serialVersionUID = 1572340987239487120L;

  /** PKID */
  private Long lineRouteId;

  /** 路線ID */
  private Long rosenId;

  /** session事業者ID */
  private Long businessCorpId;

  /** 系統名称 (検索用)*/
  @Size(max = 100)
  private String lineRouteName;

  /** 利用可否区分 (検索用)*/
  @Range(min = 0, max = 1)
  private Integer searchAvailableFlag;
}
