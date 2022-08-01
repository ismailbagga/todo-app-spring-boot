package ismail.coding.todoappspring.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import ismail.coding.todoappspring.jwt.JwtConfiguration;
import ismail.coding.todoappspring.model.UsernameUserIdPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    private final  JwtConfiguration jwtConfiguration;


    public AuthorizationFilter(JwtConfiguration jwtConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token =  jwtConfiguration.extractToken(request) ;
        if ( token == null) {
            log.info("could not pass authorization filter");
            filterChain.doFilter(request,response);
            return ;
        }

        DecodedJWT decodedJWT = jwtConfiguration.getDecodedJWT(token) ;
        if ( decodedJWT == null)  {
            log.info("Fake Token");
            throw  new ServletException("fake token") ;
        }

        var principal = new UsernameUserIdPrincipal(
                Long.parseLong(decodedJWT.getClaim("userId").toString()),
                decodedJWT.getSubject() );
        List<SimpleGrantedAuthority> authorities =
                jwtConfiguration.extractAuthorities(decodedJWT) ;
        var authToken =
                new UsernamePasswordAuthenticationToken(principal,null,authorities) ;
        SecurityContextHolder.getContext().setAuthentication(authToken);
        log.info("user is authenticated");
        filterChain.doFilter(request,response);
    }
}
