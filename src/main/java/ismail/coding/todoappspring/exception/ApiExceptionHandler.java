package ismail.coding.todoappspring.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice // annotate this class will be serve as class handle multiple Exception
public class ApiExceptionHandler {
    //Exception i want  to handle
    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<?> handleApiRequestException(ApiRequestException apiRequestException) {
        var  apiException =new ApiException( // Simple Class Store Exception Data
                apiRequestException.getMessage() ,
                apiRequestException.getCause() ,
                apiRequestException.getExceptionHttpStatus() ,
                ZonedDateTime.now()
        ) ;
        return  new ResponseEntity<>(apiException,apiRequestException.getExceptionHttpStatus()) ;
    }
}


