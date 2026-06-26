package com.yanil.com.np.server.service;

import com.yanil.com.np.server.entity.Expense;
import com.yanil.com.np.server.entity.User;
import com.yanil.com.np.server.repository.ExpenseEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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

    @Transactional
    public void saveExpense(Expense expense, String username) throws IllegalAccessException {
        User user = userService.getUserByUsername(username);
        expense.setDate(LocalDate.now());
        if(expense.getAmount().compareTo(user.getRemainingAmount())>0){
            throw new IllegalAccessException("Insufficient remaining amount");
        }
        Expense save = expenseEntryRepository.save(expense);
        user.getExpenses().add(save);
        user.setRemainingAmount(user.getRemainingAmount().subtract(expense.getAmount()));
        userService.saveUser(user);
    }

    public Expense getExpenseById(ObjectId id){
        return expenseEntryRepository.findById(id).orElse(null);
    }

    public void deleteExpenseById(String username, ObjectId id){
        User user = userService.getUserByUsername(username);
        user.getExpenses().removeIf(x -> x.getId().equals(id));
        userService.saveUser(user);
        expenseEntryRepository.deleteById(id);
    }

}
