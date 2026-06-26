package com.yanil.com.np.server.controller;


import com.yanil.com.np.server.entity.Expense;
import com.yanil.com.np.server.entity.User;
import com.yanil.com.np.server.service.ExpenseEntryService;
import com.yanil.com.np.server.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseEntryController {

    private final ExpenseEntryService expenseEntryService;
    private final UserService userService;

    public ExpenseEntryController(ExpenseEntryService expenseEntryService, UserService userService) {
        this.expenseEntryService = expenseEntryService;
        this.userService = userService;
    }

    @PostMapping("/user/{username}")
    public ResponseEntity<?> saveExpenseByUser(
            @PathVariable String username,
            @RequestBody Expense expense) {
        try {
            expenseEntryService.saveExpense(expense, username);
            return new ResponseEntity<>(expense, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Expense>> getAllExpenses(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Expense> expenses = user.getExpenses();
        if (expenses == null || expenses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getExpenseById(@PathVariable ObjectId id) {
        Expense expense = expenseEntryService.getExpenseById(id);
        if (expense == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(expense, HttpStatus.OK);
    }

    @DeleteMapping("/{username}/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable String username,@PathVariable ObjectId id) {
        Expense expense = expenseEntryService.getExpenseById(id);
        if (expense == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        expenseEntryService.deleteExpenseById(username,id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable ObjectId id, @RequestBody Expense newExpense) {
        Expense oldExpense = expenseEntryService.getExpenseById(id);

        if (oldExpense == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        oldExpense.setTitle(newExpense.getTitle() != null && !newExpense.getTitle().isEmpty() ? newExpense.getTitle() : oldExpense.getTitle());
        oldExpense.setAmount(newExpense.getAmount() != null ? newExpense.getAmount() : oldExpense.getAmount());
        oldExpense.setCategory(newExpense.getCategory() != null && !newExpense.getCategory().isEmpty() ? newExpense.getCategory() : oldExpense.getCategory());
        oldExpense.setDate(LocalDate.now());
        expenseEntryService.saveNewExpense(oldExpense);
        return new ResponseEntity<>(oldExpense, HttpStatus.OK);
    }
}
