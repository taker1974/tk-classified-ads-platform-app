package ru.spb.tksoft.ads.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Comment not owned by user.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkCommentNotOwnedException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(TkCommentNotOwnedException.class);

    /** Error code. */
    public static final int CODE = 663;

    /** Error message. */
    public static final String MESSAGE = "Comment not owned by user";

    /**
     * Constructor.
     * 
     * @param adId Ad ID.
     */
    public TkCommentNotOwnedException(long adId) {

        super(MESSAGE + ": " + adId);
        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, CODE, this);
    }
}
