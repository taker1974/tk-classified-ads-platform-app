package ru.spb.tksoft.ads.exception;

/**
 * Media [file] not found.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkMediaNotFoundException extends RuntimeException {

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
    }
}
