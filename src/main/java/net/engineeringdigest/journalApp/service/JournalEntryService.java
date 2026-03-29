package net.engineeringdigest.journalApp.service;
import java.time.LocalDateTime;
import java.util.*;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private UserService userService;

    @Transactional // Make a method transaction i.e. execute full program and roll back all when any line fail (atomicity)
    public void saveEntry(JournalEntry journalEntry, String username) {
        try {
            User user = userService.findByUsername(username);
            journalEntry.setCreatedAt(LocalDateTime.now());
            journalEntry.setUpdatedAt(LocalDateTime.now());
            JournalEntry savedEntry = journalEntryRepo.save(journalEntry);
            user.getJournalEntries().add(savedEntry);
            userService.saveEntry(user);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving entry", e);
        }
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

    @Transactional
    public boolean deleteById(ObjectId _id, String username) {
        boolean isRemoved = false;
        try {
            User user = userService.findByUsername(username);
            isRemoved = user.getJournalEntries().removeIf(x -> x.get_id().equals(_id));
            if(isRemoved) {
                userService.saveEntry(user);
                journalEntryRepo.deleteById(_id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting entry", e);
        }
        return isRemoved;
    }
}
