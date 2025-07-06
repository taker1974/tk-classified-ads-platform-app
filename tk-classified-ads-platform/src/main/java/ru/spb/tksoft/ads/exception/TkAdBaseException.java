package ru.spb.tksoft.ads.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Getter;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Application-specific base exception abstract class.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public abstract class TkAdBaseException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(TkAdBaseException.class);

    @Getter
    private final int code;

    /**
     * Base constructor.
     * 
     */
    protected TkAdBaseException(int code, String message) {

        super(message);
        this.code = code;
        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, this.code, this);
    }
}
