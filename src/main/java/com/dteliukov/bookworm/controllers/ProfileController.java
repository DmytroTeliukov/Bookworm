package com.dteliukov.bookworm.controllers;

import com.dteliukov.bookworm.exceptions.PasswordConfirmException;
import com.dteliukov.bookworm.models.entities.User;
import com.dteliukov.bookworm.services.ProfileService;
import com.dteliukov.bookworm.utils.UserEmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final UserEmailValidator userEmailValidator;

    @Autowired
    public ProfileController(ProfileService profileService, UserEmailValidator userEmailValidator) {
        this.profileService = profileService;
        this.userEmailValidator = userEmailValidator;
    }

    @GetMapping
    public String profile(Model model) {
        model.addAttribute("user", profileService.get());
        return "profile/profile";
    }

    @DeleteMapping
    public String deleteProfile() {
        profileService.delete();
        return "redirect:/login";
    }

    @GetMapping("/edit")
    public String editProfile(Model model) {
        var user = profileService.get();
        model.addAttribute("user", user);
        return "profile/edit";
    }

    @GetMapping("/password")
    public String editPassword() {
        return "profile/edit_password";
    }

    @PostMapping("/password")
    public String updatePassword(@ModelAttribute("old_password") String oldPassword,
                                 @ModelAttribute("new_password") String newPassword,
                                 @ModelAttribute("confirm_password") String confirmPassword,
                                 Model model) {

        try {
            profileService.updatePassword(oldPassword, newPassword, confirmPassword);
            return "redirect:/profile";
        } catch (PasswordConfirmException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "profile/edit_password";
    }



    @PatchMapping
    public String updateProfile(@ModelAttribute("user") @Valid User user,
                                BindingResult bindingResult) {
        userEmailValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors()) {
            return "profile/edit";
        }

        profileService.update(user);
        System.out.println("Done update!");
        return "redirect:/profile";
     }
}
