package ismail.coding.todoappspring.dto;

import ismail.coding.todoappspring.model.ImageModel;
import ismail.coding.todoappspring.model.TaskModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateTaskRequest {
    private Long taskId ;
    private  String taskName ;
    private  String task_desc ;
    private  Boolean completed ;
    private MultipartFile image ;

    public boolean shouldIUpdateTaskName() {
        return this.taskName != null ;
    }
    public boolean shouldIUpdateTaskDesc() {
        return this.task_desc != null  ;
    }
    public boolean shouldIUpdateComplete() {
        return  this.completed != null ;
    }

    public boolean shouldIUpdateImage() {
        return  image != null  ;
    }





}
