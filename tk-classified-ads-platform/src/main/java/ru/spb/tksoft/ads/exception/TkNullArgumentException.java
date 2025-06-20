package ru.spb.tksoft.ads.exception;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * User not found.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkNullArgumentException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(TkNullArgumentException.class);

    /** Error code. */
    public static final int CODE = 225;

    /** Error message. */
    public static final String MESSAGE = "Argument must not be null";

    /**
     * Constructor.
     * 
     * @param argumentName Name of argument.
     */
    public TkNullArgumentException(String argumentName) {

        super(MESSAGE + ": " + (Objects.isNull(argumentName) ? "" : argumentName));
        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, CODE);
    }
}
