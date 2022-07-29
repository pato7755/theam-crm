package com.theam.crm.repository;

import com.theam.crm.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    void testFindByUsername() {

        User user = new User("userN", "passW");
        userRepository.save(user);
        User result = new User();

        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if (optionalUser.isPresent()) result = optionalUser.get();
        assertEquals(user.getUsername(), result.getUsername());

    }

    @Test
    @Transactional
    void testExistsByUsername() {
        User user = new User("myuser", "mypass");
        userRepository.save(user);
        Boolean userExists = userRepository.existsByUsername(user.getUsername());
        assertTrue(userExists);
    }

    @Test
    @Transactional
    void testExistsByUsernameWhichDoesNotExit() {
        User user = new User("randomUser1", "randomPass1");
        Boolean userExists = userRepository.existsByUsername(user.getUsername());
        assertFalse(userExists);
    }
}