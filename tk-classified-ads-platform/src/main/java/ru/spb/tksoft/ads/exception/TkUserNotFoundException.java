package ru.spb.tksoft.ads.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.constraints.NotBlank;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * User not found.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkUserNotFoundException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(TkUserNotFoundException.class);

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
    public TkUserNotFoundException(@NotBlank final String userName,final boolean authenticationFailed) {

        super(authenticationFailed ? MESSAGE_AUTH : MESSAGE + ": " + userName);
        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, CODE, this);
    }
}
