package kr.mafoo.photo.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {MatchEnum.EnumValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchEnum {
    String message() default "타입이 올바르지 않습니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends java.lang.Enum<?>> enumClass();

    class EnumValidator implements ConstraintValidator<MatchEnum, String> {
        private MatchEnum annotation;

        @Override
        public void initialize(MatchEnum constraintAnnotation) {
            this.annotation = constraintAnnotation;
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null) return false;
            boolean result = false;
            Object[] enumValues = this.annotation.enumClass().getEnumConstants();
            if (enumValues != null) {
                for (Object enumValue : enumValues) {
                    if (value.equals(enumValue.toString())) {
                        result = true;
                        break;
                    }
                }
            }
            return result;
        }
    }
}
