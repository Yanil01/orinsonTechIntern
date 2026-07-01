package com.yanil.com.np.server.repository;

import com.yanil.com.np.server.entity.Expense;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExpenseEntryRepository extends MongoRepository<Expense, ObjectId>{

}
