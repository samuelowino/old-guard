package com.oldguard.rickandmortyapi.Validators;

import com.oldguard.rickandmortyapi.annotations.ValidStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidateStatus implements ConstraintValidator<ValidStatus, String> {

    @Override
    public void initialize(ValidStatus constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        switch (value.toLowerCase()) {
        case "alive", "dead", "unknown" -> {
            return true;
        }
        default -> {
            return false;
        }
        }
    }
}
