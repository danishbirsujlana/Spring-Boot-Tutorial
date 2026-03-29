package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

// Special Type of Class that handles and controls the HTTP requests.
@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("/grid")
    public ResponseEntity<?> getAllEntriesByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // To get the username after authenticating
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<JournalEntry> allEntries = user.getJournalEntries();
        if(allEntries != null && !allEntries.isEmpty()) {
            return new ResponseEntity<>(allEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry newEntry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // To get the username after authenticating
            String username = authentication.getName();
            journalEntryService.saveEntry(newEntry, username);
            return new ResponseEntity<>(newEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{_id}/detail")
    public ResponseEntity<JournalEntry> detail(@PathVariable ObjectId _id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // To get the username after authenticating
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<JournalEntry> collectedEntries = user.getJournalEntries().stream().filter(x -> x.get_id().equals(_id)).collect(Collectors.toList());
        if(!collectedEntries.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(_id);
            if(journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{_id}/delete")
    public ResponseEntity<?> delete(@PathVariable ObjectId _id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // To get the username after authenticating
        String username = authentication.getName();
        boolean isRemoved = journalEntryService.deleteById(_id, username);
        if(isRemoved) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{_id}/update")
    public ResponseEntity<?> update(@PathVariable ObjectId _id, @RequestBody JournalEntry updatedEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // To get the username after authenticating
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<JournalEntry> collectedEntries = user.getJournalEntries().stream().filter(x -> x.get_id().equals(_id)).collect(Collectors.toList());
        if(!collectedEntries.isEmpty()) {
            Optional<JournalEntry> oldEntry = journalEntryService.findById(_id);
            if(oldEntry.isPresent()) {
                journalEntryService.saveEntry(updatedEntry);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
