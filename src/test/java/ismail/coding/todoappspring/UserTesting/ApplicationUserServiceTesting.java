package ismail.coding.todoappspring.UserTesting;

import ismail.coding.todoappspring.dao.DaoForToDoApplication;
import ismail.coding.todoappspring.enums.Role;
import ismail.coding.todoappspring.jwt.JwtConfiguration;
import ismail.coding.todoappspring.model.ApplicationUser;
import ismail.coding.todoappspring.services.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ApplicationUserServiceTesting {
    ApplicationUser applicationUser1, applicationUser2, applicationUser3;

    @Autowired
    DaoForToDoApplication preUser ;

    @Autowired
    JdbcTemplate jdbcTemplate ;

    UserServiceImpl userService ;

    @Autowired
    DaoForToDoApplication daoForToDoApplication;
    @Autowired
    JwtConfiguration jwtConfiguration;


   @BeforeEach
   public  void beforeAllTests() {
//       preUser.deleteEverythingInUser();
//       daoForToDoApp = new DaoForToDoApp()
       System.out.println("called");
       applicationUser1 = new ApplicationUser("123dsq","mi", "sdqdqs","mike123@gmail.com",true,"sdqdqs", Role.USER) ;
       applicationUser2 = new ApplicationUser("sdqdqs","james", "sdqdqs","james@gmail.com",true,"sdqdqs", Role.USER);
   }


    @Test
    public void test1() {
        System.out.println("test 1");
       when(daoForToDoApplication.findEmailAndUserName(anyString(), anyString())).thenReturn(
               List.of(
                       applicationUser1
               )
       ) ;
       doNothing().when(daoForToDoApplication).insertUser(any());
       ApplicationUser applicationUser = new ApplicationUser("sdqdqs","mik", "sdqdqs","mike123@gmail.com",true,"sdqdqs", Role.USER) ;
       userService.saveUser(applicationUser) ;

   }
    @Test
    public  void test2()  {
        when(daoForToDoApplication.findEmailAndUserName(anyString(), anyString())).thenReturn(
                List.of(
                        applicationUser2
                )
        ) ;
        doNothing().when(daoForToDoApplication).insertUser(any());
        ApplicationUser applicationUser = new ApplicationUser("sdqdqs","james", "sdqdqs","james007@gmail.com",true,"sdqdqs", Role.USER) ;
        userService.saveUser(applicationUser) ;
    }
    @Test
    public  void test3()  {
        when(daoForToDoApplication.findEmailAndUserName(anyString(), anyString())).thenReturn(
                List.of(
                        applicationUser1,
                        applicationUser2
                )
        ) ;
        doNothing().when(daoForToDoApplication).insertUser(any());
        ApplicationUser applicationUser = new ApplicationUser("sdqdqs","james", "sdqdqs","mike123@gmail.com",true,"sdqdqs", Role.USER) ;
        userService.saveUser(applicationUser) ;
    }
    @Test
    public  void test4()  {
//       var daoForToDoApp1 = mock(DaoForToDoApp.class) ;
//       daoForToDoApp1.jdbcTemplate = jdbcTemplate ;
//       var userService1 = new UserServiceImpl(daoForToDoApp1, jwtConfiguration) ;
//        when(daoForToDoApp1.findEmailAndUserName(anyString(), anyString())).thenReturn(
//                List.of(
//                )
//        ) ;
//        ApplicationUser applicationUser = new ApplicationUser("sdqdqs","kim", "sdqdqs","kim@gmail.com",true,"sdqdqs", Role.USER) ;
//        doCallRealMethod().when(daoForToDoApp1).insertUser(any());
//        userService1.saveUser(applicationUser) ;
    }


}
