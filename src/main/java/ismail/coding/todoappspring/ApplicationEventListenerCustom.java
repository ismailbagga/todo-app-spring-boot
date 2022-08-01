package ismail.coding.todoappspring;

import ismail.coding.todoappspring.dao.DaoForToDoApp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationEventListenerCustom {
    @Autowired
    DaoForToDoApp daoForToDoApp ;
    @Autowired
    PasswordEncoder passwordEncoder ;
    @EventListener(ContextRefreshedEvent.class)
    public void onStart() {

//        log.info("application started");
//        log.info("creating test user");
//        daoForToDoApp.insertUser( new User(
//                "mike jay",
//                "mike007" ,
//                "dsqdsqdsq" ,
//                "mike@007" ,
//                true ,
//                passwordEncoder.encode("123") ,
//                Role.USER
//
//        )) ;
    }
    @EventListener(ContextClosedEvent.class)
    public void onClose() {
        log.info("Application Closed");
//        log.info("deleting everything in app user table");
//        daoForToDoApp.deleteAllUsers() ;
    }


}
