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
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @PutMapping("/update/{username}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable String username) {
        User oldUser = userService.findByUsername(username);
        if(oldUser != null) {
            oldUser.setUsername(user.getUsername());
            oldUser.setPassword(user.getPassword());
            userService.saveEntry(oldUser);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
