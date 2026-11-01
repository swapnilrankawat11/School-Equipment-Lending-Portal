package com.schoolportal.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.schoolportal.entity.User;
import com.schoolportal.entity.UserType;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    private User createUser(String email) {
        UserType userType = userTypeRepository.findAll()
                .stream()
                .filter(type -> type.getUserType().equals("Student"))
                .findFirst()
                .orElseGet(() -> {
                    UserType newType = new UserType();
                    newType.setUserType("Student");
                    return userTypeRepository.save(newType);
                });

        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Student");
        user.setEmail(email);
        user.setPassword("hashed-password");
        user.setUserType(userType);

        return repository.save(user);
    }

    @Test
    void save_shouldPersistUser() {
        User saved = createUser("student@example.com");

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getFirstName()).isEqualTo("Test");
        assertThat(saved.getLastName()).isEqualTo("Student");
        assertThat(saved.getEmail()).isEqualTo("student@example.com");
        assertThat(saved.getPassword()).isEqualTo("hashed-password");
        assertThat(saved.getUserType().getUserType()).isEqualTo("Student");
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void findById_shouldReturnUser() {
        User saved = createUser("student@example.com");

        Optional<User> result = repository.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("student@example.com");
    }

    @Test
    void findById_shouldReturnEmptyWhenNotFound() {
        assertThat(repository.findById(999)).isEmpty();
    }

    @Test
    void findByEmail_shouldReturnMatchingUser() {
        createUser("student@example.com");

        Optional<User> result = repository.findByEmail("student@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("Test");
    }

    @Test
    void findByEmail_shouldReturnEmptyWhenNotFound() {
        assertThat(repository.findByEmail("unknown@example.com")).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        createUser("student1@example.com");
        createUser("student2@example.com");

        assertThat(repository.findAll()).hasSize(2);
    }

    @Test
    void delete_shouldRemoveUser() {
        User saved = createUser("student@example.com");

        repository.deleteById(saved.getId());

        assertThat(repository.findById(saved.getId())).isEmpty();
    }
}