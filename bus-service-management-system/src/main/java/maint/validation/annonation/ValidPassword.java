package maint.validation.annonation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import maint.validation.PasswordStrengthValidator;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordStrengthValidator.class)
@Documented
public @interface ValidPassword {
  String message() default "パスワードは8桁以上30桁未満で、英大文字・小文字・数字・記号のうち3種類以上を含めてください。";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

