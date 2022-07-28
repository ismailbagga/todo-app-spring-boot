package ismail.coding.todoappspring.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        log.info("Going through Authorization Filter");
        filterChain.doFilter(request,response);
    }
}
