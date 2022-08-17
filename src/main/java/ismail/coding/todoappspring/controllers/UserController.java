package ismail.coding.todoappspring.controllers;

import ismail.coding.todoappspring.config.AuthenticationUtils;
import ismail.coding.todoappspring.dto.UpdateUserPasswordRequest;
import ismail.coding.todoappspring.dto.UpdateUserProfileRequest;
import ismail.coding.todoappspring.dto.UserRequest;
import ismail.coding.todoappspring.jwt.JwtConfiguration;
import ismail.coding.todoappspring.model.ApplicationUser;
import ismail.coding.todoappspring.services.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static ismail.coding.todoappspring.jwt.JwtConfiguration.ALTERNATIVE_AUTHORIZATION_HEADER;
import static ismail.coding.todoappspring.jwt.JwtConfiguration.AUTHORIZATION_HEADER;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {
    private final UserServiceImpl userService  ;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }
    @AllArgsConstructor
    private  class StringResponse {
        public  String response ;
    }




    @GetMapping("/public")
    public ResponseEntity<?> get() {
        System.out.println("request have been sent Get");

        return ResponseEntity.ok().body(new StringResponse("request have been sent"));
    }
    @PostMapping("/public")
    public ResponseEntity<?> post() {
        System.out.println("request have been sent Post");

        return ResponseEntity.ok().body(new StringResponse("request have been sent"));
    }
    @GetMapping("/authentication/state")
    public ResponseEntity<?> isAuthenticated() {


        return ResponseEntity.ok().body(AuthenticationUtils.isAuthenticatedRequest());
    }
    @PostMapping(value = "save",produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<?> saveUser(@RequestBody ApplicationUser applicationUser) {
        System.out.println("save user");
        var tokens = userService.saveUser(applicationUser) ;
        HttpHeaders header = new HttpHeaders()  ;
        HttpCookie cookie = new HttpCookie(AUTHORIZATION_HEADER,tokens.getRefreshToken()) ;
        header.set(AUTHORIZATION_HEADER,tokens.getAccessToken());
        header.set(ALTERNATIVE_AUTHORIZATION_HEADER,tokens.getRefreshToken());
        header.set(HttpHeaders.SET_COOKIE,cookie.toString());
        return ResponseEntity.ok().headers(header) .build() ;
    }
    @GetMapping()
    public ResponseEntity<?> getUser() {
        return  new ResponseEntity<UserRequest>(userService.findUser(), HttpStatus.ACCEPTED) ;
    }
    @PatchMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody UpdateUserProfileRequest updateUserProfileRequest) {
        userService.updateUser(updateUserProfileRequest) ;
        return  ResponseEntity.ok().build() ;
    }
    @PatchMapping("/password")
    public ResponseEntity<?> updateUserPassword(@RequestBody UpdateUserPasswordRequest updateUserPasswordRequest) {
        userService.changePassword(updateUserPasswordRequest); ;
        return  ResponseEntity.ok().build() ;
    }







}
