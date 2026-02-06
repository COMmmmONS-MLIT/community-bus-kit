package maint.form.users;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.enums.RetirementFlag;
import maint.enums.UsageDivision;
import maint.form.BaseForm;
import maint.validation.annonation.IllegalCharacters;
import maint.validation.annonation.ValidEnumValue;

/**
 * ユーザ検索FORM
 *
 * @author ryuichi.kashiwai
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UsersSearchForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = -3386061975508033412L;

  /** ログインID */
  @IllegalCharacters
  @Size(max = 256)
  private String searchEmail;

  /** 氏名（姓） */
  @IllegalCharacters
  @Size(max = 50)
  private String searchLastName;

  /** 氏名（名） */
  @IllegalCharacters
  @Size(max = 50)
  private String searchFirstName;

  /** 利用区分 */
  @NotNull
  @ValidEnumValue(enumClass = UsageDivision.class)
  private Integer searchUsageDivision;

  /**
   * コンストラクタ
   */
  public UsersSearchForm() {
    this.searchUsageDivision = RetirementFlag.EMPLOYED.getValue();
  }

}
