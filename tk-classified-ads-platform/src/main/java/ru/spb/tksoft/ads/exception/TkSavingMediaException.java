package ru.spb.tksoft.ads.exception;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Save media file failed.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkSavingMediaException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(TkSavingMediaException.class);

    /** Error code. */
    public static final int CODE = 222;

    /** Error message. */
    public static final String MESSAGE = "Error saving media";

    /**
     * Constructor.
     * 
     * @param path Media path.
     */
    public TkSavingMediaException(String path) {

        super(MESSAGE + ": " + (Objects.isNull(path) ? "" : path));
        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, CODE);
    }
}
