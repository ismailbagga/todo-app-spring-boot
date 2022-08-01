package ismail.coding.todoappspring.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ToDoModel {
    private  Long id ;
    private  String taskName ;
    private String task_desc  ;
    private LocalDateTime createdAt ;
    private LocalDateTime updateAt ;
    private ImageModel image ;
    private  Long userId ;
    private  Boolean completed ;
}
