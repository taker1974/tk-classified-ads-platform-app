package ru.spb.tksoft.common.controller.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import jakarta.validation.ConstraintViolationException;
import ru.spb.tksoft.common.controller.dto.CommonErrorResponseDto;
import ru.spb.tksoft.utils.log.LogEx;
import java.util.Arrays;

/**
 * Processing validation exceptions. Order is important! @see @Order()
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
@ControllerAdvice
@Order()
public class ValidatedControllerAdvice extends AbstractBaseControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(ValidatedControllerAdvice.class);

    /**
     * Default constructor.
     */
    public ValidatedControllerAdvice() {
        super();
    }

    /** Error code: ConstraintViolationException. */
    public static final int CVE_CODE = 613;

    /**
     * Handling ConstraintViolationException.
     * 
     * @param e Exception.
     * @return Error DTO.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CommonErrorResponseDto> handleConstraintViolation(
            ConstraintViolationException e) {

        final String message = "Validation error: " +
                e.getConstraintViolations().stream()
                        .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                        .toList();

        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, CVE_CODE,
                e.getMessage(), message);

        return new ResponseEntity<>(
                new CommonErrorResponseDto(CVE_CODE, e.getMessage(), message),
                HttpStatus.BAD_REQUEST);
    }

    /** Error code: MissingServletRequestParameterException. */
    public static final int MSP_CODE = 785;

    /**
     * Handling MissingServletRequestParameterException.
     * 
     * @param e Exception.
     * @return Error DTO.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CommonErrorResponseDto> handleMissingParams(
            MissingServletRequestParameterException e) {

        LogEx.error(log, LogEx.getThisMethodName(), LogEx.EXCEPTION_THROWN, MSP_CODE,
                e.getMessage());

        return new ResponseEntity<>(
                new CommonErrorResponseDto(MSP_CODE, e.getMessage(),
                        Arrays.toString(e.getStackTrace())),
                HttpStatus.BAD_REQUEST);
    }
}
