package maint.validation;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import maint.utils.ExObjectUtils;
import maint.validation.annonation.DateFormat;

/**
 * 日時をチェックするバリデータ
 *
 * @author administrator
 */
public class DateFormatValidator implements ConstraintValidator<DateFormat, String> {

  private String format;

  @Override
  public void initialize(DateFormat dateFormat) {
    format = dateFormat.value();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    if (ExObjectUtils.isBlank(value)) {
      return true;
    }

    try {
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
      if (format.equals("yyyyMM") || format.equals("yyyy-MM") || format.equals(
          "uuuuMM") || format.equals("uuMM")) {
        YearMonth.parse(value, dtf).atDay(1);
      } else {
        LocalDate.parse(value, dtf);
      }
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }
}
