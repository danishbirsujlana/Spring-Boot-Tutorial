package net.engineeringdigest.journalApp.entity;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.*;

// POJO Class
@Document(collation = "users")
@Data
public class User {
    @Id
    private ObjectId _id;
    @NonNull
    @Indexed(unique = true) // for username to be unique.
    private String username;
    @NonNull
    private String password;
    @DBRef // this field will be reference to entries in Journal Entries // Foreign key
    private List<JournalEntry> journalEntries = new ArrayList<>();
}