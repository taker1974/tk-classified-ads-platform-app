package ru.spb.tksoft.ads.exception;

import java.util.Objects;

/**
 * Save media file failed.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkSavingMediaException extends TkAdBaseException {

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

        super(CODE, MESSAGE + ": " + (Objects.isNull(path) ? "" : path));
    }
}
