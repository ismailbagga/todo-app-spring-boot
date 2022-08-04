package ismail.coding.todoappspring.dao;

import ismail.coding.todoappspring.dto.UpdateUserProfile;
import ismail.coding.todoappspring.exception.ApiRequestException;
import ismail.coding.todoappspring.mappers.UserMapper;
import ismail.coding.todoappspring.model.ApplicationUser;
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
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
@Repository
@Slf4j
public class UsersDao {
    private  final JdbcTemplate jdbcTemplate ;
    private final NamedParameterJdbcTemplate namedJdbcTemplate ;


    private final int TASKS_PER_PAGE = 2 ;

    @Autowired
    public UsersDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedJdbcTemplate) {
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
                SELECT * FROM app_user WHERE  
                    username = ? 
                    OR 
                    email = ? ;
                """ ;

        return  jdbcTemplate.query(sql,new UserMapper(),username,email) ;
    }
    public List<ApplicationUser> findEmailAndUserNameExcludingUser(String username  , String email,Long userId) {
        var sql = """
                SELECT * FROM app_user WHERE  
                    ( username = ? 
                    OR 
                    email = ? ) 
                    AND id <> ? ;
                    """ ;

        return  jdbcTemplate.query(sql,new UserMapper(),username,email,userId) ;

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
    public int updateUser(UpdateUserProfile newProfile, Long userId) {
        var sql = """
                UPDATE app_user SET
                full_name = CASE WHEN cast(:fullName as varchar) IS NULL THEN full_name ELSE :fullName END ,
                username = CASE WHEN cast(:username as varchar) IS NULL THEN username ELSE :username END ,
                email = CASE WHEN cast(:email as varchar) IS NULL THEN email ELSE :email END ,
                bio = CASE WHEN cast(:bio as varchar) IS NULL THEN bio ELSE :bio END
                WHERE id =  :userId; 
                """ ;
        var parameters = new MapSqlParameterSource() ;
        parameters.addValue("fullName",newProfile.getFullName()) ;
        parameters.addValue("username",newProfile.getUsername()) ;
        parameters.addValue("email",newProfile.getEmail()) ;
        parameters.addValue("bio",newProfile.getBio()) ;
        parameters.addValue("userId",userId) ;

        return namedJdbcTemplate.update(sql,parameters);
    }



}
