package maint.validation.annonation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import maint.enums.ValidateEnumsInterFace;
import maint.validation.EnumValueValidator;

/**
 * Enumに存在する値かをチェックするバリデータ用アノテーション<br>
 * <h1>ここにアノテーションの使い方を記載する。</h1>
 *
 * @author administrator
 */
@Documented
@Target({FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = EnumValueValidator.class)
@ReportAsSingleViolation
public @interface ValidEnumValue {
  Class<? extends ValidateEnumsInterFace> enumClass();

  String message() default "{validation.enum}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
