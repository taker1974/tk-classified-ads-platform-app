package ru.spb.tksoft.ads.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.constraints.NotBlank;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * User exists.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkUserExistsException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(TkUserExistsException.class);

    /** Error code. */
    public static final int CODE = 957;

    /** Error message. */
    public static final String MESSAGE = "User exists";

    /**
     * The only constructor.
     * 
     * @param message Error message.
     */
    public TkUserExistsException(@NotBlank final String userName) {

        super(MESSAGE + ": " + userName);
        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, CODE, this);
    }
}
