package ru.spb.tksoft.ads.service.auth;

import ru.spb.tksoft.ads.dto.RegisterRequestDto;

/**
 * Authentication and authorization service interface.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public interface AuthService {

    /**
     * Login user.
     * 
     * @param userName User name
     * @param password Password
     * @return True if login was successful
     */
    boolean login(String userName, String password);

    /**
     * Register user.
     * 
     * @param registerRequest User data.
     * @return True if registration was successful
     */
    boolean register(RegisterRequestDto registerRequest);
}
