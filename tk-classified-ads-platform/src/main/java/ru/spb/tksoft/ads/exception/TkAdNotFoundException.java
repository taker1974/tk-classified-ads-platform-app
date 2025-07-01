package ru.spb.tksoft.ads.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Ad not found.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkAdNotFoundException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(TkAdNotFoundException.class);

    /** Error code. */
    public static final int CODE = 223;

    /** Error message. */
    public static final String MESSAGE = "Ad not found";

    /**
     * Constructor.
     * 
     * @param subMessage Additional message.
     */
    public TkAdNotFoundException(String subMessage) {

        super(MESSAGE + ": " + subMessage);
        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, CODE, this);
    }
}
