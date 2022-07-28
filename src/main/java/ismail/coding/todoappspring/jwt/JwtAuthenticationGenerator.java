package ismail.coding.todoappspring.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import ismail.coding.todoappspring.dto.TokensContainer;
import ismail.coding.todoappspring.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
public class JwtAuthenticationGenerator {
    @Value("{spring.jwt.secret-key}")
    private String secretKey ;
    @Value("{spring.jwt.expiration-time}")
    private  int expirationDurationInSeconds  ;
    
    
    public TokensContainer generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey) ;
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withIssuer("path-/api/v1/login")
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*10))
                .withIssuedAt(Instant.from(LocalDateTime.now()))
                .withClaim("authorities",user.getAuthoritiesAsList())
                .sign(algorithm) ;
        String refreshToken =  JWT.create()
                .withSubject(user.getUsername())
                .withIssuer("path-/api/v1/login")
                .withExpiresAt(new Date(System.currentTimeMillis()+expirationDurationInSeconds))
                .withIssuedAt(Instant.from(LocalDateTime.now()))
                .withClaim("authorities",user.getAuthoritiesAsList())
                .sign(algorithm) ;
        return new TokensContainer(accessToken,refreshToken);
    }
}
