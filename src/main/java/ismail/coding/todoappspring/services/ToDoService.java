package ismail.coding.todoappspring.services;
import ismail.coding.todoappspring.dao.DaoForToDoApp;
import ismail.coding.todoappspring.exception.ApiRequestException;
import ismail.coding.todoappspring.model.ApplicationUser;
import ismail.coding.todoappspring.model.ImageModel;
import ismail.coding.todoappspring.model.ToDoModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class ToDoService {

    private final DaoForToDoApp daoForToDoApp  ;

    @Autowired
    public ToDoService(DaoForToDoApp daoForToDoApp) {
        this.daoForToDoApp = daoForToDoApp;
    }


    public ToDoModel saveTask(ToDoModel toDoModel, MultipartFile file) {
        log.info("save a task");
        try {
            var image =  ImageModel.generateImageModel(file) ;
            if (daoForToDoApp.findUserById(toDoModel.getUserId()).isEmpty())
                throw  new ApiRequestException("user was not found",HttpStatus.EXPECTATION_FAILED);

            daoForToDoApp.insertTaskWithImage(toDoModel,image) ;
        }
        catch (Exception e) {
            log.debug("Ops Exception msg : {}",e.getMessage());
        }
//        catch ( IllegalStateException e) {
//            throw new ApiRequestException(
//                    e.getMessage(),
//                    HttpStatus.BAD_GATEWAY) ;
//        }
//        catch (IOException ex) {
//            throw new ApiRequestException(
//                    "could not parse the file",
//                    HttpStatus.INTERNAL_SERVER_ERROR) ;
//        }


        return null  ;
    }

}
