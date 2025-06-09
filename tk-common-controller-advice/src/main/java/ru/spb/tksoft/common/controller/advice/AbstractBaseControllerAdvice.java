package ru.spb.tksoft.common.controller.advice;

import jakarta.validation.constraints.NotNull;

/**
 * Base class for controller advice.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public abstract class AbstractBaseControllerAdvice {

    /** Message prefix for any exception message. */
    public static final String MESSAGE_PREFIX = "Exception caught";

    /** Default constructor. */
    protected AbstractBaseControllerAdvice() {}

    /**
     * Default message.
     * 
     * @param obj Any object which name will be displayed in message body.
     * @return Common message.
     */
    protected String getCommonMessage(@NotNull Object obj) {

        return String.format("%s %s", MESSAGE_PREFIX, obj.getClass().getSimpleName());
    }
}
