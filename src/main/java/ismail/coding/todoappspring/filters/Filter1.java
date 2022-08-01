package ismail.coding.todoappspring.filters;

import ismail.coding.todoappspring.exception.ApiRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
public class Filter1 extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      log.info("the first filter");
      String parameter = request.getParameter("filter1") ;
      if (parameter == null) {
          log.info("could not passed the first filter");
          filterChain.doFilter(request,response); // Move to the next filter in chain or to api endpoint
          // Since the parameter is null probably the filter is not responsible for validating the request
          return;
      }
      if ( parameter.equals("faked")) {

            throw  new ApiRequestException("faked token", HttpStatus.BAD_REQUEST); // Block Request
      }
      log.info("the first filter is passed");
      log.info("move to the next filter");
      filterChain.doFilter(request,response); // Move to the next filter or  to api endpoint


    }
}
