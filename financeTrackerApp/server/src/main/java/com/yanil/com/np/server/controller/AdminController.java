package com.yanil.com.np.server.controller;


import com.yanil.com.np.server.entity.Expense;
import com.yanil.com.np.server.entity.User;
import com.yanil.com.np.server.service.ExpenseEntryService;
import com.yanil.com.np.server.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final ExpenseEntryService expenseEntryService;
    public AdminController(UserService userService, ExpenseEntryService expenseEntryService) {
        this.userService = userService;
        this.expenseEntryService = expenseEntryService;
    }
    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUser();
    }

    @GetMapping("/expenses")
    public List<Expense> getAllExpenses(){
        return expenseEntryService.getAllExpenses();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUserByUsername(@PathVariable ObjectId id){
        userService.deleteUserById(id);
    }


}
