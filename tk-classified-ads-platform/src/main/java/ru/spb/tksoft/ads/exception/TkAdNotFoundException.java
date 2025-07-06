package ru.spb.tksoft.ads.exception;

/**
 * Ad not found.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkAdNotFoundException extends TkAdBaseException {

    /** Error code. */
    public static final int CODE = 223;

    /** Error message. */
    public static final String MESSAGE = "Ad not found";

    /**
     * Constructor.
     * 
     * @param subMessage Additional message.
     */
    public TkAdNotFoundException(String subMessage) {

        super(CODE, MESSAGE + ": " + subMessage);
    }
}
