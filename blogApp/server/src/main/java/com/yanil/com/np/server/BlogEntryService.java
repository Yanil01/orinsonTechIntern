package com.yanil.com.np.server;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yanil.com.np.server.entity.BlogEntry;
import com.yanil.com.np.server.repository.BlogEntryRepository;

@Component
public class BlogEntryService {

    @Autowired
    private BlogEntryRepository blogEntryRepository;

    public void save(BlogEntry blogEntry) {
        blogEntryRepository.save(blogEntry);
    }

    public List<BlogEntry> getAll() {
        return blogEntryRepository.findAll();
    }

    public BlogEntry getById(ObjectId id) {
        return blogEntryRepository.findById(id).orElse(null);
    }

    public void deleteById(ObjectId id) {
        blogEntryRepository.deleteById(id);
    }
}
