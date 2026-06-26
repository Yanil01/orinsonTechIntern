package com.yanil.com.np.server.entity;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

@Document(collection = "blogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogEntry {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @Nonnull
    private String title;
    @Nonnull
    private String content;
    @Nonnull
    private String userName;

    private LocalDateTime createdTime;

}
