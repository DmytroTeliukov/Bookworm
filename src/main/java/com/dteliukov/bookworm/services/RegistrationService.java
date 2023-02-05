package com.dteliukov.bookworm.services;

import com.dteliukov.bookworm.exceptions.PasswordConfirmException;
import com.dteliukov.bookworm.models.entities.Member;
import com.dteliukov.bookworm.models.entities.User;
import com.dteliukov.bookworm.models.enums.Role;
import com.dteliukov.bookworm.models.enums.UserStatus;
import com.dteliukov.bookworm.repositories.MemberRepository;
import com.dteliukov.bookworm.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserRepository userRepository,
                               MemberRepository memberRepository,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void saveMember(User user, String confirmedPassword) {
        if (!user.getPassword().equals(confirmedPassword)) {
            throw new PasswordConfirmException("The password is not confirmed!");
        }
        prepareUserData(user, Role.MEMBER);

        Member member = new Member();
        member.setUser(user);
        user.setMember(member);

        userRepository.save(user);
        memberRepository.save(member);
    }

    @Transactional
    public void saveLibrarian(User user) {
        prepareUserData(user, Role.LIBRARIAN);
        userRepository.save(user);
    }

    private void prepareUserData(User user, Role role) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setJoined(new Date());
        user.setRole(Role.LIBRARIAN);
        user.setUserStatus(UserStatus.ACTIVE);
    }

}
