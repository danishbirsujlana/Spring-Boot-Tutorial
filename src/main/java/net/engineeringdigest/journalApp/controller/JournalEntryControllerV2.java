package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

// Special Type of Class that handles and controls the HTTP requests.
@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("/grid")
    public List<JournalEntry> getAll(@PathVariable String username) {
        return journalEntryService.findAll();
    }

    @GetMapping("/grid/user/{username}")
    public ResponseEntity<?> getAllEntriesByUser(@PathVariable String username) {
        User user = userService.findByUsername(username);
        List<JournalEntry> allEntries = user.getJournalEntries();
        if(allEntries != null && !allEntries.isEmpty()) {
            return new ResponseEntity<>(allEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create/{username}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry newEntry, @PathVariable String username) {
        try {
            journalEntryService.saveEntry(newEntry, username);
            return new ResponseEntity<>(newEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{_id}/detail")
    public ResponseEntity<JournalEntry> detail(@PathVariable ObjectId _id) {
        Optional<JournalEntry> journalEntry = journalEntryService.findById(_id);
        if(journalEntry.isPresent()) {
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{_id}/delete/{username}")
    public void delete(@PathVariable ObjectId _id, @PathVariable String username) {
        journalEntryService.deleteById(_id, username);
    }

    @PutMapping("/{_id}/update")
    public boolean update(@PathVariable ObjectId _id, @RequestBody JournalEntry updatedEntry) {
        journalEntryService.saveEntry(updatedEntry);
        return true;
    }
}
