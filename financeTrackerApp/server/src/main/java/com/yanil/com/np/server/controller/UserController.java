package com.yanil.com.np.server.controller;

import com.yanil.com.np.server.entity.User;
import com.yanil.com.np.server.service.UserService;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
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
   private final PasswordEncoder passwordEncoder;
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Data
    public static class IncomeRequest {
        private BigDecimal income;
    }

    @Data
    public static class ChangePasswordRequest {
        private String currentPassword;
        private String newPassword;

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

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ChangePasswordRequest request){
        try{
            User user = userService.getUserByUsername(userDetails.getUsername());
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Current password is incorrect.");
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userService.saveUser(user);
            return new ResponseEntity<>("Password changed successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

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
