package com.dteliukov.bookworm.controllers;

import com.dteliukov.bookworm.models.enums.UserStatus;
import com.dteliukov.bookworm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public String readUsers(Model model) {
        model.addAttribute("users", userService.load());

        return "admin/users";
    }

    @GetMapping("/{id}/ban")
    public String banUser(@PathVariable("id") int id) {
        userService.changeStatus(id, UserStatus.BANNED);
        return "redirect:/users";
    }

    @GetMapping("/{id}/active")
    public String activeUser(@PathVariable("id") int id) {
        userService.changeStatus(id, UserStatus.ACTIVE);
        return "redirect:/users";
    }
}
