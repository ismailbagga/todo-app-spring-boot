package ismail.coding.todoappspring.services;

import ismail.coding.todoappspring.model.ToDoModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ToDoService {

    public ToDoModel saveTask(ToDoModel toDoModel) {
        log.info("save a task");
        return null  ;
    }

}
