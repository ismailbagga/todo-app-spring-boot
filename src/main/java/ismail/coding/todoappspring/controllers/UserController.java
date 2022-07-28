package ismail.coding.todoappspring.controllers;

import ismail.coding.todoappspring.model.User;
import ismail.coding.todoappspring.services.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {
    private final UserServiceImpl userService  ;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping()
    public String getuser() {
        String name = "<h1>mikes 13! </h1>" ;
//        String.format()
        return  name ;
    }
    @PostMapping("save")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        userService.saveUser(user) ;
        return ResponseEntity.accepted().build() ;
    }





}
