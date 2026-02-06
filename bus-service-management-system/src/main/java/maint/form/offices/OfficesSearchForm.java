package maint.form.offices;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.enums.UsageDivision;
import maint.form.BaseForm;
import maint.validation.annonation.IllegalCharacters;
import maint.validation.annonation.ValidEnumValue;

/**
 * 営業所管理検索FORM
 *
 * @author jeon
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OfficesSearchForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = -4486061975508033412L;

  /** 営業所ID（検索用） */
  private Long searchOfficeId;
  /** 運行会社名 （検索用） */
  private String searchOperatingName;
  /** 営業所名（検索用） */
  @IllegalCharacters
  @Size(max = 100)
  private String searchName;
  /** 利用区分（検索用） */
  @NotNull
  @ValidEnumValue(enumClass = UsageDivision.class)
  private Integer searchAvailableFlag;
  /** 運行会社ID （検索用） */
  private Long searchOperatingCompanyId;
  /** 営業所ID */
  private String businessCorpId;
  /** ユーザーID */
  private Long userId;
}
