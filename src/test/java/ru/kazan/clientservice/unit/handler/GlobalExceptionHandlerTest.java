package ru.kazan.clientservice.unit.handler;


import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.kazan.clientservice.dto.exception.ExceptionResponse;
import ru.kazan.clientservice.exception.ApplicationException;
import ru.kazan.clientservice.exception.ExceptionEnum;
import ru.kazan.clientservice.handler.GlobalExceptionHandler;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
    }

    @Test
    void catchApplicationException() {
        ApplicationException exception = new ApplicationException(
                ExceptionEnum.NOT_FOUND,
                "Our exception"
        );
        when(request.getRequestURI()).thenReturn("/test");
        ResponseEntity<ExceptionResponse> responseEntity = exceptionHandler.catchApplicationException(exception, request);

        assertEquals(404, responseEntity.getStatusCode().value());
        assertEquals("Not Found", Objects.requireNonNull(responseEntity.getBody()).getType());
        assertEquals("Our exception", responseEntity.getBody().getMessage());
        assertEquals("/test", responseEntity.getBody().getUrl());
    }


    @Test
    void catchMethodNotAllowed() {
        when(request.getRequestURI()).thenReturn("/test");
        ResponseEntity<ExceptionResponse> responseEntity = exceptionHandler.catchMethodNotAllowed(request);

        assertEquals(405, responseEntity.getStatusCode().value());
        assertEquals("Method Not Allowed", Objects.requireNonNull(responseEntity.getBody()).getType());
        assertEquals("Метод не разрешен. " +
                        "Сервер знает о запрашиваемом методе, но он был деактивирован и не может быть использован.",
                responseEntity.getBody().getMessage());
        assertEquals("/test", responseEntity.getBody().getUrl());
    }

    @Test
    void catchMessageNotReadableException() {
        when(request.getRequestURI()).thenReturn("/test");
        ResponseEntity<ExceptionResponse> responseEntity = exceptionHandler.catchMessageNotReadableException(request);

        assertEquals(415, responseEntity.getStatusCode().value());
        assertEquals("Unsupported Media Type", Objects.requireNonNull(responseEntity.getBody()).getType());
        assertEquals("Формат запрашиваемых данных не поддерживается сервером, поэтому запрос отклонён.",
                responseEntity.getBody().getMessage());
        assertEquals("/test", responseEntity.getBody().getUrl());
    }

    @Test
    void catchOtherException() {
        when(request.getRequestURI()).thenReturn("/test");
        ResponseEntity<ExceptionResponse> responseEntity = exceptionHandler.catchOtherException(request);

        assertEquals(500, responseEntity.getStatusCode().value());
        assertEquals("Internal Server Error", Objects.requireNonNull(responseEntity.getBody()).getType());
        assertEquals("Внутренняя ошибка сервера. Подождите несколько минут и попробуйте снова.",
                responseEntity.getBody().getMessage());
        assertEquals("/test", responseEntity.getBody().getUrl());
    }
}