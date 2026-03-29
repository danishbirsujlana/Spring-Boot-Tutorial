package net.engineeringdigest.journalApp.controller;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Special Type of Class that handles and controls the HTTP requests.
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUserUsers() {
        List<User> users = userService.findAll();
        if(users != null && !users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create")
    public void createAdmin(@RequestBody User user) {
        userService.saveAdmin(user);
    }
}
