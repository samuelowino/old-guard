package com.oldguard.rickandmortyapi.annotations;

import com.oldguard.rickandmortyapi.Validators.ValidateStatus;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidateStatus.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface ValidStatus {
    String message() default "Status can only be alive,dead or unknown";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
