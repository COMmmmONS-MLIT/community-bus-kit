package maint.form.signposts;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.enums.UsageDivision;
import maint.form.BaseForm;
import maint.validation.annonation.IllegalCharacters;
import maint.validation.annonation.ValidEnumValue;

/**
 * 標柱検索FORM
 *
 * @author g.ko
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SignpostsSearchForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = -375506213532L;

  /** 停留所ID（検索用） */
  private Long searchBusStopId;

  /** 標柱枝番（検索用） */
  @IllegalCharacters
  @Size(max = 100)
  private String searchSignpostBranchNumber;

  /** 標柱名称（検索用） */
  @IllegalCharacters
  @Size(max = 100)
  private String searchSignpostName;

  /** 利用可否区分（0：利用不可、1：利用中） */
  @NotNull
  @ValidEnumValue(enumClass = UsageDivision.class)
  private Integer availableFlag;

  /**
   * コンストラクタ
   */
  public SignpostsSearchForm() {
    this.availableFlag = UsageDivision.ACTIVE.getValue();
  }

}
