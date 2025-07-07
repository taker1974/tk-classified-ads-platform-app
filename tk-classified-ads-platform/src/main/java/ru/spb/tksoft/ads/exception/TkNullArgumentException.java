package ru.spb.tksoft.ads.exception;

import java.util.Objects;

/**
 * User not found.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class TkNullArgumentException extends TkAdBaseException {

    /** Error code. */
    public static final int CODE = 225;

    /** Error message. */
    public static final String MESSAGE = "Argument must not be null";

    /**
     * Constructor.
     * 
     * @param argumentName Name of argument.
     */
    public TkNullArgumentException(String argumentName) {

        super(CODE, MESSAGE + ": " + (Objects.isNull(argumentName) ? "" : argumentName));
    }
}
