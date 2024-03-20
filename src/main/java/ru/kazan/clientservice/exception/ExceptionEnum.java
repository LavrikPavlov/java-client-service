package ru.kazan.clientservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {

    /**
     * @HttpStatus is 400
     */

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Некорректный запрос. Проверте данные и попробуйте еще раз",
            "badRequestException"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Странница не найдена.",
            "notFoundException"),
    REQUEST_TIMEOUT(HttpStatus.REQUEST_TIMEOUT,"Время ожидания запроса истекло.",
            "requestTimeoutException"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,
            "Ошибка авторизации. Для доступа требуется аутентификация.",
            "unauthorizedException"),
    FORBIDDEN(HttpStatus.FORBIDDEN,
            "Запрещено. Отсутствуют права доступа к содержимому.",
            "forbiddenException"),

    /**
     * @HttpStatus is 500
     */

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,
            "Внутренняя ошибка сервера. Подождите несколько минут и попробуйте снова.",
            "internalServerError"),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE,
            "Сервер временно недоступен по техническим причинам. Попробуйте позже.",
            "serviceUnavailable");


    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final String exception;

}
