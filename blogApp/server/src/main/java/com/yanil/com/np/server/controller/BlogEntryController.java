package com.yanil.com.np.server.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yanil.com.np.server.BlogEntryService;
import com.yanil.com.np.server.entity.BlogEntry;

@RestController
@RequestMapping("/blogs")
@CrossOrigin(origins = "http://localhost:5173")
public class BlogEntryController {
    @Autowired
    private BlogEntryService blogEntryService;

    @GetMapping
    public ResponseEntity<List<BlogEntry>> getBlogs() {
        List<BlogEntry> blogs = blogEntryService.getAll();
        if (blogs == null || blogs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBlogsById(@PathVariable ObjectId id) {
        BlogEntry blogEntry = blogEntryService.getById(id);
        if (blogEntry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(blogEntry, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BlogEntry> createNewEntry(@RequestBody BlogEntry newBlogEntry) {
        try {
            newBlogEntry.setCreatedTime(LocalDateTime.now());
            blogEntryService.save(newBlogEntry);
            return new ResponseEntity<>(newBlogEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlogsById(@PathVariable ObjectId id) {
        BlogEntry blogEntry = blogEntryService.getById(id);
        if (blogEntry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        blogEntryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogEntry> updateBlogsById(@PathVariable ObjectId id, @RequestBody BlogEntry newBlog) {
        BlogEntry oldBlog = blogEntryService.getById(id);
        if (oldBlog == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (newBlog.getTitle() != null && !newBlog.getTitle().isEmpty()) {
            oldBlog.setTitle(newBlog.getTitle());
        }
        if (newBlog.getContent() != null && !newBlog.getContent().isEmpty()) {
            oldBlog.setContent(newBlog.getContent());

        }
        if (newBlog.getUserName() != null && !newBlog.getUserName().isEmpty()) {
            oldBlog.setUserName(newBlog.getUserName());
        }
        oldBlog.setCreatedTime(LocalDateTime.now());
        blogEntryService.save(oldBlog);
        return new ResponseEntity<>(oldBlog, HttpStatus.OK);
    }
}
