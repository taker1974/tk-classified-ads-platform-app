package ru.spb.tksoft.ads.exception;

import java.util.Objects;

/**
 * User exists.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkUserExistsException extends TkAdBaseException {

    /** Error code. */
    public static final int CODE = 957;

    /** Error message. */
    public static final String MESSAGE = "User exists";

    /**
     * The only constructor.
     * 
     * @param userName User name.
     */
    public TkUserExistsException(String userName) {

        super(CODE, MESSAGE + ": " + (Objects.isNull(userName) ? "null" : userName));
    }
}
