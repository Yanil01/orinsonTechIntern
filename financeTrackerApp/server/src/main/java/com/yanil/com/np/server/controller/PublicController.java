package com.yanil.com.np.server.controller;


import com.yanil.com.np.server.entity.User;
import com.yanil.com.np.server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PublicController {

    private final UserService userService;

    public PublicController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/health-check")
    public String healthCheck(){
        return "Server is running well !";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        userService.saveNewUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
