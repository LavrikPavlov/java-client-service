package ru.kazan.clientservice.dto.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {

    String url;
    Integer error;
    String type;
    String message;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:SSSS")
    LocalDateTime timestamp;
    
}
