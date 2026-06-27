package com.yanil.com.np.server.service;

import com.yanil.com.np.server.entity.Expense;
import com.yanil.com.np.server.entity.User;
import com.yanil.com.np.server.repository.ExpenseEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseEntryService {


    private final ExpenseEntryRepository expenseEntryRepository;
    private final UserService userService;


    public ExpenseEntryService(ExpenseEntryRepository expenseEntryRepository, UserService userService) {
        this.expenseEntryRepository = expenseEntryRepository;
        this.userService = userService;
    }


    public List<Expense> getAllExpenses(){
        return expenseEntryRepository.findAll();
    }

    public void saveNewExpense(Expense expense){
        expenseEntryRepository.save(expense);
    }


    public void saveExpense(Expense expense, String username) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (expense.getAmount().compareTo(user.getRemainingAmount()) > 0) {
            throw new IllegalArgumentException("Insufficient remaining amount");
        }
        expense.setDate(LocalDate.now());
        Expense saved = expenseEntryRepository.save(expense);
        user.getExpenses().add(saved);
        user.setRemainingAmount(
                user.getRemainingAmount().subtract(expense.getAmount())
        );
        userService.saveUser(user);
    }
    public Expense getExpenseById(ObjectId id){
        return expenseEntryRepository.findById(id).orElse(null);
    }

    public void deleteExpenseById(String username, ObjectId id){
        User user = userService.getUserByUsername(username);
        Optional<Expense> expense = expenseEntryRepository.findById(id);
        if (user == null ){
            throw new IllegalArgumentException("User not found");

        }
        if(expense.isEmpty()){
            throw new IllegalArgumentException("Expense not found");
        }
        user.setRemainingAmount(user.getRemainingAmount().add(expense.get().getAmount()));
        user.getExpenses().removeIf(x -> x.getId().equals(id));
        userService.saveUser(user);
        expenseEntryRepository.deleteById(id);
    }
    public Expense updateExpenseById(String username, ObjectId id, Expense newExpense){
        User user = userService.getUserByUsername(username);
        Expense oldExpense = getExpenseById(id);
        if(user.getExpenses().stream().anyMatch(e->e.getId().equals(id))){
            BigDecimal previousAmount = oldExpense.getAmount();
            BigDecimal currentAmount = newExpense.getAmount()!=null?newExpense.getAmount():oldExpense.getAmount();
            BigDecimal netIncrease =currentAmount.subtract(previousAmount);
            if (netIncrease.compareTo(BigDecimal.ZERO) > 0 &&
                    netIncrease.compareTo(user.getRemainingAmount()) > 0) {
                throw new IllegalArgumentException("Insufficient remaining amount");
            }
            oldExpense.setAmount(currentAmount);
            oldExpense.setTitle(newExpense.getTitle()!=null && !newExpense.getTitle().isEmpty()?newExpense.getTitle():oldExpense.getTitle());
            oldExpense.setCategory(newExpense.getCategory()!=null&&!newExpense.getCategory().isEmpty()?newExpense.getCategory():oldExpense.getCategory());
            oldExpense.setDate(LocalDate.now());
            saveNewExpense(oldExpense);
            user.setRemainingAmount(user.getRemainingAmount().subtract(netIncrease));
            userService.saveUser(user);
            return oldExpense;
        }
        throw new IllegalArgumentException("Expense not found for user: " + username + " and id: " + id);


    }

}
