package ismail.coding.todoappspring.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import ismail.coding.todoappspring.dto.TokensContainer;
import ismail.coding.todoappspring.model.ApplicationUser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@ConfigurationProperties(prefix = "spring.jwt")
@Data
public class JwtConfiguration {
    private String secretKey ;
    private  int expirationDurationInSeconds  ;



    public final  static String JWT_PREFIX= "Bearer " ;
    public final  static String AUTHORIZATION_HEADER = "access_token" ;
    public final static  String ALTERNATIVE_AUTHORIZATION_HEADER = "refresh_token";
    private final static String AUTHORITIES_CLAIM = "authorities" ;



    public TokensContainer generateToken(ApplicationUser applicationUser) {
        var algorithm = Algorithm.HMAC256(secretKey) ;
        log.info("expiration time is {}",expirationDurationInSeconds);
        log.info("secret key is {}",secretKey);
        List<String> authorities =
                applicationUser.getAuthoritiesAsList().stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList()) ;
        String accessToken = JWT.create()
                .withSubject(applicationUser.getUsername())
                .withIssuer("path-/api/v1/login")
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*2)) // 2 hours
                .withClaim("userId", applicationUser.getId())
                .withClaim(AUTHORITIES_CLAIM,authorities)
                .sign(algorithm) ;
        String refreshToken =  JWT.create()
                .withSubject(applicationUser.getUsername())
                .withIssuer("path-/api/v1/login")
                .withExpiresAt(new Date(System.currentTimeMillis()+expirationDurationInSeconds))
                .withClaim(AUTHORITIES_CLAIM,authorities)
                .sign(algorithm) ;
        return new TokensContainer(accessToken,
                refreshToken,
                applicationUser.getUsername());
    }

    public String extractToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER) ;

        if( token == null || !token.startsWith(JWT_PREFIX)) {
            return  null ;
        }
        return  token.replace(JWT_PREFIX,"") ;
    }
    public  DecodedJWT getDecodedJWT(String token ) {
        var algorithm = Algorithm.HMAC256(secretKey) ;

        try {
            JWTVerifier jwtVerifier = JWT.require(algorithm).build() ;
            return jwtVerifier.verify(token) ;
        }
        catch (Exception e) {
            return  null ;
        }

    }
    public  List<SimpleGrantedAuthority> extractAuthorities(DecodedJWT decodedJWT) {
        String[] array =
                decodedJWT.getClaim(AUTHORITIES_CLAIM).asArray(String.class) ;
        return  Arrays.stream(array).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
