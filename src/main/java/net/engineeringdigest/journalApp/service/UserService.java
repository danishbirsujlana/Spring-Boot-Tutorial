package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {
    @Autowired
    private UserRepo userRepository;

    public void saveEntry(User user) {
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId _id) {
        return userRepository.findById(_id);
    }

    public void deleteById(ObjectId _id) {
        userRepository.deleteById(_id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
