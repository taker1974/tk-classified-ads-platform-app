package ru.spb.tksoft.ads.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Ad not owned by user.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkAdNotOwnedException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(TkAdNotOwnedException.class);

    /** Error code. */
    public static final int CODE = 124;

    /** Error message. */
    public static final String MESSAGE = "Ad not not owned by user";

    /**
     * Constructor.
     * 
     * @param adId Ad ID.
     */
    public TkAdNotOwnedException(long adId) {

        super(MESSAGE + ": " + adId);
        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, CODE, this);
    }
}
