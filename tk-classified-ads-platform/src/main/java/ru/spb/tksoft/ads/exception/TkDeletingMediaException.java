package ru.spb.tksoft.ads.exception;

import java.util.Objects;

/**
 * Delete media file failed.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkDeletingMediaException extends TkAdBaseException {

    /** Error code. */
    public static final int CODE = 657;

    /** Error message. */
    public static final String MESSAGE = "Error deleting media";

    /**
     * Constructor.
     * 
     * @param path Media path.
     */
    public TkDeletingMediaException(String path) {

        super(CODE, MESSAGE + ": " + (Objects.isNull(path) ? "" : path));
    }
}
