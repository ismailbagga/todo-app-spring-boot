package ismail.coding.todoappspring.services;

import ismail.coding.todoappspring.exception.ApiRequestException;
import ismail.coding.todoappspring.model.User;
import ismail.coding.todoappspring.dao.DaoForToDoApp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements  UserService , UserDetailsService {
    public final DaoForToDoApp daoToDoApp ;


    @Autowired
    public UserServiceImpl(DaoForToDoApp daoToDoApp) {
        this.daoToDoApp = daoToDoApp;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = daoToDoApp.findUserByUserName(username) ;
        if (user.isEmpty()) throw new UsernameNotFoundException("no user was found") ;
        log.info("user was found ");
        return user.get();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return  new BCryptPasswordEncoder(10) ;
    }


    public void saveUser(User user) {
        String username = user.getUsername();
        String email = user.getEmail()  ;
        List<User> list =
                daoToDoApp.findEmailAndUserName(username,email);
        System.out.println(list.size());

        if (list.size() > 0 ) {
            if ( (list.size() == 2)
                    ||
                    (list.get(0).getUsername().equals(username) && list.get(0).getEmail().equals(email) ) )
                    throw  new ApiRequestException("username and email have been used",HttpStatus.MULTI_STATUS) ;

             if ( list.get(0).getUsername().equals(username)) throw  new ApiRequestException("username is already used",HttpStatus.CONFLICT) ;
             if ( list.get(0).getEmail().equals(email)) throw  new ApiRequestException("email is already used",HttpStatus.CONFLICT) ;
         }
        user.setPassword(passwordEncoder().encode(user.getPassword()));
         daoToDoApp.insertUser(user);


    }
}
