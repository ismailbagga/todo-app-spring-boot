package ismail.coding.todoappspring.dao;

import ismail.coding.todoappspring.model.User;
import ismail.coding.todoappspring.mappers.UserMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class DaoForToDoApp {
    public JdbcTemplate jdbcTemplate ;

    public DaoForToDoApp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findUserByUserName(String username)  {
        String sql = """
               SELECT id , full_name , username , bio , email ,enabled , password ,role 
               FROM app_user 
               WHERE username =  ?
                """ ;
        return jdbcTemplate.query(sql,new UserMapper(),username).stream().findFirst() ;

    };

    public void insertUser(User user) {
        System.out.println("called insert");
        String sql = """
                insert into app_user (full_name,username,bio,email,enabled,password,role) 
                values (?,?,?,?,?,?,?) ;
                
                """ ;
        try {
            jdbcTemplate.update(sql,user.getFullName(),
                    user.getUsername(),user.getBio(),user.getEmail() ,
                    user.isEnabled() ,  user.getPassword() , user.getRole().name()
            ) ;
        }
        catch (Exception e) {
            System.out.printf("\nops cant insert because : %s\n",e.getMessage());
        }
    }
    public List<User> findEmailAndUserName(String username  , String email ) {
        var sql = """
                SELECT * FROM app_user WHERE username = ? OR email = ? ;
                """ ;
        return  jdbcTemplate.query(sql,new UserMapper(),username,email) ;
    }
    public void deleteEverythingInUser() {
        jdbcTemplate.update("delete from app_user ") ;
    }
}
