package ru.spb.tksoft.ads.exception;

import java.util.Objects;

/**
 * Unsupported media type.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkUnsupportedMediaTypeException extends RuntimeException {

    /** Error code. */
    public static final int CODE = 673;

    /** Error message. */
    public static final String MESSAGE = "Unsupported media type";

    /**
     * Constructor.
     * 
     * @param mediaType Media type.
     */
    public TkUnsupportedMediaTypeException(String mediaType) {

        super(MESSAGE + ": " + (Objects.isNull(mediaType) ? "" : mediaType));
    }
}
