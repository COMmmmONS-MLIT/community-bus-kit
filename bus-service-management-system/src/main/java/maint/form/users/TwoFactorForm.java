package maint.form.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import maint.form.BaseForm;

/**
 * 2要素認証フォーム
 *
 * @author sangsoo.lee
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TwoFactorForm extends BaseForm {

  /** Serial Version UID */
  private static final long serialVersionUID = -7050485729534535661L;

  /** 2要素認証コード */
  @NotBlank
  @Pattern(regexp = "^[A-Za-z0-9]{6}$", message = "{validation.two.factor.auth.code.invalid}")
  private String authCode;

}
