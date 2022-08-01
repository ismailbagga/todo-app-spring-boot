package ismail.coding.todoappspring.dao;

import ismail.coding.todoappspring.exception.ApiRequestException;
import ismail.coding.todoappspring.model.ApplicationUser;
import ismail.coding.todoappspring.mappers.UserMapper;
import ismail.coding.todoappspring.model.ImageModel;
import ismail.coding.todoappspring.model.ToDoModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.sql.Statement.*;


@Repository
@Slf4j
public class DaoForToDoApp {
    private  final  JdbcTemplate jdbcTemplate ;
    private final NamedParameterJdbcTemplate namedJdbcTemplate ;

    @Autowired
    public DaoForToDoApp(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedJdbcTemplate) {
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
//            return jdbcTemplate.update(sql,
//                    applicationUser.getFullName(),
//                    applicationUser.getUsername(),
//                    applicationUser.getBio(),
//                    applicationUser.getEmail() ,
//                    applicationUser.isEnabled() ,
//                    applicationUser.getPassword() ,
//                    applicationUser.getRole().name()
//            ) ;
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


    public void insertTaskWithImage(ToDoModel toDoModel, ImageModel image) {
        var parameters = new MapSqlParameterSource() ;
        // ------ Image
        parameters.addValue("name",image.getName()) ;
        parameters.addValue("type",image.getType()) ;
        parameters.addValue("image",image.getImageBytes()) ;
//        parameters.addValue("task_id",image.getTaskId()) ;
        // ------ Task
        parameters.addValue("task_name",toDoModel.getTaskName()) ;
        parameters.addValue("task_desc",toDoModel.getTask_desc()) ;
        parameters.addValue("user_id",toDoModel.getUserId()) ;
        parameters.addValue("completed",toDoModel.getCompleted()) ;
        var sql = """
                INSERT INTO todo(task_name,task_desc,user_id,completed) 
                VALUES (:task_name,:task_desc,:user_id,:completed)   ;
                INSERT INTO images(name,type,image,task_id) 
                values (:name,:type,:image, (SELECT lastval() ))  ;         
     
                """ ;
        try {
                    namedJdbcTemplate.update(
                            sql,parameters) ;
//                    jdbcTemplate.update(
//                            (con -> {
//                                var ps = con.prepareStatement(sql, RETURN_GENERATED_KEYS);
////                To DO Parameters
//                                ps.setString(1, toDoModel.getTaskName());
//                                ps.setString(2, toDoModel.getTask_desc());
//                                ps.setLong(3, toDoModel.getUserId());
//                                ps.setBoolean(4, toDoModel.getCompleted());
////                Image Parameters
//                                ps.setString(5, image.getName());
//                                ps.setString(6, image.getType());
//                                ps.setBytes(7, image.getImageBytes());
//
//                                return ps;
//                            }));
        }
        catch (Exception e) {
            throw  e ;
//            throw  new ApiRequestException(e.getMessage(),HttpStatus.I_AM_A_TEAPOT) ;
        }


    }
}
