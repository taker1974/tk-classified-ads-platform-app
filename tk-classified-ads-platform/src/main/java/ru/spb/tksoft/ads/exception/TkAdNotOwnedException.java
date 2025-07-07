package ru.spb.tksoft.ads.exception;

/**
 * Ad not owned by user.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkAdNotOwnedException extends TkAdBaseException {

    /** Error code. */
    public static final int CODE = 124;

    /** Error message. */
    public static final String MESSAGE = "Ad not owned by user";

    /**
     * Constructor.
     * 
     * @param adId Ad ID.
     */
    public TkAdNotOwnedException(long adId) {

        super(CODE, MESSAGE + ": " + adId);
    }
}
