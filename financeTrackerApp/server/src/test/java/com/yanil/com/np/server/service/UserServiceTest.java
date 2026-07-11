package com.yanil.com.np.server.service;

import com.yanil.com.np.server.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private  UserRepository userRepository;



    @ParameterizedTest
    @CsvSource({
            "ram",
            "yanil",
            "vipul"
    })
    public void testGetUserByUsername(String username){
        assertNotNull(userRepository.findByUsername(username));
    }
}
