package ismail.coding.todoappspring.dto;

import ismail.coding.todoappspring.jwt.JwtConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;

import static ismail.coding.todoappspring.jwt.JwtConfiguration.*;

@Data
public class TokensContainer {
    private final String accessToken ;
    private final  String refreshToken ;
    private final  String username ;

    public TokensContainer(String accessToken,
                           String refreshToken,
                           String username ) {
        this.accessToken = JWT_PREFIX + accessToken;
        this.refreshToken = JWT_PREFIX + refreshToken;
        this.username = username ;
    }
}
