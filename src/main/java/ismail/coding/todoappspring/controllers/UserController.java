package ismail.coding.todoappspring.controllers;

import ismail.coding.todoappspring.model.User;
import ismail.coding.todoappspring.services.UserServiceImpl;
import org.springframework.http.HttpHeaders;
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
    public String getuser1() {
        //        String.format()
        return "<h1>mikes 13! </h1>";
    }
    @GetMapping("/private")
    public String getuser2() {

        return "<h1>mikes 13! </h1>";
    }
    @PostMapping("save")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        var tokens = userService.saveUser(user) ;
        HttpHeaders header = new HttpHeaders()  ;
        header.set("access-token",tokens.getAccessToken());
        header.set("refresh-token",tokens.getRefreshToken());
        return ResponseEntity.ok().headers(header).body("user sign up successfully")  ;


    }





}
