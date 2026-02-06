package maint.form.operatingCompanies;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.enums.UsageDivision;
import maint.form.BaseForm;
import maint.validation.annonation.IllegalCharacters;
import maint.validation.annonation.ValidEnumValue;

/**
 * 運行会社検索FORM
 *
 * @author jeon
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OperatingCompaniesSearchForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = -4486061975508033412L;

  /** 事業者（検索用） */
  private Long searchBusinessCorpId;
  /** 事業者名（検索用） */
  private String searchBusinessCorpName;
  /** 運行会社名（検索用） */
  @IllegalCharacters
  @Size(max = 100)
  private String searchOperatingName;
  /** 利用区分（検索用） */
  @NotNull
  @ValidEnumValue(enumClass = UsageDivision.class)
  private Integer searchAvailableFlag;
  /** ユーザーID */
  private Long userId;
}
