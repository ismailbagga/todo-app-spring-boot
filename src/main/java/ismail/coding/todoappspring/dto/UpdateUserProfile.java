package ismail.coding.todoappspring.dto;

import ismail.coding.todoappspring.exception.ApiRequestException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class UpdateUserProfile {
    private String fullName ;
    private String username ;
    private String bio ;
    private String email ;
    private String  confirmationPassword ;

    public boolean shouldIIgnore() {
        if ( confirmationPassword == null) throw  new ApiRequestException("no confirmation password was sent", HttpStatus.BAD_REQUEST);
        return  fullName == null && username == null && bio == null && email == null  ;
    }

}
