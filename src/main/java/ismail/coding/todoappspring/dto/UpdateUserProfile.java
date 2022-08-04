package ismail.coding.todoappspring.dto;

import lombok.Data;

@Data
public class UpdateUserProfile {
    private String fullName ;
    private String username ;
    private String bio ;
    private String email ;

}
