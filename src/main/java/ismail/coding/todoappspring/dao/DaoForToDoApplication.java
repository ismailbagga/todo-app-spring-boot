package ismail.coding.todoappspring.dao;

import ismail.coding.todoappspring.dto.UpdateTaskRequest;
import ismail.coding.todoappspring.exception.ApiRequestException;
import ismail.coding.todoappspring.mappers.ImageTaskMapper;
import ismail.coding.todoappspring.mappers.TaskMapper;
import ismail.coding.todoappspring.model.ApplicationUser;
import ismail.coding.todoappspring.mappers.UserMapper;
import ismail.coding.todoappspring.model.ImageModel;
import ismail.coding.todoappspring.model.TaskModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.sql.Statement.*;


@Repository
@Slf4j
public class DaoForToDoApplication {
    private  final  JdbcTemplate jdbcTemplate ;
    private final NamedParameterJdbcTemplate namedJdbcTemplate ;

    @Autowired
    public DaoForToDoApplication(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }
    public Optional<ApplicationUser> findUserByUserName(String username)  {
        String sql = """
               SELECT id , full_name , username , bio , email ,enabled , password ,role 
               FROM app_user 
               WHERE username =  ?
                """ ;
        return jdbcTemplate.query(sql,new UserMapper(),username).stream().findFirst() ;
    };
    public Long insertUser(ApplicationUser applicationUser) {
        log.info("called insert");
        String sql = """
                insert into app_user (full_name,username,bio,email,enabled,password,role) 
                values (?,?,?,?,?,?,?) returning id ;
                    """ ;
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder() ;
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    var ps =   con.prepareStatement(sql, RETURN_GENERATED_KEYS) ;
                    ps.setString(1,applicationUser.getFullName()) ;
                    ps.setString(2,applicationUser.getUsername());
                    ps.setString(3,applicationUser.getBio());
                    ps.setString(4,applicationUser.getEmail() );
                    ps.setBoolean(5,applicationUser.isEnabled() );
                    ps.setString(6,applicationUser.getPassword() );
                    ps.setString(7,applicationUser.getRole().name());
                    return ps ;
                }
            },keyHolder) ;
            log.info("user generated id is : {}",keyHolder.getKey());
            return  Objects.requireNonNull(keyHolder.getKey()).longValue()  ;
        }
        catch (Exception e) {
            throw  new ApiRequestException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR) ;
        }
    }
    public List<ApplicationUser> findEmailAndUserName(String username  , String email ) {
        var sql = """
                SELECT * FROM app_user WHERE username = ? OR email = ? ;
                """ ;
        return  jdbcTemplate.query(sql,new UserMapper(),username,email) ;
    }
    public void deleteAllUsers() {
        jdbcTemplate.update("delete from app_user ") ;
    }

    public Optional<ApplicationUser> findUserById(Long id ) {
        var sql = """
                    SELECT * FROM app_user 
                    WHERE id = ? ; 
                """ ;
        return jdbcTemplate.query(sql,new UserMapper(),id).stream().findFirst();
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

    public List<TaskModel> selectAllTaskForUser(Long id) {
        var sql = """
           SELECT * FROM todo as td 
           JOIN images as img ON  img.task_id = td.id
           where user_id = ? ;
                """ ;

        return jdbcTemplate.query(sql,new ImageTaskMapper(8),id);

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
                updated_at = CURRENT_TIMESTAMP WHERE id = ?
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
