package ru.spb.tksoft.ads.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Media [file] not found.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkMediaNotFoundException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(TkMediaNotFoundException.class);

    /** Error code. */
    public static final int CODE = 222;

    /** Error message. */
    public static final String MESSAGE = "Media not found";

    /**
     * Constructor.
     * 
     * @param mediaId Media ID.
     */
    public TkMediaNotFoundException(String mediaId) {

        super(MESSAGE + ": " + mediaId);
        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, CODE, this);
    }
}
