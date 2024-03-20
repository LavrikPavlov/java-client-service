package ru.kazan.clientservice.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@Setter
public class ApplicationException extends RuntimeException{

    private ExceptionEnum exceptionEnum;
    private HttpStatus httpStatus;
    private String errorMessage;

    public ApplicationException(ExceptionEnum exceptionEnum){
        this.exceptionEnum = exceptionEnum;
        this.httpStatus = exceptionEnum.getHttpStatus();
        this.errorMessage = exceptionEnum.getErrorMessage();
    }

    public ApplicationException(ExceptionEnum exceptionEnum, String errorMessage){
        this.exceptionEnum = exceptionEnum;
        this.httpStatus = exceptionEnum.getHttpStatus();
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return this.errorMessage;
    }
}
