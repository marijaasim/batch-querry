package com.marija.quarry_batch.service;

import com.marija.quarry_batch.model.User;
import com.marija.quarry_batch.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

}
