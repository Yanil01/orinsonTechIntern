package com.yanil.com.np.taskManagerApp.entity;

import lombok.Data;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "tasks")
public class TaskEntry {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String name;
    private String descriptions;
    private String status;
    private LocalDateTime createdDate;
}
