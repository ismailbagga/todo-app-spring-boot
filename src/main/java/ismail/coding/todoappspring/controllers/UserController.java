package ismail.coding.todoappspring.controllers;

import ismail.coding.todoappspring.dto.UpdateUserPasswordRequest;
import ismail.coding.todoappspring.dto.UpdateUserProfileRequest;
import ismail.coding.todoappspring.dto.UserRequest;
import ismail.coding.todoappspring.model.ApplicationUser;
import ismail.coding.todoappspring.services.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getMike() {
//        System.out.println("request have been sent");

        return ResponseEntity.ok().body(new StringResponse("request have been sent"));
    }
    @GetMapping("/private")
    public String getJames() {
        return "private";
    }
    @PostMapping(value = "save",produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<?> saveUser(@RequestBody ApplicationUser applicationUser) {
        var tokens = userService.saveUser(applicationUser) ;
        HttpHeaders header = new HttpHeaders()  ;
        HttpCookie cookie = new HttpCookie("access-token",tokens.getRefreshToken()) ;
        header.set("access-token",tokens.getAccessToken());
        header.set("refresh-token",tokens.getRefreshToken());
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
