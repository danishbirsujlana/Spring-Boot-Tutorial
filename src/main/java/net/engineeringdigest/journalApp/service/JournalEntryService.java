package net.engineeringdigest.journalApp.service;
import java.time.LocalDateTime;
import java.util.*;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private UserService userService;

    public void saveEntry(JournalEntry journalEntry, String username) {
        User user = userService.findByUsername(username);
        journalEntry.setCreatedAt(LocalDateTime.now());
        journalEntry.setUpdatedAt(LocalDateTime.now());
        JournalEntry savedEntry = journalEntryRepo.save(journalEntry);
        user.getJournalEntries().add(savedEntry);
        userService.saveEntry(user);
    }

    // Overloading for update
    public void saveEntry(JournalEntry journalEntry) {
        journalEntry.setUpdatedAt(LocalDateTime.now());
        JournalEntry savedEntry = journalEntryRepo.save(journalEntry);
    }

    public List<JournalEntry> findAll() {
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId _id) {
        return journalEntryRepo.findById(_id);
    }

    public void deleteById(ObjectId _id, String username) {
        User user = userService.findByUsername(username);
        user.getJournalEntries().removeIf(x -> x.get_id().equals(_id));
        userService.saveEntry(user);
        journalEntryRepo.deleteById(_id);
    }
}
