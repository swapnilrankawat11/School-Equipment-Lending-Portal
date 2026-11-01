package com.schoolportal.repository;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.schoolportal.entity.Equipment;
import com.schoolportal.entity.EquipmentAvailabilityType;
import com.schoolportal.entity.EquipmentCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EquipmentRepositoryTest {

    @Autowired
    private EquipmentRepository repository;

    @Autowired
    private EquipmentCategoryRepository categoryRepository;

    @Autowired
    private EquipmentAvailabilityTypeRepository availabilityTypeRepository;

    private Equipment createEquipment(String name, int totalQuantity, int quantityLeft) {
        Equipment equipment = new Equipment();
        equipment.setName(name);
        equipment.setCategory(createCategory());
        equipment.setAvailabilityType(createAvailabilityType());
        equipment.setTotalQuantity(totalQuantity);
        equipment.setQuantityLeft(quantityLeft);

        return equipment;
    }

    private EquipmentCategory createCategory() {
        return categoryRepository.findAll()
                .stream()
                .filter(c -> c.getCategoryName().equals("Electronics"))
                .findFirst()
                .orElseGet(() -> {
                    EquipmentCategory category = new EquipmentCategory();
                    category.setCategoryName("Electronics");
                    return categoryRepository.save(category);
                });
    }

    private EquipmentAvailabilityType createAvailabilityType() {
        return availabilityTypeRepository.findAll()
                .stream()
                .filter(a -> a.getAvailabilityStatus().equals("Available"))
                .findFirst()
                .orElseGet(() -> {
                    EquipmentAvailabilityType availability = new EquipmentAvailabilityType();
                    availability.setAvailabilityStatus("Available");
                    return availabilityTypeRepository.save(availability);
                });
    }
    
    @Test
    void save_shouldPersistEquipmentWithRelationships() {
        Equipment equipment = createEquipment("Laptop", 10, 8);

        Equipment saved = repository.save(equipment);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Laptop");
        assertThat(saved.getTotalQuantity()).isEqualTo(10);
        assertThat(saved.getQuantityLeft()).isEqualTo(8);
        assertThat(saved.getCategory().getCategoryName()).isEqualTo("Electronics");
        assertThat(saved.getAvailabilityType().getAvailabilityStatus()).isEqualTo("Available");
    }

    @Test
    void findById_shouldReturnEquipment() {
        Equipment saved = repository.save(createEquipment("Laptop", 10, 8));

        Optional<Equipment> result = repository.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Laptop");
    }

    @Test
    void findAll_shouldReturnAllEquipment() {
        repository.save(createEquipment("Laptop", 10, 8));
        repository.save(createEquipment("Projector", 5, 3));

        assertThat(repository.findAll()).hasSize(2);
    }

    @Test
    void findAllWithDetails_shouldReturnEquipmentWithCategoryAndAvailability() {
        repository.save(createEquipment("Laptop", 10, 8));
        repository.save(createEquipment("Projector", 5, 3));

        List<Equipment> result = repository.findAllWithDetails();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Equipment::getName)
                .containsExactly("Laptop", "Projector");
        assertThat(result.get(0).getCategory().getCategoryName()).isEqualTo("Electronics");
        assertThat(result.get(0).getAvailabilityType().getAvailabilityStatus()).isEqualTo("Available");
    }

    @Test
    @Transactional
    void updateEquipmentQuantityLeftById_shouldUpdateQuantity() {
        Equipment saved = repository.save(createEquipment("Laptop", 10, 8));

        int rowsUpdated = repository.updateEquipmentQuantityLeftById(5, saved.getId());

        assertThat(rowsUpdated).isEqualTo(1);
        assertThat(repository.findById(saved.getId()).get().getQuantityLeft()).isEqualTo(5);
    }

    @Test
    @Transactional
    void updateEquipmentQuantityLeftById_shouldReturnZeroForUnknownId() {
        int rowsUpdated = repository.updateEquipmentQuantityLeftById(5, 999);

        assertThat(rowsUpdated).isZero();
    }

    @Test
    @Transactional
    void updateAvailabilityStatusById_shouldUpdateAvailability() {
        Equipment saved = repository.save(createEquipment("Laptop", 10, 8));

        EquipmentAvailabilityType unavailable = new EquipmentAvailabilityType();
        unavailable.setAvailabilityStatus("Unavailable");
        unavailable = availabilityTypeRepository.save(unavailable);

        int rowsUpdated = repository.updateAvailabilityStatusById(
                unavailable.getId(), saved.getId());

        assertThat(rowsUpdated).isEqualTo(1);

        repository.flush();

        assertThat(repository.findById(saved.getId()).get()
                .getAvailabilityType().getAvailabilityStatus()).isEqualTo("Unavailable");
    }

    @Test
    void delete_shouldRemoveEquipment() {
        Equipment saved = repository.save(createEquipment("Laptop", 10, 8));

        repository.deleteById(saved.getId());

        assertThat(repository.findById(saved.getId())).isEmpty();
    }
}