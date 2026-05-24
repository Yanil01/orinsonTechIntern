package com.yanil.com.np.taskManagerApp.controller;

import com.yanil.com.np.taskManagerApp.entity.TaskEntry;
import com.yanil.com.np.taskManagerApp.service.TaskEntryService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "http://localhost:5173")
public class TaskManagerController {

    private final TaskEntryService taskEntryService;

    public TaskManagerController(TaskEntryService taskEntryService) {
        this.taskEntryService = taskEntryService;
    }

    @GetMapping
    public ResponseEntity<List<TaskEntry>> getAll() {
        List<TaskEntry> entries = taskEntryService.getAll();
        if (entries == null || entries.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entries, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TaskEntry> createEntry(@RequestBody TaskEntry newEntry) {
        try {
            newEntry.setCreatedDate(LocalDateTime.now());
            taskEntryService.save(newEntry);
            return new ResponseEntity<>(newEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEntry(@PathVariable ObjectId id) {
        if (taskEntryService.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        taskEntryService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskEntry> updateEntry(
            @PathVariable ObjectId id,
            @RequestBody TaskEntry newEntry) {

        TaskEntry oldEntry = taskEntryService.getById(id);
        if (oldEntry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (newEntry.getName() != null && !newEntry.getName().isEmpty())
            oldEntry.setName(newEntry.getName());

        if (newEntry.getDescriptions() != null && !newEntry.getDescriptions().isEmpty())
            oldEntry.setDescriptions(newEntry.getDescriptions());

        if (newEntry.getStatus() != null && !newEntry.getStatus().isEmpty())
            oldEntry.setStatus(newEntry.getStatus());

        oldEntry.setCreatedDate(LocalDateTime.now());
        taskEntryService.save(oldEntry);

        return ResponseEntity.ok(oldEntry);
    }
}