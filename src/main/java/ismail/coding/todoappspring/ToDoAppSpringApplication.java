package ismail.coding.todoappspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.*;

//@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@SpringBootApplication()
public class ToDoAppSpringApplication {

	public static void main(String[] args) {

		SpringApplication springApplication =
				new SpringApplication(ToDoAppSpringApplication.class) ;
		springApplication.setAdditionalProfiles("prod");

		ApplicationContext context =  springApplication.run(args) ;

	}

}
