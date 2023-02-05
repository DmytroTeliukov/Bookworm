package com.dteliukov.bookworm.controllers;

import com.dteliukov.bookworm.exceptions.PasswordConfirmException;
import com.dteliukov.bookworm.models.entities.User;
import com.dteliukov.bookworm.services.RegistrationService;
import com.dteliukov.bookworm.utils.UserEmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final UserEmailValidator userEmailValidator;

    @Autowired
    public RegistrationController(RegistrationService registrationService, UserEmailValidator userEmailValidator) {
        this.registrationService = registrationService;
        this.userEmailValidator = userEmailValidator;
    }

    @GetMapping
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "auth/registration";
    }

    @PostMapping
    public String addMember(Model model,
                            @RequestParam("confirm_password") String confirmPassword,
                            @ModelAttribute("user") @Valid User user,
                            BindingResult bindingResult) {
        userEmailValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors())
            return "auth/registration";

        try {
            registrationService.saveMember(user, confirmPassword);
            return "redirect:/profile";
        } catch (PasswordConfirmException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "auth/registration";
    }

    @GetMapping("/librarian")
    public String registrationLibrarian(Model model) {
        model.addAttribute("user", new User());
        return "admin/hire";
    }

    @PostMapping("/librarian")
    public String hireLibrarian(@ModelAttribute("user") @Valid User user,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/hire";

        registrationService.saveLibrarian(user);
        return "redirect:/users";
    }
}
