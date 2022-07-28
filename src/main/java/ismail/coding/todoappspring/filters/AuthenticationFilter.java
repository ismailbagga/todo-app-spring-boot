package ismail.coding.todoappspring.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import ismail.coding.todoappspring.dto.TokensContainer;
import ismail.coding.todoappspring.dto.UsernamePasswordAuthRequest;
import ismail.coding.todoappspring.jwt.JwtAuthenticationGenerator;
import ismail.coding.todoappspring.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    AuthenticationManager authenticationManager  ;
    JwtAuthenticationGenerator jwtAuthenticationGenerator ;

    @Autowired
    public AuthenticationFilter(
            AuthenticationManager authenticationManager
            , JwtAuthenticationGenerator jwtAuthenticationGenerator) {
        this.authenticationManager = authenticationManager ;
        this.jwtAuthenticationGenerator = jwtAuthenticationGenerator ;

    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            ServletInputStream inputStream = request.getInputStream();

            UsernamePasswordAuthRequest usernamePasswordAuthRequest =
                    new ObjectMapper().readValue(inputStream,UsernamePasswordAuthRequest.class) ;
            var token =
                    new UsernamePasswordAuthenticationToken(usernamePasswordAuthRequest.getUsername(),usernamePasswordAuthRequest.getPassword()) ;

            return getAuthenticationManager().authenticate(token) ;
        } catch (IOException e) {
            throw  new RuntimeException("failed to map request to username password request") ;
        }

    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal() ; // because loadUsersByUsername() return User as UserDetail
        // so i can up cast ;
        TokensContainer tokens  =  jwtAuthenticationGenerator.generateToken(user) ;

        response.addHeader("access-token",tokens.getAccessToken());
        response.addHeader("refresh-token",tokens.getRefreshToken());
        log.info("Successful Authentication");

    }
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        //   TODO :  OPTIONAL  ban user after 20 authentication


        log.info("Unsuccessful Authentication");
    }


}