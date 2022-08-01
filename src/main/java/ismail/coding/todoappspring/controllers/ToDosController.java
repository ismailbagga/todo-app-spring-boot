package ismail.coding.todoappspring.controllers;


import ismail.coding.todoappspring.exception.ApiRequestException;
import ismail.coding.todoappspring.model.TaskModel;
import ismail.coding.todoappspring.services.ToDoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static ismail.coding.todoappspring.security.AuthenticationUtils.getAuthenticatorPrincipal;

@RestController
@RequestMapping("/api/v1/tasks")
@Slf4j
public class ToDosController {

    @Autowired
    private ToDoService toDoService ;

    @GetMapping("/all")
    private ResponseEntity<?> getAllTasksForUser() {
        var principal = getAuthenticatorPrincipal() ;
        return new ResponseEntity<List<TaskModel>>(
                toDoService.getAllTasksForUser(principal.getId())
                ,HttpStatus.ACCEPTED )  ;
    }
    @GetMapping(value = "/task/{taskId}")
    private ResponseEntity<?> getTask(@PathVariable() Long taskId ) {

        return  new ResponseEntity<TaskModel>(toDoService.getTask(taskId),HttpStatus.ACCEPTED) ;

    } ;



    @PostMapping(
            path = "{userId}" ,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}

    )
    public ResponseEntity<?> addToDo(
            @PathVariable("userId") int userId ,
            @RequestPart("task") TaskModel taskModel,
            @RequestPart("image") MultipartFile file) {

        var principal = getAuthenticatorPrincipal() ;

//            var toDoModel = new ObjectMapper().readValue(todoString,ToDoModel.class) ;
            if (
                    taskModel.getUserId() ==  null  ||
                    ! Objects.equals(taskModel.getUserId(), Objects.requireNonNull(principal).getId())) {
                throw  new ApiRequestException("invalid ",HttpStatus.FAILED_DEPENDENCY) ;
            }

            return new ResponseEntity<>(
                    toDoService.saveTask(taskModel,file),
                    HttpStatus.ACCEPTED);





    }
}
