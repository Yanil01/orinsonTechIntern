package com.yanil.com.np.taskManagerApp.repository;

import com.yanil.com.np.taskManagerApp.entity.TaskEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskEntryRepository extends MongoRepository<TaskEntry, ObjectId> {

}
