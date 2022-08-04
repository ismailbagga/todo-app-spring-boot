package ismail.coding.todoappspring.controllers;

import ismail.coding.todoappspring.dto.UpdateUserProfile;
import ismail.coding.todoappspring.dto.UserRequest;
import ismail.coding.todoappspring.model.ApplicationUser;
import ismail.coding.todoappspring.services.UserServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {
    private final UserServiceImpl userService  ;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping("/public")
    public String getMike() {
        //        String.format()
        return "public";
    }
    @GetMapping("/private")
    public String getJames() {
        return "private";
    }
    @PostMapping("save")
    public ResponseEntity<?> saveUser(@RequestBody ApplicationUser applicationUser) {
        var tokens = userService.saveUser(applicationUser) ;
        HttpHeaders header = new HttpHeaders()  ;
        header.set("access-token",tokens.getAccessToken());
        header.set("refresh-token",tokens.getRefreshToken());
        return ResponseEntity.ok().headers(header).body("user sign up successfully")  ;
    }
    @GetMapping()
    public ResponseEntity<?> getUser() {
        return  new ResponseEntity<UserRequest>(userService.findUser(), HttpStatus.ACCEPTED) ;
    }
    @PatchMapping()
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserProfile updateUserProfile) {
        userService.updateUser(updateUserProfile) ;
        return  ResponseEntity.ok().build() ;
    }







}
