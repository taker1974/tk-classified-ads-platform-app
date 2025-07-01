package ru.spb.tksoft.ads.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Comment not found.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkCommentNotFoundException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(TkCommentNotFoundException.class);

    /** Error code. */
    public static final int CODE = 922;

    /** Error message. */
    public static final String MESSAGE = "Comment not found";

    /**
     * Constructor.
     * 
     * @param subMessage Additional message.
     */
    public TkCommentNotFoundException(String subMessage) {

        super(MESSAGE + ": " + subMessage);
        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, CODE, this);
    }
}
