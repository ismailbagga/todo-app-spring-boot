package ismail.coding.todoappspring.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import ismail.coding.todoappspring.dto.TokensContainer;
import ismail.coding.todoappspring.dto.UsernamePasswordAuthRequest;
import ismail.coding.todoappspring.jwt.JwtConfiguration;
import ismail.coding.todoappspring.model.ApplicationUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
import java.util.Arrays;

import static ismail.coding.todoappspring.jwt.JwtConfiguration.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    AuthenticationManager authenticationManager  ;
    JwtConfiguration jwtConfiguration;

    public AuthenticationFilter(
            AuthenticationManager authenticationManager
            , JwtConfiguration jwtConfiguration) {
        this.authenticationManager = authenticationManager ;
        this.jwtConfiguration = jwtConfiguration;

    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            ServletInputStream inputStream = request.getInputStream();

//            Arrays.stream(request.getCookies()).forEach(System.out::println);  ;
            UsernamePasswordAuthRequest usernamePasswordAuthRequest =
                    new ObjectMapper().readValue(inputStream,UsernamePasswordAuthRequest.class) ;
            var token =
                    new UsernamePasswordAuthenticationToken(usernamePasswordAuthRequest.getUsername(),usernamePasswordAuthRequest.getPassword()) ;

            return authenticationManager.authenticate(token) ;
        } catch (IOException e) {
            throw  new RuntimeException("failed to map request to username password request filter") ;
        }

    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        ApplicationUser applicationUser = (ApplicationUser) authResult.getPrincipal() ;
        TokensContainer tokens  =  jwtConfiguration.generateToken(applicationUser) ;
        response.addHeader(AUTHORIZATION_HEADER,tokens.getAccessToken());
        response.addHeader(ALTERNATIVE_AUTHORIZATION_HEADER,tokens.getRefreshToken());
        response.addHeader("username", tokens.getUsername());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),tokens);


    }
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        response.setStatus(HttpStatus.FORBIDDEN.value());

        log.info("Unsuccessful Authentication");
    }


}