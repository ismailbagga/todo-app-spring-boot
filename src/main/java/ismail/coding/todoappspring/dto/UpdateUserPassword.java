package ismail.coding.todoappspring.dto;

import lombok.Data;

@Data
public class UpdateUserPassword {
    private String oldPassword ;
    private String newPassword ;

}
