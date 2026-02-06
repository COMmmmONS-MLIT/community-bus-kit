package maint.validation.annonation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import maint.constants.CommConstant;
import maint.validation.IllegalCharactersValidator;

/**
 * 禁則文字のValidator用アノテーション<br>
 * <h1>ここにアノテーションの使い方を記載する。</h1>
 *
 * @author administrator
 */
@Documented
@Target({FIELD, TYPE_USE, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = IllegalCharactersValidator.class)
@ReportAsSingleViolation
public @interface IllegalCharacters {
  String message() default CommConstant.ILLEGAL_CHARACTERS + "は入力できません。";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
