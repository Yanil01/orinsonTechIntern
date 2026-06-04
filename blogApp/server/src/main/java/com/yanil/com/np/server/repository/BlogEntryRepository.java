package com.yanil.com.np.server.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.yanil.com.np.server.entity.BlogEntry;

public interface BlogEntryRepository extends MongoRepository<BlogEntry, ObjectId> {

}
