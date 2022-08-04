package ismail.coding.todoappspring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest{
    private String fullName ;
    private String username ;
    private String bio ;
    private String email ;

}
