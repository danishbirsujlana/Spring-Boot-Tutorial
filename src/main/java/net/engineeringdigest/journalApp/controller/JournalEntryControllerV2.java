package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

// Special Type of Class that handles and controls the HTTP requests.
@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping("/grid")
    public List<JournalEntry> getAll() {
        return journalEntryService.findAll();
    }

    @PostMapping("/create")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry newEntry) {
        try {
            newEntry.setCreatedAt(LocalDateTime.now());
            newEntry.setUpdatedAt(LocalDateTime.now());
            journalEntryService.saveEntry(newEntry);
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

    @DeleteMapping("/{_id}/delete")
    public void delete(@PathVariable ObjectId _id) {
        journalEntryService.deleteById(_id);
    }

    @PutMapping("/{_id}/update")
    public boolean update(@PathVariable ObjectId _id, @RequestBody JournalEntry updatedEntry) {
        journalEntryService.saveEntry(updatedEntry);
        return true;
    }
}
