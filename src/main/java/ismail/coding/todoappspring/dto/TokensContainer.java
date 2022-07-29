package ismail.coding.todoappspring.dto;

import ismail.coding.todoappspring.jwt.JwtConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;

import static ismail.coding.todoappspring.jwt.JwtConfiguration.*;

@Data
public class TokensContainer {
    private String accessToken ;
    private String refreshToken ;

    public TokensContainer(String accessToken, String refreshToken) {
        this.accessToken = JWT_PREFIX + accessToken;
        this.refreshToken = JWT_PREFIX + refreshToken;
    }
}
