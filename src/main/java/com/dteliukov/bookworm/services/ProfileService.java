package com.dteliukov.bookworm.services;

import com.dteliukov.bookworm.exceptions.PasswordConfirmException;
import com.dteliukov.bookworm.models.entities.User;
import com.dteliukov.bookworm.repositories.UserRepository;
import com.dteliukov.bookworm.security.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileService(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User get() {
        return getUser();
    }

    public void updatePassword(String oldPassword,
                               String newPassword,
                               String confirmPassword) {
        var user = getUser();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new PasswordConfirmException("Old password is incorrect!");
        } else if (!newPassword.equals(confirmPassword)) {
            throw new PasswordConfirmException("Not confirmed password!");
        } else {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
    }
    public void update(User user) {
        var currentUser = getUser();

        currentUser.setLastname(user.getLastname());
        currentUser.setFirstname(user.getFirstname());
        currentUser.setEmail(user.getEmail());

        userRepository.save(currentUser);
    }
    public void delete() {
        userRepository.delete(getUser());
        SecurityContextHolder.clearContext();
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (UserInfo) authentication.getPrincipal();

        return user.getUser();
    }

}
