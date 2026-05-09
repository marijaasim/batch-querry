package com.marija.quarry_batch.service;

import com.marija.quarry_batch.model.User;
import com.marija.quarry_batch.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

}
