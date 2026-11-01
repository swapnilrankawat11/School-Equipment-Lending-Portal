package com.schoolportal.repository;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.schoolportal.entity.UserType;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserTypeRepositoryTest {

    @Autowired
    private UserTypeRepository repository;

    @Test
    void save_shouldPersistUserType() {
        UserType userType = new UserType();
        userType.setUserType("Student");

        UserType saved = repository.save(userType);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUserType()).isEqualTo("Student");
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void findById_shouldReturnUserType() {
        UserType userType = new UserType();
        userType.setUserType("Admin");

        UserType saved = repository.save(userType);

        Optional<UserType> result = repository.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getUserType()).isEqualTo("Admin");
    }

    @Test
    void findById_shouldReturnEmptyWhenNotFound() {
        assertThat(repository.findById(999)).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllUserTypes() {
        UserType student = new UserType();
        student.setUserType("Student");

        UserType admin = new UserType();
        admin.setUserType("Admin");

        repository.saveAll(List.of(student, admin));

        assertThat(repository.findAll()).hasSize(2);
    }

    @Test
    void update_shouldModifyUserType() {
        UserType userType = new UserType();
        userType.setUserType("Student");

        UserType saved = repository.save(userType);

        saved.setUserType("Staff");
        repository.save(saved);

        assertThat(repository.findById(saved.getId()))
                .isPresent()
                .get()
                .extracting(UserType::getUserType)
                .isEqualTo("Staff");
    }

    @Test
    void delete_shouldRemoveUserType() {
        UserType userType = new UserType();
        userType.setUserType("Student");

        UserType saved = repository.save(userType);

        repository.deleteById(saved.getId());

        assertThat(repository.findById(saved.getId())).isEmpty();
    }
}