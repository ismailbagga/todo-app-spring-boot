package ismail.coding.todoappspring.security;

import ismail.coding.todoappspring.filters.AuthenticationFilter;
import ismail.coding.todoappspring.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurity  extends  WebSecurityConfigurerAdapter {
    private final UserServiceImpl userServiceImpl ;
    private final  PasswordEncoder passwordEncoder ;
    private final AuthenticationFilter authenticationFilter ;

    @Autowired
    public WebSecurity(UserServiceImpl userServiceImpl, PasswordEncoder passwordEncoder, AuthenticationFilter authenticationFilter) {
        this.userServiceImpl = userServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.authenticationFilter = authenticationFilter;
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
            authenticationFilter.setFilterProcessesUrl("/api/v1/login");
          http.csrf().disable() ;
          http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) ;
          http.authorizeRequests().antMatchers("/api/v1/**").permitAll() ;
          http.authorizeRequests().anyRequest().authenticated() ;
          http.addFilter(authenticationFilter) ;
//          http.addFilterAfter(new AuthorizationFilter(),AuthorizationFilter.class) ;




    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceImpl).passwordEncoder(passwordEncoder) ;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception {
        http.sessionManagement(
                (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ) ;
        http.authorizeRequests(
                (auth) ->  auth.antMatchers("/api/v1/users").permitAll().
                        anyRequest().authenticated()
        ) ;
      return  http.build() ;
    };



}
