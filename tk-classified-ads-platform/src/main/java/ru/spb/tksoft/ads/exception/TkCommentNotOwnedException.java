package ru.spb.tksoft.ads.exception;

/**
 * Comment not owned by user.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkCommentNotOwnedException extends TkAdBaseException {

    /** Error code. */
    public static final int CODE = 663;

    /** Error message. */
    public static final String MESSAGE = "Comment not owned by user";

    /**
     * Constructor.
     * 
     * @param adId Ad ID.
     */
    public TkCommentNotOwnedException(long adId) {

        super(CODE, MESSAGE + ": " + adId);
    }
}
