package ru.spb.tksoft.ads.service.auth.impl;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.RegisterRequestDto;
import ru.spb.tksoft.ads.service.auth.AuthService;

/**
 * Authentication service implementation.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @NotNull
    private final UserDetailsService userDetailsService;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean register(RegisterRequestDto register) {

        // TODO: Implement registering logic.TODO: Implement registering logic.
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean login(String userName, String password) {

        // TODO: Implement login logic.
        return true;
    }
}
