package maint.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import maint.utils.ExObjectUtils;
import maint.utils.PasswordUtils;
import maint.validation.annonation.ValidPassword;

public class PasswordStrengthValidator implements ConstraintValidator<ValidPassword, String> {

  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
    if (ExObjectUtils.isBlank(password)) {
      return true;
    }
    return PasswordUtils.isStrongPassword(password);
  }
}
