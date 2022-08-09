package ismail.coding.todoappspring.services;
import ismail.coding.todoappspring.dao.TasksDao;
import ismail.coding.todoappspring.dao.UsersDao;
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

import static ismail.coding.todoappspring.config.AuthenticationUtils.getAuthenticatorPrincipal;

@Service
@Slf4j
public class TasksService {

    private final TasksDao tasksDao;
    private final UsersDao usersDao ;

    @Autowired
    public TasksService(TasksDao tasksDao, UsersDao usersDao) {
        this.tasksDao = tasksDao;
        this.usersDao = usersDao;
    }


    public TaskModel saveTask(TaskModel taskModel, MultipartFile file) {
        log.info("save a task");
        try {
            var image =  ImageModel.generateImageModel(file) ;
            if (usersDao.findUserById(taskModel.getUserId()).isEmpty())
                throw  new ApiRequestException("user was not found",HttpStatus.EXPECTATION_FAILED);

            tasksDao.insertTaskWithImage(taskModel,image) ;
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

    public List<TaskModel> getAllTasksForUser(Long id,int page) {
        return  tasksDao.selectAllTaskForUser(id,page) ;


    }

    public TaskModel getTask(Long taskId) {

        Optional<TaskModel> taskModel = tasksDao.getTaskModel(taskId) ;

        if ( taskModel.isEmpty())
            throw new ApiRequestException("no task was found",HttpStatus.NOT_FOUND) ;

        if (!Objects.equals(getAuthenticatorPrincipal().getId(),taskModel.get().getUserId() ))
            throw new ApiRequestException("accessing a task don't belongs to the user",HttpStatus.BAD_GATEWAY) ;

        return  taskModel.get() ;

    }
    public void deleteTask(Long taskId ) {
        // check if task exits
        Optional<TaskModel> task = tasksDao.getSimpleTask(taskId) ;
        if ( task.isEmpty())
            throw new ApiRequestException("there is no task with id of "+taskId,HttpStatus.NOT_FOUND) ;

        // check if task belongs to the user
        if (Objects.requireNonNull(getAuthenticatorPrincipal()).getId() != task.get().getUserId() )
            throw new ApiRequestException("accessing a task don't belongs to the user",HttpStatus.BAD_GATEWAY) ;
        //delete  the user
        tasksDao.deleteTask(taskId);
    }


    @Transactional
    public void  updateTask(UpdateTaskRequest request) {
        //Check if request us null
        if (request.getTaskId() == null)
            throw  new ApiRequestException("request must contain a task id ",HttpStatus.PARTIAL_CONTENT) ;
        // Check if task exists
        Optional<TaskModel> task =
                tasksDao.getSimpleTask(request.getTaskId()) ;
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
            tasksDao.updateTask(request) ;
        }

        if (! request.shouldIUpdateImage()) return ;
        try {
                var image =  ImageModel.generateImageModel(request.getImage()) ;
                image.setTaskId(task.get().getId());
                log.info("update user image");
                tasksDao.updateTaskImage(image) ;

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
