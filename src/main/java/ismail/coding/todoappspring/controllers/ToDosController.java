package ismail.coding.todoappspring.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import ismail.coding.todoappspring.exception.ApiRequestException;
import ismail.coding.todoappspring.model.ImageModel;
import ismail.coding.todoappspring.model.ToDoModel;
import ismail.coding.todoappspring.services.ToDoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

import static ismail.coding.todoappspring.security.AuthenticationUtils.getAuthenticatorUsername;

@RestController
@RequestMapping("/api/v1/todo")
@Slf4j
public class ToDosController {

    @Autowired
    private ToDoService toDoService ;

    @PostMapping(
            path = "{userId}" ,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}

    )
    public ResponseEntity<?> addToDo(
            @PathVariable("userId") int userId ,
            @RequestPart("task") ToDoModel toDoModel  ,
            @RequestPart("image") MultipartFile file) {

        var principal = getAuthenticatorUsername() ;
        try {
//            var toDoModel = new ObjectMapper().readValue(todoString,ToDoModel.class) ;
            if (
                    toDoModel.getUserId() ==  null  ||
                    ! Objects.equals(toDoModel.getUserId(), Objects.requireNonNull(principal).getId())) {
                throw  new ApiRequestException("invalid ",HttpStatus.FAILED_DEPENDENCY) ;
            }
            var image =  ImageModel.generateImageModel(file) ;
            toDoModel.setImage(image);
            return new ResponseEntity<>(toDoService.saveTask(toDoModel), HttpStatus.ACCEPTED);

        } catch (IOException e) {
            log.info("Error  parsing file : {} ",e.getMessage());
            return ResponseEntity.badRequest().build() ;
        }



    }
}
