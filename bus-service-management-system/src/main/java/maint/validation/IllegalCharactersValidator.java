package maint.validation;

import java.util.Arrays;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import maint.constants.CommConstant;
import maint.utils.ExObjectUtils;
import maint.validation.annonation.IllegalCharacters;

/**
 * 禁則文字をチェックするValidator
 *
 * @author administrator
 */
public class IllegalCharactersValidator implements ConstraintValidator<IllegalCharacters, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (ExObjectUtils.isBlank(value)) {
      return true;
    }
    return Arrays.asList(value.split("\r\n")).stream()
        .filter(s -> s.matches(".*[" + CommConstant.ILLEGAL_CHARACTERS + "]+.*")).count() == 0;
  }
}
