package com.yanil.com.np.server.controller;


import com.yanil.com.np.server.entity.Expense;
import com.yanil.com.np.server.service.ExpenseEntryService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseEntryController {

    private final ExpenseEntryService expenseEntryService;

    public ExpenseEntryController(ExpenseEntryService expenseEntryService) {
        this.expenseEntryService = expenseEntryService;
    }

    @PostMapping("/user")
    public ResponseEntity<?> saveExpenseByUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Expense expense) {
        try {
            expenseEntryService.saveExpense(expense, userDetails.getUsername());
            return new ResponseEntity<>(expense, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/user")
    public ResponseEntity<List<Expense>> getAllExpenses(@AuthenticationPrincipal UserDetails userDetails) {

        List<Expense> expenses = expenseEntryService.getExpensesByUsername(userDetails.getUsername());
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpenseById(@PathVariable ObjectId id, @AuthenticationPrincipal UserDetails userDetails) {
        try{
            expenseEntryService.deleteExpenseById(userDetails.getUsername(), id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@AuthenticationPrincipal UserDetails userDetails,@PathVariable ObjectId id, @RequestBody Expense newExpense) {
        try{
            Expense updateExpense = expenseEntryService.updateExpenseById(userDetails.getUsername(),id,newExpense);
            return new ResponseEntity<>(updateExpense, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }


    }
}
