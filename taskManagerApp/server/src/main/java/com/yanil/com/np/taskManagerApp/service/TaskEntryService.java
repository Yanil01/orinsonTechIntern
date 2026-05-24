package com.yanil.com.np.taskManagerApp.service;

import com.yanil.com.np.taskManagerApp.entity.TaskEntry;
import com.yanil.com.np.taskManagerApp.repository.TaskEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskEntryService {
    private final TaskEntryRepository taskEntryRepository;

    public TaskEntryService(TaskEntryRepository taskEntryRepository) {
        this.taskEntryRepository = taskEntryRepository;
    }

    public void save(TaskEntry taskEntry){
        taskEntryRepository.save(taskEntry);
    }

    public List<TaskEntry> getAll(){
        return taskEntryRepository.findAll();
    }

    public void delete(ObjectId id){
        taskEntryRepository.deleteById(id);
    }
    public TaskEntry getById(ObjectId id){
        return taskEntryRepository.findById(id).orElse(null);
    }

}
