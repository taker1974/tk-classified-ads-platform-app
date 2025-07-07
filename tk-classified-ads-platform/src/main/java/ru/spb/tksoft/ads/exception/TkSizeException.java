package ru.spb.tksoft.ads.exception;

import java.util.Objects;

/**
 * Size exception: empty, too small, too large.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkSizeException extends TkAdBaseException {

    /** Error code. */
    public static final int CODE = 694;

    /** Error message. */
    public static final String MESSAGE = "Inadequate size of object";

    /**
     * Constructor.
     * 
     * @param objectName Name of object.
     */
    public TkSizeException(String objectName) {

        super(CODE, MESSAGE + ": " + (Objects.isNull(objectName) ? "" : objectName));
    }
}
