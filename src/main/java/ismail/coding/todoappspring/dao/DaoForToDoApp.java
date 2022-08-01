package ismail.coding.todoappspring.dao;

import ismail.coding.todoappspring.exception.ApiRequestException;
import ismail.coding.todoappspring.model.ApplicationUser;
import ismail.coding.todoappspring.mappers.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Repository
@Slf4j
public class DaoForToDoApp {
    public JdbcTemplate jdbcTemplate ;


    public DaoForToDoApp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
                    var ps =   con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) ;
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
}
