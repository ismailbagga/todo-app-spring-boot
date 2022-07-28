package ismail.coding.todoappspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;

//@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@SpringBootApplication()
public class ToDoAppSpringApplication {

	public static void main(String[] args) {


	ApplicationContext context =  SpringApplication.run(ToDoAppSpringApplication.class, args);
	var bean = context.getBean(PasswordEncoder.class) ;
		System.out.printf("the bean is %s" , bean);
	}

}
