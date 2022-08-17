package ismail.coding.todoappspring.config;


import ismail.coding.todoappspring.jwt.JwtConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

import static ismail.coding.todoappspring.jwt.JwtConfiguration.ALTERNATIVE_AUTHORIZATION_HEADER;
import static ismail.coding.todoappspring.jwt.JwtConfiguration.AUTHORIZATION_HEADER;

@Configuration
public class WebConfig extends WebMvcAutoConfiguration implements  WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins("http://localhost:4200/")
                .allowedMethods("POST","GET","UPDATE","DELETE","OPTION","PATCH")
                .allowedHeaders(AUTHORIZATION_HEADER,"Content-Type")
                .exposedHeaders(AUTHORIZATION_HEADER,ALTERNATIVE_AUTHORIZATION_HEADER)
//                .exposedHeaders("access-token","username","refresh-token")
                .allowCredentials(false);
//                .allowedOrigins("http://localhost:4200")
//                .allowedHeaders("access-token","Content-Type","Cache-Control")
//                .exposedHeaders("access-token")
//                .allowCredentials(true)
//                .allowedMethods("POST","GET","PATCH","UPDATE","DELETE","OPTION")
        ;
    }
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        final CorsConfiguration config = new CorsConfiguration();
//
//        config.setAllowedOrigins(List.of("http://localhost:4200"));
//        config.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
//        config.setAllowCredentials(true);
//        config.addExposedHeader("access-token");
//        config.setAllowedHeaders(Arrays.asList("access-token", "Cache-Control", "Content-Type"));
//
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//
//        return source;
//    }
}