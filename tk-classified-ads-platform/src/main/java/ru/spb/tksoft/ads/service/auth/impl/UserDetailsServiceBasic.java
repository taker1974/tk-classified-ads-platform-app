package ru.spb.tksoft.ads.service.auth.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.entity.UserEntity;
import ru.spb.tksoft.ads.service.UserServiceCached;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * UserDetailsService implementation.
 * 
 * Dedicated to the Spring Security authentication flow, mostly for basic authentication.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceBasic implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserDetailsServiceBasic.class);

    private final UserServiceCached userServiceCached;

    /**
     * {@inheritDoc}
     * 
     * Used by Spring Security.
     */
    @Override
    @NotNull
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        UserEntity user = userServiceCached.getUserEntityLazy(userName);

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return User.builder()
                .username(user.getName())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
