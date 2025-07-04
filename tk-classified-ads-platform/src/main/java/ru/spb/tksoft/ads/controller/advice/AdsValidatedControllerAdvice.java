package ru.spb.tksoft.ads.controller.advice;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import ru.spb.tksoft.common.controller.advice.ValidatedControllerAdvice;

/**
 * Interception of very common exceptions. ORDER IS IMPORTANT! @see @Order()
 * 
 * @see ru.spb.tksoft.common.controller.advice.CommonControllerAdvice
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AdsValidatedControllerAdvice extends ValidatedControllerAdvice {

    /**
     * Default constructor.
     */
    public AdsValidatedControllerAdvice() {
        super();
    }
}
