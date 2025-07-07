package ru.spb.tksoft.ads.exception;

/**
 * Comment not found.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkCommentNotFoundException extends TkAdBaseException {

    /** Error code. */
    public static final int CODE = 922;

    /** Error message. */
    public static final String MESSAGE = "Comment not found";

    /**
     * Constructor.
     * 
     * @param subMessage Additional message.
     */
    public TkCommentNotFoundException(String subMessage) {

        super(CODE, MESSAGE + ": " + subMessage);
    }
}
