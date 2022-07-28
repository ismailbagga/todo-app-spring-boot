package ismail.coding.todoappspring.UserTesting;

import ismail.coding.todoappspring.dao.DaoForToDoApp;
import ismail.coding.todoappspring.enums.Role;
import ismail.coding.todoappspring.exception.ApiRequestException;
import ismail.coding.todoappspring.model.User;
import ismail.coding.todoappspring.services.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTesting {
    User user1 , user2  , user3;

    @Autowired
    DaoForToDoApp preUser ;

    @Autowired
    JdbcTemplate jdbcTemplate ;

    UserServiceImpl userService ;

    @Autowired
    DaoForToDoApp daoForToDoApp ;


   @BeforeEach
   public  void beforeAllTests() {
//       preUser.deleteEverythingInUser();
//       daoForToDoApp = new DaoForToDoApp()
       System.out.println("called");
       user1 = new User("123dsq","mi", "sdqdqs","mike123@gmail.com",true,"sdqdqs", Role.USER) ;
       user2 = new User("sdqdqs","james", "sdqdqs","james@gmail.com",true,"sdqdqs", Role.USER);
   }


    @Test
    public void test1() {
        System.out.println("test 1");
       when(daoForToDoApp.findEmailAndUserName(anyString(), anyString())).thenReturn(
               List.of(
                       user1
               )
       ) ;
       doNothing().when(daoForToDoApp).insertUser(any());
       User user = new User("sdqdqs","mik", "sdqdqs","mike123@gmail.com",true,"sdqdqs", Role.USER) ;
       userService.saveUser(user) ;

   }
    @Test
    public  void test2()  {
        when(daoForToDoApp.findEmailAndUserName(anyString(), anyString())).thenReturn(
                List.of(
                        user2
                )
        ) ;
        doNothing().when(daoForToDoApp).insertUser(any());
        User user = new User("sdqdqs","james", "sdqdqs","james007@gmail.com",true,"sdqdqs", Role.USER) ;
        userService.saveUser(user) ;
    }
    @Test
    public  void test3()  {
        when(daoForToDoApp.findEmailAndUserName(anyString(), anyString())).thenReturn(
                List.of(
                        user1,
                        user2
                )
        ) ;
        doNothing().when(daoForToDoApp).insertUser(any());
        User user = new User("sdqdqs","james", "sdqdqs","mike123@gmail.com",true,"sdqdqs", Role.USER) ;
        userService.saveUser(user) ;
    }
    @Test
    public  void test4()  {
       var daoForToDoApp1 = mock(DaoForToDoApp.class) ;
       daoForToDoApp1.jdbcTemplate = jdbcTemplate ;
       var userService1 = new UserServiceImpl(daoForToDoApp1) ;
        when(daoForToDoApp1.findEmailAndUserName(anyString(), anyString())).thenReturn(
                List.of(
                )
        ) ;
        User user = new User("sdqdqs","kim", "sdqdqs","kim@gmail.com",true,"sdqdqs", Role.USER) ;
        doCallRealMethod().when(daoForToDoApp1).insertUser(any());
        userService1.saveUser(user) ;
    }


}
