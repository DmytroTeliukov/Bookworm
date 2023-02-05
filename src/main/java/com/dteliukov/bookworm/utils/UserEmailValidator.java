package com.dteliukov.bookworm.utils;

import com.dteliukov.bookworm.models.entities.User;
import com.dteliukov.bookworm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserEmailValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserEmailValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        var getUser = userService.get(user.getEmail());

        if (getUser.isPresent()) {
            if (user.getId() != getUser.get().getId()) {
                errors.rejectValue("email", "", "The email is already taken!");
            }
        }
    }
}
