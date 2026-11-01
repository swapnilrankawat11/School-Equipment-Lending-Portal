package com.schoolportal.repository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.schoolportal.entity.RequestStatusType;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RequestStatusTypeRepositoryTest {

	@Autowired
	private RequestStatusTypeRepository repository;

	@Test
	void save_shouldPersistRequestStatusType() {
		RequestStatusType status = new RequestStatusType();
		status.setStatusType("Pending");
		status.setDescription("Request is awaiting approval");

		RequestStatusType saved = repository.save(status);

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getStatusType()).isEqualTo("Pending");
		assertThat(saved.getDescription()).isEqualTo("Request is awaiting approval");
	}

	@Test
	void findById_shouldReturnRequestStatusType() {
		RequestStatusType status = new RequestStatusType();
		status.setStatusType("Approved");

		RequestStatusType saved = repository.save(status);

		Optional<RequestStatusType> result = repository.findById(saved.getId());

		assertThat(result).isPresent();
		assertThat(result.get().getStatusType()).isEqualTo("Approved");
	}

	@Test
	void findById_shouldReturnEmptyWhenNotFound() {
		assertThat(repository.findById(999)).isEmpty();
	}

	@Test
	void findAll_shouldReturnAllRequestStatusTypes() {
		RequestStatusType pending = new RequestStatusType();
		pending.setStatusType("Pending");

		RequestStatusType approved = new RequestStatusType();
		approved.setStatusType("Approved");

		repository.saveAll(List.of(pending, approved));

		assertThat(repository.findAll()).hasSize(2);
	}

	@Test
	void update_shouldModifyRequestStatusType() {
		RequestStatusType status = new RequestStatusType();
		status.setStatusType("Pending");

		RequestStatusType saved = repository.save(status);

		saved.setStatusType("Rejected");
		repository.save(saved);

		assertThat(repository.findById(saved.getId())).isPresent().get().extracting(RequestStatusType::getStatusType)
				.isEqualTo("Rejected");
	}

	@Test
	void delete_shouldRemoveRequestStatusType() {
		RequestStatusType status = new RequestStatusType();
		status.setStatusType("Pending");

		RequestStatusType saved = repository.save(status);

		repository.deleteById(saved.getId());

		assertThat(repository.findById(saved.getId())).isEmpty();
	}
}