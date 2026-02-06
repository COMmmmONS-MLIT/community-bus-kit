package maint.validation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import maint.enums.ValidateEnumsInterFace;
import maint.utils.ExObjectUtils;
import maint.validation.annonation.ValidEnumValue;

/**
 * Enumに存在する値かをチェックするバリデータ<br> ここにアノテーションの使い方を記載
 *
 * @author administrator
 */
public class EnumValueValidator implements ConstraintValidator<ValidEnumValue, Object> {

  /** Enum値リスト */
  private List<Object> enumValue;

  /**
   * Enum値リスト設定
   */
  @Override
  public void initialize(ValidEnumValue validEnum) {
    enumValue =
        Stream.of(validEnum.enumClass().getEnumConstants()).map(ValidateEnumsInterFace::getValue)
            .collect(Collectors.toList());
  }

  /**
   * valueリスト比較
   *
   * @param value   値
   * @param context ConstraintValidatorContext
   * @return true:Enumに一致する値あり false:Enumに一致する値なし
   */
  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    return ExObjectUtils.isBlank(value) || enumValue.contains(value);
  }
}
