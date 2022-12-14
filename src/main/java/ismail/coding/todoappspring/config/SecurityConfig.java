package ismail.coding.todoappspring.config;

import ismail.coding.todoappspring.filters.*;
import ismail.coding.todoappspring.jwt.JwtConfiguration;
import ismail.coding.todoappspring.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends  WebSecurityConfigurerAdapter {
    private final UserServiceImpl userServiceImpl ;
    private final  PasswordEncoder passwordEncoder ;
    private final JwtConfiguration jwtConfiguration;

    @Autowired
    public SecurityConfig(UserServiceImpl userServiceImpl, PasswordEncoder passwordEncoder, JwtConfiguration jwtConfiguration) {
        this.userServiceImpl = userServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfiguration = jwtConfiguration;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        var authenticationFilter = new AuthenticationFilter(authenticationManagerBean(), jwtConfiguration) ;
        var authorizationFilter = new AuthorizationFilter(jwtConfiguration) ;
        authenticationFilter.setFilterProcessesUrl("/api/v1/users/login");
        Filter1 filter1 = new Filter1() ;
        Filter2 filter2 = new Filter2() ;
        Filter3 filter3 = new Filter3() ;

        http.cors() ;
        http.csrf().disable() ;
        http.authorizeRequests()
//                .antMatchers("/api/v1/tasks/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v1/users/authentication/state")
                    .permitAll()
                .antMatchers("/api/v1/users/save")
                    .permitAll()
                .antMatchers("/api/v1/login")
                    .permitAll()
                .antMatchers("/api/v1/users/public")
                    .permitAll() ;
        http.authorizeRequests().anyRequest().authenticated() ;


        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) ;

        http.addFilter(authenticationFilter) ;
        http.addFilterAfter(new Filter1(), UsernamePasswordAuthenticationFilter.class) ;
        http.addFilterBefore(authorizationFilter,Filter1.class) ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceImpl).passwordEncoder(passwordEncoder) ;
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        final CorsConfiguration configuration =
//                new CorsConfiguration() ;
//        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
//        configuration.setAllowedMethods(List.of("GET"));
//        configuration.setAllowCredentials(true);
//        configuration.setAllowedHeaders(List.of("Authorization","Cash-Control","Content-Type"));
//        var source = new UrlBasedCorsConfigurationSource() ;
//        source.registerCorsConfiguration("/**",configuration);
//        return  source ;
//    }

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
