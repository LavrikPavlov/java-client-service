package ru.kazan.clientservice.handler;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.kazan.clientservice.dto.exception.ExceptionResponse;
import ru.kazan.clientservice.exception.ApplicationException;
import ru.kazan.clientservice.exception.ExceptionEnum;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String REASON_OF_EXCEPTION = "An invalid request with error [ {} ] was rejected because: {}";

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> catchApplicationException(ApplicationException e,
                                                                   HttpServletRequest request){

        ExceptionEnum exceptionEnum = e.getExceptionEnum();
        ExceptionResponse response = new ExceptionResponse(
                request.getRequestURI(),
                exceptionEnum.getHttpStatus().value(),
                exceptionEnum.getHttpStatus().getReasonPhrase(),
                exceptionEnum.getErrorMessage(),
                LocalDateTime.now()
        );

        log.error(REASON_OF_EXCEPTION,response.getError(), response.getMessage());
        return new ResponseEntity<>(response, e.getHttpStatus());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            NullPointerException.class})
    public ResponseEntity<ExceptionResponse> catchMessageNotReadableException(HttpServletRequest request) {
        ExceptionEnum exceptionMessage = ExceptionEnum.UNSUPPORTED_MEDIA_TYPE;
        ExceptionResponse response = getExceptionResponse(request, exceptionMessage);
        return new ResponseEntity<>(response, exceptionMessage.getHttpStatus());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> catchMethodNotAllowed(HttpServletRequest request) {
        ExceptionEnum exceptionMessage = ExceptionEnum.METHOD_NOT_ALLOWED;
        ExceptionResponse response = getExceptionResponse(request, exceptionMessage);
        return new ResponseEntity<>(response, exceptionMessage.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> catchOtherException(HttpServletRequest request) {
        ExceptionEnum exceptionMessage = ExceptionEnum.INTERNAL_SERVER_ERROR;
        ExceptionResponse response = getExceptionResponse(request, exceptionMessage);
        return new ResponseEntity<>(response, exceptionMessage.getHttpStatus());
    }

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ExceptionResponse> catchMethodArgumentTypeMismatchException(HttpServletRequest request) {
        ExceptionEnum exceptionMessage = ExceptionEnum.BAD_REQUEST;
        ExceptionResponse response = getExceptionResponse(request, exceptionMessage);
        return new ResponseEntity<>(response, exceptionMessage.getHttpStatus());
    }

    private ExceptionResponse getExceptionResponse(HttpServletRequest request, ExceptionEnum exceptionEnum){

        ExceptionResponse response = new ExceptionResponse(
                request.getRequestURI(),
                exceptionEnum.getHttpStatus().value(),
                exceptionEnum.getHttpStatus().getReasonPhrase(),
                exceptionEnum.getErrorMessage(),
                LocalDateTime.now()
        );

        log.error(REASON_OF_EXCEPTION, response.getError(), response.getMessage());

        return response;
    }
}
