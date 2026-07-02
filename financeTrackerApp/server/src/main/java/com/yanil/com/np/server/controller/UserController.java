package com.yanil.com.np.server.controller;

import com.yanil.com.np.server.entity.User;
import com.yanil.com.np.server.service.UserService;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/users")
public class UserController {
   private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Data
    public static class IncomeRequest {
        private BigDecimal income;
    }


    @GetMapping
    public ResponseEntity<User> getUserById(@AuthenticationPrincipal UserDetails userDetails){
        User user = userService.getUserByUsername(userDetails.getUsername());
        if(user == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/income")
    public ResponseEntity<?> setUserSalary(
            @RequestBody IncomeRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.getUserByUsername(userDetails.getUsername());

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (user.getIncome() == null) {
            user.setIncome(BigDecimal.ZERO);
        }

        if (request == null || request.getIncome() == null) {
            return new ResponseEntity<>("Income cannot be null", HttpStatus.BAD_REQUEST);
        }

        user.setIncome(user.getIncome().add(request.getIncome()));
        if (user.getRemainingAmount() == null){
            user.setRemainingAmount(user.getIncome());
        }else {
            user.setRemainingAmount(user.getRemainingAmount().add(request.getIncome()));
        }
        userService.saveUser(user);

        return new ResponseEntity<>(user.getIncome(), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal UserDetails userDetails){
        User user = userService.getUserByUsername(userDetails.getUsername());
        if(user == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.deleteUserById(user.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
