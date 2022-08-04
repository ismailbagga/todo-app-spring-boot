package ismail.coding.todoappspring;


import ismail.coding.todoappspring.dao.TasksDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PreExecuter implements CommandLineRunner {

    @Autowired
    TasksDao tasksDao;

    @Autowired
    PasswordEncoder passwordEncoder ;


    @Override
    public void run(String... args) throws Exception {
       log.info("run Command line Runner");

    }
}
