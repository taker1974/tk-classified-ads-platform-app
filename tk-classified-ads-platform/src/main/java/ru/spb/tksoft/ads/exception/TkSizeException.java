package ru.spb.tksoft.ads.exception;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Size exception: empty, too small, too large.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkSizeException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(TkSizeException.class);

    /** Error code. */
    public static final int CODE = 694;

    /** Error message. */
    public static final String MESSAGE = "Inadequate size of object";

    /**
     * Constructor.
     * 
     * @param argumentName Name of argument.
     */
    public TkSizeException(String objectName) {

        super(MESSAGE + ": " + (Objects.isNull(objectName) ? "" : objectName));
        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, CODE);
    }
}
