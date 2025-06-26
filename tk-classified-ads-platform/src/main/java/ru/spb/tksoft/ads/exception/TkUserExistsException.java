package ru.spb.tksoft.ads.exception;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     * @param userName User name.
     */
    public TkUserExistsException(String userName) {

        super(MESSAGE + ": " + (Objects.isNull(userName) ? "null" : userName));
        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, CODE);
    }
}
