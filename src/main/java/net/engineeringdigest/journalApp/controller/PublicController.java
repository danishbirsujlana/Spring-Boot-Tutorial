package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    UserService userService;

    @GetMapping("/health-check")
    public String health () {
       return "Server OK";
    }

    // User creation is public and no need to authenticate here
    @PostMapping("/user/create")
    public void createUser(@RequestBody User user) {
        userService.saveNewUser(user);
    }
}
