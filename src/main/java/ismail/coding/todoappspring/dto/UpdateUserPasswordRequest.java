package ismail.coding.todoappspring.dto;

import lombok.Data;

@Data
public class UpdateUserPasswordRequest {
    private String oldPassword ;
    private String newPassword ;

}
