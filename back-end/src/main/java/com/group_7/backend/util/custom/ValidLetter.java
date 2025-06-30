package com.group_7.backend.util.custom;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LetterValidator.class) //Validator xử lí cho @ này
@Documented
public @interface ValidLetter {
    String message() default "Must contain only letters, spaces";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}