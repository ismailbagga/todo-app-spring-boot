package ismail.coding.todoappspring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokensContainer {
    private String accessToken ;
    private String refreshToken ;
}
