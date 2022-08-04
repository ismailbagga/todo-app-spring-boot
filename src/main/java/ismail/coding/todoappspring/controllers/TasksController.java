package ismail.coding.todoappspring.controllers;


import ismail.coding.todoappspring.dto.UpdateTaskRequest;
import ismail.coding.todoappspring.exception.ApiRequestException;
import ismail.coding.todoappspring.model.TaskModel;
import ismail.coding.todoappspring.services.TasksService;
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
public class TasksController {

    @Autowired
    private TasksService tasksService;

    @GetMapping("/all")
    private ResponseEntity<?> getAllTasksForUser() {
        var principal = getAuthenticatorPrincipal() ;
        return new ResponseEntity<List<TaskModel>>(
                tasksService.getAllTasksForUser(1L)
                ,HttpStatus.ACCEPTED )  ;
    }
    @GetMapping(value = "/task/{taskId}")
    private ResponseEntity<?> getTask(@PathVariable() Long taskId ) {

        return  new ResponseEntity<TaskModel>(tasksService.getTask(taskId),HttpStatus.ACCEPTED) ;

    } ;
    @PostMapping(
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<?> addToDo(
            @RequestPart("task") TaskModel taskModel,
            @RequestPart("image") MultipartFile file) {

        var principal = getAuthenticatorPrincipal() ;

//            var toDoModel = new ObjectMapper().readValue(todoString,ToDoModel.class) ;
            if (
                    taskModel.getUserId() ==  null  ||
                    ! Objects.equals(taskModel.getUserId(), Objects.requireNonNull(principal).getId())) {
                throw  new ApiRequestException("user Id is missing or taskModel don't belong to the user",
                        HttpStatus.FAILED_DEPENDENCY) ;
            }

            return new ResponseEntity<>(
                    tasksService.saveTask(taskModel,file),
                    HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {

        tasksService.deleteTask(taskId);
        return   ResponseEntity.ok().build() ;

    }
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> patchTask(
            @RequestPart(value = "task") UpdateTaskRequest request ,
            @RequestPart(value = "image",required = false) MultipartFile file
            )  {
        request.setImage(file);
        tasksService.updateTask(request);

        return  ResponseEntity.ok().build() ;
    }
}
