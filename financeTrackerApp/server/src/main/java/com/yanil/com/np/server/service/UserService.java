package com.yanil.com.np.server.service;


import com.yanil.com.np.server.entity.User;
import com.yanil.com.np.server.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveNewUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("USER"));
        userRepository.save(user);
    }
    public void saveUser(User user){
        userRepository.save(user);
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public User getUserById(ObjectId id){
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }


    public void deleteUserById(ObjectId id){
       userRepository.deleteById(id);
    }
}
