package com.dteliukov.bookworm.services;

import com.dteliukov.bookworm.models.entities.Member;
import com.dteliukov.bookworm.models.entities.User;
import com.dteliukov.bookworm.models.enums.UserStatus;
import com.dteliukov.bookworm.repositories.MemberRepository;
import com.dteliukov.bookworm.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> load() {
        return userRepository.findAll();
    }

    public Optional<User> get(int id) {
        return userRepository.findById(id);
    }

    public Optional<User> get(String email) {
        return userRepository.findByEmail(email);
    }

    public void changeStatus(int id, UserStatus userStatus) {
        var user = get(id).get();
        user.setUserStatus(userStatus);
        userRepository.save(user);
    }
}
