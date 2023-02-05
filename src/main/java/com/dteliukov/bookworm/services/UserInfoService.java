package com.dteliukov.bookworm.services;

import com.dteliukov.bookworm.models.enums.UserStatus;
import com.dteliukov.bookworm.repositories.UserRepository;
import com.dteliukov.bookworm.security.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserInfoService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userInfo = userRepository.findByEmail(username);

        if (userInfo.isEmpty())
            throw new UsernameNotFoundException("User not found!");
        else if (userInfo.get().getUserStatus().equals(UserStatus.BANNED)) {
            throw new UsernameNotFoundException("User is banned!");
        }


        return new UserInfo(userInfo.get());
    }
}
