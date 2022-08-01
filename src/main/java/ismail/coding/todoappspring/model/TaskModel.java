package ismail.coding.todoappspring.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskModel {
    private  Long id ;
    private  String taskName ;
    private String task_desc  ;
    private Date createdAt ;
    private Date updateAt ;
    private ImageModel imageModel ;
//    private ImageModel image ;
    private  Long userId ;
    private  Boolean completed ;

    public TaskModel(Long id,
                     String taskName,
                     String task_desc,
                     Date createdAt,
                     Date updateAt,
                     Long userId, Boolean completed) {
        this.id = id;
        this.taskName = taskName;
        this.task_desc = task_desc;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
        this.userId = userId;
        this.completed = completed;
    }
}
