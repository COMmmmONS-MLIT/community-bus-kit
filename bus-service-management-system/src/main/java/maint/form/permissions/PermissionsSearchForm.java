package maint.form.permissions;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.form.BaseForm;
import maint.validation.annonation.IllegalCharacters;

/**
 * 権限検索FORM
 *
 * @author y.kim
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PermissionsSearchForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = 2472340987239487120L;

  /** 権限名（検索用） */
  @IllegalCharacters
  @Size(max = 20)
  private String searchPermRoleName;
}
