package ismail.coding.todoappspring.controllers;

import ismail.coding.todoappspring.model.ApplicationUser;
import ismail.coding.todoappspring.services.UserServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/users")
public class ApplicationUserController {
    private final UserServiceImpl userService  ;

    public ApplicationUserController(UserServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping("/public")
    public String getMike() {
        //        String.format()
        return "mike";
    }
    @GetMapping("/private")
    public String getJames() {

        return "james";
    }
    @PostMapping("save")
    public ResponseEntity<?> saveUser(@RequestBody ApplicationUser applicationUser) {
        var tokens = userService.saveUser(applicationUser) ;
        HttpHeaders header = new HttpHeaders()  ;
        header.set("access-token",tokens.getAccessToken());
        header.set("refresh-token",tokens.getRefreshToken());
        return ResponseEntity.ok().headers(header).body("user sign up successfully")  ;
    }







}
