package com.yanil.com.np.server.repository;

import com.yanil.com.np.server.entity.Expense;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExpenseEntryRepository extends MongoRepository<Expense, ObjectId>{
}
