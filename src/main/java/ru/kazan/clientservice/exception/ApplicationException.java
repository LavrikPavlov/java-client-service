package ru.kazan.clientservice.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@Setter
public class ApplicationException extends RuntimeException{

    private final ExceptionEnum exceptionEnum;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    public ApplicationException(ExceptionEnum exceptionEnum){
        this.exceptionEnum = exceptionEnum;
        this.httpStatus = exceptionEnum.getHttpStatus();
        this.errorMessage = exceptionEnum.getErrorMessage();
    }

    @Override
    public String getMessage() {
        return this.errorMessage;
    }
}
