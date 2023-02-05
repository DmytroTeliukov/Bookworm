package com.dteliukov.bookworm.utils;

import com.dteliukov.bookworm.models.entities.Author;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AuthorYearsValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Author.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var author = (Author) target;

        if (author.getYearDeath() != 0) {
            if(author.getYearDeath() - author.getYearBirth() <= 0) {
                errors.rejectValue("yearBirth", "", "Year of birth should be less than year of death!");
            }
        }

    }
}
