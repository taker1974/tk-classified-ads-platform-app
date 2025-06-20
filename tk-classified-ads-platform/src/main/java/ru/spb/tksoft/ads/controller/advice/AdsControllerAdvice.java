package ru.spb.tksoft.ads.controller.advice;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import ru.spb.tksoft.common.controller.advice.AbstractBaseControllerAdvice;

/**
 * Application-specific exceptions.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AdsControllerAdvice extends AbstractBaseControllerAdvice {

    /**
     * Конструктор по умолчанию.
     */
    private AdsControllerAdvice() {
        super();
    }
}
