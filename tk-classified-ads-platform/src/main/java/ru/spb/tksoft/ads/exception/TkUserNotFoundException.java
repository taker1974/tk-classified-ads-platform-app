package ru.spb.tksoft.ads.exception;

import java.util.Objects;

/**
 * User not found.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkUserNotFoundException extends RuntimeException {

    /** Error code. */
    public static final int CODE = 885;

    /** Error message. */
    public static final String MESSAGE = "User not found";

    /** Error message, authentication failed. */
    public static final String MESSAGE_AUTH = "User with given credentials not found";

    /**
     * Constructor.
     * 
     * @param userName User name.
     * @param authenticationFailed True if authentication failed.
     */
    @SuppressWarnings("java:S3358") // Extract this nested ternary operation into an independent
                                    // statement.
    public TkUserNotFoundException(String userName, boolean authenticationFailed) {

        super(authenticationFailed ? MESSAGE_AUTH
                : MESSAGE + ": " + (Objects.isNull(userName) ? "null" : userName));
    }
}
