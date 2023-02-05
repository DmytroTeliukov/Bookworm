package com.dteliukov.bookworm.config;

import com.dteliukov.bookworm.models.enums.Role;
import com.dteliukov.bookworm.services.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserInfoService userInfoService;

    @Autowired
    public SecurityConfig(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/users/**", "/registration/librarian").hasRole(Role.ADMIN.name())
                .antMatchers("/authors/new", "/authors/edit", "/categories**", "/reservation**").hasRole(Role.LIBRARIAN.name())
                .antMatchers("/login", "/registration", "/error").permitAll()
                .anyRequest().hasAnyRole(Role.MEMBER.name(), Role.LIBRARIAN.name(), Role.ADMIN.name())
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/profile", true)
                .failureUrl("/login?error")
                .and()
                .logout().logoutUrl("/logout")
                .logoutSuccessUrl("/login");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userInfoService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
