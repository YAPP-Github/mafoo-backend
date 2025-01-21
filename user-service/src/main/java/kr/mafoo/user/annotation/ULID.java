package kr.mafoo.user.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ULID.ULIDValidator.class})
public @interface ULID {
    String message() default "ULID 형식이 아닙니다";
    Class[] groups() default {};
    Class[] payload() default {};

    class ULIDValidator implements ConstraintValidator<ULID, String> {
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null) return false;
            return value.matches("[0-7][0-9A-HJKMNP-TV-Z]{25}");
        }
    }

}
