package com.yanil.com.np.server.controller;

import com.yanil.com.np.server.entity.User;
import com.yanil.com.np.server.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/users")
public class UserController {
   private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable ObjectId id){
        User user = userService.getUserById(id);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.FOUND);
    }
    @PostMapping("/{username}/{income}")
    public ResponseEntity<?> setUserSalary(@PathVariable String username, @PathVariable BigDecimal income){
        User user = userService.getUserByUsername(username);
        if(user!=null){
            user.setIncome(income.add(user.getIncome()));
            return new ResponseEntity<>(user.getIncome(),HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable ObjectId id){
        User user = userService.getUserById(id);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
