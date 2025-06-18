package ru.spb.tksoft.ads.service.auth.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ru.spb.tksoft.ads.dto.request.RegisterRequestDto;
import ru.spb.tksoft.ads.entity.UserEntity;
import ru.spb.tksoft.ads.exception.TkUserExistsException;
import ru.spb.tksoft.ads.mapper.UserMapper;
import ru.spb.tksoft.ads.service.UserService;
import ru.spb.tksoft.ads.service.auth.AuthService;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Authentication service implementation.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@Service
@RequiredArgsConstructor
public class AuthServiceBasic implements AuthService {

    private final Logger log = LoggerFactory.getLogger(AuthServiceBasic.class);

    @NotNull
    private final UserService userService;

    @NotNull
    private final PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     * 
     * Creates a new user if it does not exist.
     * 
     * Note for dbg/dev: password '12345678' is encoded to
     * '$2a$10$dNwSTUHdpy8BEEgtQvWKLuhfh2rNSSJIZRG3PieITjsDPcphmGGoi'; password '0123456789ABCDEF'
     * is encoded to '$2a$10$Vi90CYzDu3rPEjBSI5nrOeVBB.xc0t2G38atUD8tBMZ./lse.unt.';
     * 
     * @param registerRequest RegisterRequestDto object with user credentials.
     * @return true if the user is successfully created, false otherwise.
     */
    @Override
    public boolean register(@NotNull final RegisterRequestDto registerRequest) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        final String userName = registerRequest.getUsername();
        try {
            if (userService.existsByName(userName)) {
                throw new TkUserExistsException(userName);
            }

            UserEntity newUser = UserMapper.toEntity(registerRequest);
            newUser.setPassword(
                    passwordEncoder.encode(registerRequest.getPassword()));

            userService.createUser(newUser);

        } catch (Exception ex) { // Unexpected exception only.
            LogEx.error(log, LogEx.getThisMethodName(), ex);
            return false;
        }

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * All this logic is just for demonstration purposes: returns true anyway because we already
     * logged in via basic auth. See loadUserByUsername()
     * 
     * @param userName User name.
     * @param password Password.
     */
    @Override
    public boolean login(@NotBlank final String userName, @NotBlank final String password) {

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STARTING);

        try {
            if (userService.existsByNameAndPassword(
                    userName, passwordEncoder.encode(password))) {

                // We don't want to explain the reason to the user, but log it.
                LogEx.warn(log, LogEx.getThisMethodName(),
                        "User with given credentials not exists: %s", userName);
                return false;
            }

        } catch (Exception ex) { // Unexpected exception only.
            LogEx.error(log, LogEx.getThisMethodName(), ex);
            return false;
        }

        LogEx.trace(log, LogEx.getThisMethodName(), LogEx.STOPPING);
        return true;
    }
}
