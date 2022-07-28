package ismail.coding.todoappspring.exception;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ApiRequestException extends  RuntimeException{
    @Getter
    private final HttpStatus exceptionHttpStatus ;

    public ApiRequestException(String message, HttpStatus status) {
        super(message);
        this.exceptionHttpStatus = status ;
    }
    public ApiRequestException(String message, Throwable cause,HttpStatus status) {
        super(message, cause);
        this.exceptionHttpStatus = status ;

    }
}
