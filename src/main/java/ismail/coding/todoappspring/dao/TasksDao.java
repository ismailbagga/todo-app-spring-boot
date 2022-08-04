package ismail.coding.todoappspring.dao;

import ismail.coding.todoappspring.dto.UpdateTaskRequest;
import ismail.coding.todoappspring.exception.ApiRequestException;
import ismail.coding.todoappspring.mappers.ImageTaskMapper;
import ismail.coding.todoappspring.mappers.TaskMapper;
import ismail.coding.todoappspring.model.ImageModel;
import ismail.coding.todoappspring.model.TaskModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Slf4j
public class TasksDao {
    private  final  JdbcTemplate jdbcTemplate ;
    private final NamedParameterJdbcTemplate namedJdbcTemplate ;


    private final int TASKS_PER_PAGE = 2 ;

    @Autowired
    public TasksDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }


    public void insertTaskWithImage(TaskModel taskModel, ImageModel image) {
        var parameters = new MapSqlParameterSource() ;
        // ------ Image
        parameters.addValue("name",image.getName()) ;
        parameters.addValue("type",image.getType()) ;
        parameters.addValue("image",image.getImageBytes()) ;

        // ------ Task
        parameters.addValue("task_name", taskModel.getTaskName()) ;
        parameters.addValue("task_desc", taskModel.getTask_desc()) ;
        parameters.addValue("user_id", taskModel.getUserId()) ;
        parameters.addValue("completed", taskModel.getCompleted()) ;
        var sqlOp1 = """
                  with userID as ( 
                  
                  INSERT INTO todo(task_name,task_desc,user_id,completed) 
                  VALUES (:task_name,:task_desc,:user_id,:completed) returning id 
                  ) 
                         
                    INSERT INTO images(name,type,image,task_id) 
                    values (:name,:type,:image,(SELECT * FROM userID) )  ;  
                """ ;
        var sqlOp2 = """
                
              INSERT INTO todo(task_name,task_desc,user_id,completed) 
                VALUES (:task_name,:task_desc,:user_id,:completed)   ;
                INSERT INTO images(name,type,image,task_id) 
                values (:name,:type,:image, (SELECT lastval() ))  ;         
     
                """ ;
        try {
            namedJdbcTemplate.update(sqlOp1,parameters) ;

        }
        catch (Exception e) {
            throw  new ApiRequestException(e.getMessage(),HttpStatus.I_AM_A_TEAPOT) ;
        }


    }

    public List<TaskModel> selectAllTaskForUser(Long id,int page) {
        var sql = """
           SELECT * FROM todo as td 
           JOIN images as img ON  img.task_id = td.id
           WHERE user_id = ? 
           OFFSET  ?
           LIMIT ?
           ;
                """ ;

        return jdbcTemplate.query(sql,new ImageTaskMapper(8),id,page*TASKS_PER_PAGE,TASKS_PER_PAGE);

    }

    public Optional<TaskModel> getTaskModel(Long taskId) {
        var sql = """
                SELECT * FROM  todo td 
                JOIN images ON td.id = task_id 
                WHERE td.id = ? ;
            """ ;

        return  jdbcTemplate.query(sql,new ImageTaskMapper(8), taskId).stream().findFirst()  ;
    }
    public Optional<TaskModel> getSimpleTask(Long taskId) {
        var sql = """
                SELECT * FROM  todo 
                WHERE id = ? ;
            """ ;

        return  jdbcTemplate.query(sql,new TaskMapper(), taskId).stream().findFirst()  ;

    } ;
    public void deleteTask(Long taskId) {
        var sql = """
                DELETE FROM todo WHERE  id = ?
                """;
        jdbcTemplate.update(sql,taskId);

    }

    public void updateTask(UpdateTaskRequest request) {
        var sql = """
                UPDATE todo SET task_name = ? , task_desc = ? , completed = ? ,
                updated_at = CURRENT_TIMESTAMP WHERE id = ? ;
                """ ;
        jdbcTemplate.update(sql,request.getTaskName(),request.getTaskDesc(),request.getCompleted(),request.getTaskId()) ;
    }

    public void updateTaskImage(ImageModel image) {
        var sql = """
                UPDATE images SET name = ? ,  type = ? , image = ?  
                WHERE task_id = ? ;         
                """ ;
        jdbcTemplate.update(sql,image.getName(),image.getType(),image.getImageBytes(),image.getTaskId()) ;
    }


}
