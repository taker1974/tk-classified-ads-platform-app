package ru.spb.tksoft.ads.service.auth.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.entity.UserEntity;
import ru.spb.tksoft.ads.exception.TkNotFoundException;
import ru.spb.tksoft.ads.repository.UserRepository;
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
public class UserDetailsServiceBasicCached implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserDetailsServiceBasicCached.class);

    @NotNull
    private final UserRepository userRepository;

    @NotNull
    private final PasswordEncoder passwordEncoder;

    /**
     * Clears the cache.
     */
    @CacheEvict(cacheNames = {"userDetailed"}, allEntries = true)
    public void clearCache() {
        // ..
    }

    /**
     * {@inheritDoc}
     * 
     * Used by Spring Security.
     */
    @Override
    @Cacheable(value = "userDetailed", unless = "#result == null", key = "#name")
    @NotNull
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        UserEntity user = userRepository.findOneByName(userName)
            .orElseThrow(()->new TkNotFoundException("User with given name is not exists: " + userName));

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return User.builder()
                .username(user.getName())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
