package maint.validation;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import maint.utils.ExObjectUtils;
import maint.validation.annonation.NumericLength;

/**
 * Null値可能Long型のバリデータ
 *
 * @author administrator
 */
public class NumericLengthValidator implements ConstraintValidator<NumericLength, Object> {

  private static final Pattern PATTERN = Pattern.compile("^[0-9]+$");
  private int max;

  @Override
  public void initialize(NumericLength nullableLong) {
    max = nullableLong.max();
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    return ExObjectUtils.isBlank(value) || (value.toString().length() <= max && PATTERN.matcher(
        value.toString()).find());
  }
}
