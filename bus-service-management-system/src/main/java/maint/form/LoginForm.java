package maint.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * ログインフォーム
 *
 * @author administrator
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LoginForm extends BaseForm {

  /** Serial Version UID */
  @Serial
  private static final long serialVersionUID = 6925289592140715752L;

  /** メール */
  private String email;

  /** パスワード */
  private String loginPass;

  /** エラーメッセージKey */
  private String errorMessageKey;

}
