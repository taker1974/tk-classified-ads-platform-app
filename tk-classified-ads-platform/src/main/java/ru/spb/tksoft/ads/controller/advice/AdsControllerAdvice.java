package ru.spb.tksoft.ads.controller.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.spb.tksoft.ads.exception.TkAdBaseException;
import ru.spb.tksoft.ads.exception.TkAdNotFoundException;
import ru.spb.tksoft.ads.exception.TkAdNotOwnedException;
import ru.spb.tksoft.ads.exception.TkCommentNotFoundException;
import ru.spb.tksoft.ads.exception.TkCommentNotOwnedException;
import ru.spb.tksoft.ads.exception.TkDeletingMediaException;
import ru.spb.tksoft.ads.exception.TkMediaNotFoundException;
import ru.spb.tksoft.ads.exception.TkNullArgumentException;
import ru.spb.tksoft.ads.exception.TkSavingMediaException;
import ru.spb.tksoft.ads.exception.TkSizeException;
import ru.spb.tksoft.ads.exception.TkUnsupportedMediaTypeException;
import ru.spb.tksoft.ads.exception.TkUserExistsException;
import ru.spb.tksoft.ads.exception.TkUserNotFoundException;
import ru.spb.tksoft.common.controller.advice.AbstractBaseControllerAdvice;
import ru.spb.tksoft.common.controller.dto.CommonErrorResponseDto;
import ru.spb.tksoft.utils.log.LogEx;

/**
 * Application-specific exceptions.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AdsControllerAdvice extends AbstractBaseControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(AdsControllerAdvice.class);

    private static final int ORDER_BASE = 100;

    /**
     * Default constructor.
     */
    private AdsControllerAdvice() {
        super();
    }

    /**
     * Exceptions that cause the error "NOT_FOUND"
     * 
     * @param e Exception.
     * @return Response entity.
     */
    @ExceptionHandler({
            TkAdNotFoundException.class, TkCommentNotFoundException.class,
            TkMediaNotFoundException.class, TkUserNotFoundException.class})
    @Order(Ordered.LOWEST_PRECEDENCE - ORDER_BASE - 1)
    public ResponseEntity<CommonErrorResponseDto> handleNotFound(TkAdBaseException e) {

        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN,
                e.getCode(), e.getMessage());

        return new ResponseEntity<>(
                new CommonErrorResponseDto(e.getCode(), e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    /**
     * Exceptions that cause the error "FORBIDDEN"
     * 
     * @param e Exception.
     * @return Response entity.
     */
    @ExceptionHandler({TkAdNotOwnedException.class, TkCommentNotOwnedException.class})
    @Order(Ordered.LOWEST_PRECEDENCE - ORDER_BASE - 2)
    public ResponseEntity<CommonErrorResponseDto> handleForbidden(TkAdBaseException e) {

        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN,
                e.getCode(), e.getMessage());

        return new ResponseEntity<>(
                new CommonErrorResponseDto(e.getCode(), e.getMessage()),
                HttpStatus.FORBIDDEN);
    }

    /**
     * Exceptions that cause the error "BAD_REQUEST"
     * 
     * @param e Exception.
     * @return Response entity.
     */
    @ExceptionHandler({TkNullArgumentException.class,
            TkSizeException.class, TkUnsupportedMediaTypeException.class,
            TkUserExistsException.class})
    @Order(Ordered.LOWEST_PRECEDENCE - ORDER_BASE - 3)
    public ResponseEntity<CommonErrorResponseDto> handleBadRequest(TkAdBaseException e) {

        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN,
                e.getCode(), e.getMessage());

        return new ResponseEntity<>(
                new CommonErrorResponseDto(e.getCode(), e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Exceptions that cause the error "INTERNAL_SERVER_ERROR"
     * 
     * @param e Exception.
     * @return Response entity.
     */
    @ExceptionHandler({TkDeletingMediaException.class,
            TkSavingMediaException.class})
    @Order(Ordered.LOWEST_PRECEDENCE - ORDER_BASE - 3)
    public ResponseEntity<CommonErrorResponseDto> handleInternalError(TkAdBaseException e) {

        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN,
                e.getCode(), e.getMessage());

        return new ResponseEntity<>(
                new CommonErrorResponseDto(e.getCode(), e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
