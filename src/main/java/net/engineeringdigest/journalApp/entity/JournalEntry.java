package net.engineeringdigest.journalApp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

// POJO Class
@Document
@Data
@NoArgsConstructor // We have to add a no-args constructor and @Data annotation of have Required-Args Constructor
public class JournalEntry {
    @Id
    private ObjectId _id;
    private String title;
    @NonNull
    private String content;
    @NonNull
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}