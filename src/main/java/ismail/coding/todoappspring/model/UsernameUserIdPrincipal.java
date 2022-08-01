package ismail.coding.todoappspring.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@Builder
@AllArgsConstructor
public class UsernameUserIdPrincipal {
    private Long id ;
    private String username ;

}
