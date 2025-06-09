package ru.spb.tksoft.common.controller.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.spb.tksoft.common.controller.dto.CommonErrorResponseDto;
import ru.spb.tksoft.utils.log.LogEx;
import java.util.Arrays;

/**
 * Processing of very common exception. Order is important! @see @Order()
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@ControllerAdvice
@Order()
public class CommonControllerAdvice extends AbstractBaseControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(CommonControllerAdvice.class);

    /** Default error code. */
    public static final int E_CODE = 160;

    /**
     * Default constructor.
     */
    public CommonControllerAdvice() {
        super();
    }

    /**
     * Default/fallback handler.
     * 
     * @param e Exception.
     * @return Error DTO.
     */
    @ExceptionHandler(Exception.class)
    @Order()
    public ResponseEntity<CommonErrorResponseDto> handleException(Exception e) {

        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, E_CODE,
                e.getMessage());

        return new ResponseEntity<>(
                new CommonErrorResponseDto(E_CODE, e.getMessage(),
                        Arrays.toString(e.getStackTrace())),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /** Error code: RuntimeException. */
    public static final int RTE_CODE = 170;

    /**
     * Handling RuntimeException.
     * 
     * @param e Exception.
     * @return Error DTO.
     */
    @ExceptionHandler(RuntimeException.class)
    @Order(Ordered.LOWEST_PRECEDENCE - 1)
    public ResponseEntity<CommonErrorResponseDto> handleRuntimeException(RuntimeException e) {

        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, RTE_CODE,
                e.getMessage());

        return new ResponseEntity<>(
                new CommonErrorResponseDto(RTE_CODE, e.getMessage(),
                        Arrays.toString(e.getStackTrace())),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /** Error code: NullPointerException. */
    public static final int NPE_CODE = 171;

    /**
     * Handling NullPointerException.
     * 
     * @param e Exception.
     * @return Error DTO.
     */
    @ExceptionHandler(NullPointerException.class)
    @Order(Ordered.LOWEST_PRECEDENCE - 2)
    public ResponseEntity<CommonErrorResponseDto> handleNpe(NullPointerException e) {

        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, NPE_CODE,
                e.getMessage());

        return new ResponseEntity<>(
                new CommonErrorResponseDto(NPE_CODE, e.getMessage(),
                        Arrays.toString(e.getStackTrace())),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /** Error code: IllegalArgumentException. */
    public static final int IAE_CODE = 172;

    /**
     * Handling IllegalArgumentException.
     * 
     * @param e Exception.
     * @return Error DTO.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @Order(Ordered.LOWEST_PRECEDENCE - 3)
    public ResponseEntity<CommonErrorResponseDto> handleIAE(IllegalArgumentException e) {

        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, IAE_CODE,
                e.getMessage());

        return new ResponseEntity<>(
                new CommonErrorResponseDto(IAE_CODE, e.getMessage(),
                        Arrays.toString(e.getStackTrace())),
                HttpStatus.NOT_ACCEPTABLE);
    }
}
