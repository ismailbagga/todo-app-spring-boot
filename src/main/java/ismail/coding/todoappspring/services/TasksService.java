package ismail.coding.todoappspring.services;
import ismail.coding.todoappspring.dao.DaoForToDoApplication;
import ismail.coding.todoappspring.dto.UpdateTaskRequest;
import ismail.coding.todoappspring.exception.ApiRequestException;
import ismail.coding.todoappspring.model.ImageModel;
import ismail.coding.todoappspring.model.TaskModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ismail.coding.todoappspring.security.AuthenticationUtils.getAuthenticatorPrincipal;

@Service
@Slf4j
public class TasksService {

    private final DaoForToDoApplication daoForToDoApplication;

    @Autowired
    public TasksService(DaoForToDoApplication daoForToDoApplication) {
        this.daoForToDoApplication = daoForToDoApplication;
    }


    public TaskModel saveTask(TaskModel taskModel, MultipartFile file) {
        log.info("save a task");
        try {
            var image =  ImageModel.generateImageModel(file) ;
            if (daoForToDoApplication.findUserById(taskModel.getUserId()).isEmpty())
                throw  new ApiRequestException("user was not found",HttpStatus.EXPECTATION_FAILED);

            daoForToDoApplication.insertTaskWithImage(taskModel,image) ;
        }
        catch ( IllegalStateException e) {
            throw new ApiRequestException(
                    e.getMessage(),
                    HttpStatus.BAD_GATEWAY) ;
        }
        catch (IOException ex) {
            throw new ApiRequestException(
                    "could not parse the file",
                    HttpStatus.INTERNAL_SERVER_ERROR) ;
        }


        return null  ;
    }

    public List<TaskModel> getAllTasksForUser(Long id) {
        return  daoForToDoApplication.selectAllTaskForUser(id) ;


    }

    public TaskModel getTask(Long taskId) {

        Optional<TaskModel> taskModel = daoForToDoApplication.getTaskModel(taskId) ;

        if ( taskModel.isEmpty())
            throw new ApiRequestException("no task was found",HttpStatus.NOT_FOUND) ;

        if (!Objects.equals(getAuthenticatorPrincipal().getId(),taskModel.get().getUserId() ))
            throw new ApiRequestException("accessing a task don't belongs to the user",HttpStatus.BAD_GATEWAY) ;

        return  taskModel.get() ;

    }
    public void deleteTask(Long taskId ) {
        // check if task exits
        Optional<TaskModel> task = daoForToDoApplication.getSimpleTask(taskId) ;
        if ( task.isEmpty())
            throw new ApiRequestException("there is no task with id of "+taskId,HttpStatus.NOT_FOUND) ;

        // check if task belongs to the user
        if (Objects.requireNonNull(getAuthenticatorPrincipal()).getId() != task.get().getUserId() )
            throw new ApiRequestException("accessing a task don't belongs to the user",HttpStatus.BAD_GATEWAY) ;
        //delete  the user
        daoForToDoApplication.deleteTask(taskId);
    }


    @Transactional
    public void  updateTask(UpdateTaskRequest request) {
        //Check if request us null
        if (request.getTaskId() == null)
            throw  new ApiRequestException("request must contain a task id ",HttpStatus.PARTIAL_CONTENT) ;
        // Check if task exists
        Optional<TaskModel> task =
                daoForToDoApplication.getSimpleTask(request.getTaskId()) ;
        if ( task.isEmpty())
            throw new ApiRequestException("there is no task with id of "+request.getTaskId(),HttpStatus.NOT_FOUND) ;
        // check if task belong to the user
        if (Objects.requireNonNull(getAuthenticatorPrincipal()).getId() != task.get().getUserId() )
            throw new ApiRequestException("accessing a task don't belongs to the user",HttpStatus.BAD_GATEWAY) ;

        int  updateTaskCount = 0 ;
        if ( ! request.shouldIUpdateTaskName()) {
            request.setTaskName(task.get().getTaskName());
            updateTaskCount++;
        }
        if(  ! request.shouldIUpdateTaskDesc()) {
             request.setTaskDesc(task.get().getTask_desc());
            updateTaskCount++;

        }
        if (  ! request.shouldIUpdateComplete()) {
            request.setCompleted(task.get().getCompleted());
            updateTaskCount++;
        }
        if ( updateTaskCount != 3 ) {
            log.info("update user task meta data");
            daoForToDoApplication.updateTask(request) ;
        }

        if (! request.shouldIUpdateImage()) return ;
        try {
                var image =  ImageModel.generateImageModel(request.getImage()) ;
                image.setTaskId(task.get().getId());
                log.info("update user image");
                daoForToDoApplication.updateTaskImage(image) ;

        }
        catch ( IllegalStateException e) {
            throw new ApiRequestException(
                    e.getMessage(),
                    HttpStatus.BAD_GATEWAY) ;
        }
        catch (IOException ex) {
            throw new ApiRequestException(
                    "could not parse the file",
                    HttpStatus.INTERNAL_SERVER_ERROR) ;
        }

        // check if there is nothing to update then skip
    }




    ;
}
