
package com.schoolportal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.schoolportal.dto.EquipmentDTO;
import com.schoolportal.entity.Equipment;
import com.schoolportal.entity.EquipmentAvailabilityType;
import com.schoolportal.entity.EquipmentCategory;
import com.schoolportal.repository.EquipmentAvailabilityTypeRepository;
import com.schoolportal.repository.EquipmentCategoryRepository;
import com.schoolportal.repository.EquipmentRepository;

@ExtendWith(MockitoExtension.class)
class EquipmentServiceTest {

	@Mock
	private EquipmentRepository equipmentRepository;

	@Mock
	private EquipmentAvailabilityTypeRepository availabilityRepository;

	@Mock
	private EquipmentCategoryRepository categoryRepository;

	@InjectMocks
	private EquipmentService equipmentService;

	private EquipmentCategory category;
	private EquipmentAvailabilityType available;
	private EquipmentAvailabilityType lentOut;
	private EquipmentAvailabilityType damaged;

	@BeforeEach
	void setUp() {

		category = new EquipmentCategory();
		category.setId(1);
		category.setCategoryName("Electronics");

		available = new EquipmentAvailabilityType();
		available.setId(1);
		available.setAvailabilityStatus("Available");

		lentOut = new EquipmentAvailabilityType();
		lentOut.setId(2);
		lentOut.setAvailabilityStatus("Lent Out");

		damaged = new EquipmentAvailabilityType();
		damaged.setId(3);
		damaged.setAvailabilityStatus("Damaged");
	}

	// Helper method to create an equipment DTO
	private EquipmentDTO createDTO(int categoryId, int availabilityId, int totalQuantity, Integer quantityLeft) {

		EquipmentDTO dto = new EquipmentDTO();

		dto.setName("Laptop");
		dto.setCategoryId(categoryId);
		dto.setAvailabilityId(availabilityId);
		dto.setTotalQuantity(totalQuantity);
		dto.setQuantityLeft(quantityLeft);
		dto.setDescription("Dell Laptop");
		dto.setCreatedBy(1);
		dto.setModifiedBy(1);

		return dto;
	}

	// Helper method to create an equipment entity
	private Equipment createEquipment(int totalQuantity, Integer quantityLeft) {

		Equipment equipment = new Equipment();

		equipment.setId(10);
		equipment.setName("Laptop");
		equipment.setCategory(category);
		equipment.setAvailabilityType(available);
		equipment.setTotalQuantity(totalQuantity);
		equipment.setQuantityLeft(quantityLeft);
		equipment.setDescription("Dell Laptop");
		equipment.setCreatedBy(1);
		equipment.setModifiedBy(1);

		return equipment;
	}

	// ==========================================
	// GET ALL EQUIPMENTS
	// ==========================================

	@Test
	void getAllEquipments_shouldReturnEquipmentList() {

		Equipment equipment = createEquipment(10, 8);

		when(equipmentRepository.findAllWithDetails()).thenReturn(List.of(equipment));

		List<EquipmentDTO> result = equipmentService.getAllEquipments();

		assertEquals(1, result.size());
		assertEquals("Laptop", result.get(0).getName());
		assertEquals(1, result.get(0).getCategoryId());
		assertEquals("Electronics", result.get(0).getCategoryName());
		assertEquals(1, result.get(0).getAvailabilityId());
		assertEquals("Available", result.get(0).getAvailabilityStatus());
		assertEquals(10, result.get(0).getTotalQuantity());
		assertEquals(8, result.get(0).getQuantityLeft());

		verify(equipmentRepository).findAllWithDetails();
	}

	@Test
	void getAllEquipments_whenNoEquipment_shouldReturnEmptyList() {

		when(equipmentRepository.findAllWithDetails()).thenReturn(List.of());

		List<EquipmentDTO> result = equipmentService.getAllEquipments();

		assertTrue(result.isEmpty());
	}

	// ==========================================
	// ADD EQUIPMENT
	// ==========================================

	@Test
	void addEquipment_withValidData_shouldSaveEquipment() {

		EquipmentDTO dto = createDTO(1, 1, 10, 10);

		when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

		when(availabilityRepository.findById(1)).thenReturn(Optional.of(available));

		boolean result = equipmentService.addEquipment(dto);

		assertTrue(result);

		verify(equipmentRepository).save(argThat(equipment -> equipment.getName().equals("Laptop")
				&& equipment.getTotalQuantity() == 10 && equipment.getQuantityLeft() == 10
				&& equipment.getCategory() == category && equipment.getAvailabilityType() == available));
	}

	@Test
	void addEquipment_withInvalidCategory_shouldThrowException() {

		EquipmentDTO dto = createDTO(99, 1, 10, 10);

		when(categoryRepository.findById(99)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> equipmentService.addEquipment(dto));

		assertEquals("Invalid equipment category.", exception.getMessage());

		verify(equipmentRepository, never()).save(any());
	}

	@Test
	void addEquipment_withInvalidAvailability_shouldThrowException() {

		EquipmentDTO dto = createDTO(1, 99, 10, 10);

		when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

		when(availabilityRepository.findById(99)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> equipmentService.addEquipment(dto));

		assertEquals("Invalid availability status.", exception.getMessage());

		verify(equipmentRepository, never()).save(any());
	}

	@Test
	void addEquipment_withNegativeQuantity_shouldThrowException() {

		EquipmentDTO dto = createDTO(1, 1, -5, -5);

		when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

		when(availabilityRepository.findById(1)).thenReturn(Optional.of(available));

		RuntimeException exception = assertThrows(RuntimeException.class, () -> equipmentService.addEquipment(dto));

		assertEquals("Total quantity cannot be negative.", exception.getMessage());

		verify(equipmentRepository, never()).save(any());
	}

	@Test
	void addEquipment_withMismatchedQuantityLeft_shouldThrowException() {

		EquipmentDTO dto = createDTO(1, 1, 10, 5);

		when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

		when(availabilityRepository.findById(1)).thenReturn(Optional.of(available));

		RuntimeException exception = assertThrows(RuntimeException.class, () -> equipmentService.addEquipment(dto));

		assertEquals("New equipment quantity left must equal total quantity.", exception.getMessage());

		verify(equipmentRepository, never()).save(any());
	}

	// ==========================================
	// UPDATE EQUIPMENT
	// ==========================================

	@Test
	void updateEquipment_withValidData_shouldUpdateEquipment() {

		Equipment existing = createEquipment(10, 7);

		EquipmentDTO dto = createDTO(1, 1, 12, 12);

		when(equipmentRepository.findById(10)).thenReturn(Optional.of(existing));

		when(availabilityRepository.findById(1)).thenReturn(Optional.of(available));

		when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

		boolean result = equipmentService.updateEquipment(10, dto);

		assertTrue(result);

		// 3 items issued, total increased to 12.
		// Remaining stock should therefore be 9.
		assertEquals(12, existing.getTotalQuantity());
		assertEquals(9, existing.getQuantityLeft());
		assertEquals("Laptop", existing.getName());

		verify(equipmentRepository).save(existing);
	}

	@Test
	void updateEquipment_withInvalidId_shouldThrowException() {

		EquipmentDTO dto = createDTO(1, 1, 10, 10);

		when(equipmentRepository.findById(99)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> equipmentService.updateEquipment(99, dto));

		assertEquals("Equipment not found with ID = 99", exception.getMessage());

		verify(equipmentRepository, never()).save(any());
	}

	@Test
	void updateEquipment_withNullStock_shouldThrowException() {

		Equipment existing = createEquipment(10, null);

		EquipmentDTO dto = createDTO(1, 1, 10, 10);

		when(equipmentRepository.findById(10)).thenReturn(Optional.of(existing));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> equipmentService.updateEquipment(10, dto));

		assertEquals("Equipment stock quantity is not configured.", exception.getMessage());

		verify(equipmentRepository, never()).save(any());
	}

	@Test
	void updateEquipment_withNegativeQuantity_shouldThrowException() {

		Equipment existing = createEquipment(10, 8);

		EquipmentDTO dto = createDTO(1, 1, -1, -1);

		when(equipmentRepository.findById(10)).thenReturn(Optional.of(existing));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> equipmentService.updateEquipment(10, dto));

		assertEquals("Total quantity cannot be negative.", exception.getMessage());

		verify(equipmentRepository, never()).save(any());
	}

	@Test
	void updateEquipment_belowIssuedQuantity_shouldThrowException() {

		Equipment existing = createEquipment(10, 7);
		// 3 items are already issued.

		EquipmentDTO dto = createDTO(1, 1, 2, 2);

		when(equipmentRepository.findById(10)).thenReturn(Optional.of(existing));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> equipmentService.updateEquipment(10, dto));

		assertEquals("Total quantity cannot be less than issued quantity (3).", exception.getMessage());

		verify(equipmentRepository, never()).save(any());
	}

	@Test
	void updateEquipment_allItemsIssued_shouldRequireLentOutStatus() {

		Equipment existing = createEquipment(10, 7);

		EquipmentDTO dto = createDTO(1, 1, 3, 3);
		// 3 total items, all 3 are already issued.

		when(equipmentRepository.findById(10)).thenReturn(Optional.of(existing));

		when(availabilityRepository.findById(1)).thenReturn(Optional.of(available));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> equipmentService.updateEquipment(10, dto));

		assertTrue(exception.getMessage().contains("All items are issued, status must be 'Lent Out'."));

		verify(equipmentRepository, never()).save(any());
	}

	@Test
	void updateEquipment_allItemsIssuedWithLentOut_shouldSucceed() {

		Equipment existing = createEquipment(10, 7);

		EquipmentDTO dto = createDTO(1, 2, 3, 3);

		when(equipmentRepository.findById(10)).thenReturn(Optional.of(existing));

		when(availabilityRepository.findById(2)).thenReturn(Optional.of(lentOut));

		when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

		boolean result = equipmentService.updateEquipment(10, dto);

		assertTrue(result);
		assertEquals(3, existing.getTotalQuantity());
		assertEquals(0, existing.getQuantityLeft());
		assertEquals("Lent Out", existing.getAvailabilityType().getAvailabilityStatus());

		verify(equipmentRepository).save(existing);
	}

	@Test
	void updateEquipment_damagedWhileItemsIssued_shouldThrowException() {

		Equipment existing = createEquipment(10, 7);

		EquipmentDTO dto = createDTO(1, 3, 10, 10);

		when(equipmentRepository.findById(10)).thenReturn(Optional.of(existing));

		when(availabilityRepository.findById(3)).thenReturn(Optional.of(damaged));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> equipmentService.updateEquipment(10, dto));

		assertTrue(exception.getMessage().contains("Cannot mark equipment as Damaged while items are still issued."));

		verify(equipmentRepository, never()).save(any());
	}

	@Test
	void updateEquipment_lentOutWithSomeStockRemaining_shouldBecomeAvailable() {

		Equipment existing = createEquipment(10, 7);

		EquipmentDTO dto = createDTO(1, 2, 12, 12);

		when(equipmentRepository.findById(10)).thenReturn(Optional.of(existing));

		when(availabilityRepository.findById(2)).thenReturn(Optional.of(lentOut));

		when(availabilityRepository.findByAvailabilityStatusIgnoreCase("Available")).thenReturn(Optional.of(available));

		when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

		boolean result = equipmentService.updateEquipment(10, dto);

		assertTrue(result);
		assertSame(available, existing.getAvailabilityType());
		assertEquals(9, existing.getQuantityLeft());

		verify(equipmentRepository).save(existing);
	}

	@Test
	void updateEquipment_lentOutWithNoIssuedItems_shouldThrowException() {

		Equipment existing = createEquipment(10, 10);

		EquipmentDTO dto = createDTO(1, 2, 12, 12);

		when(equipmentRepository.findById(10)).thenReturn(Optional.of(existing));

		when(availabilityRepository.findById(2)).thenReturn(Optional.of(lentOut));

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> equipmentService.updateEquipment(10, dto));

		assertTrue(exception.getMessage().contains("Cannot mark equipment as 'Lent Out' when no items are issued."));

		verify(equipmentRepository, never()).save(any());
	}

	@Test
	void updateEquipment_withoutAvailableStatus_shouldThrowException() {

		Equipment existing = createEquipment(10, 7);

		EquipmentDTO dto = createDTO(1, 2, 12, 12);

		when(equipmentRepository.findById(10)).thenReturn(Optional.of(existing));

		when(availabilityRepository.findById(2)).thenReturn(Optional.of(lentOut));

		when(availabilityRepository.findByAvailabilityStatusIgnoreCase("Available")).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> equipmentService.updateEquipment(10, dto));

		assertEquals("Available status not found in database.", exception.getMessage());

		verify(equipmentRepository, never()).save(any());
	}

	@Test
	void updateEquipment_withInvalidCategory_shouldThrowException() {

		Equipment existing = createEquipment(10, 10);

		EquipmentDTO dto = createDTO(99, 1, 10, 10);

		when(equipmentRepository.findById(10)).thenReturn(Optional.of(existing));

		when(availabilityRepository.findById(1)).thenReturn(Optional.of(available));

		when(categoryRepository.findById(99)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> equipmentService.updateEquipment(10, dto));

		assertEquals("Invalid equipment category.", exception.getMessage());

		verify(equipmentRepository, never()).save(any());
	}

	// ==========================================
	// DELETE EQUIPMENT
	// ==========================================

	@Test
	void deleteEquipment_whenEquipmentExists_shouldDelete() {

		when(equipmentRepository.existsById(10)).thenReturn(true);

		boolean result = equipmentService.deleteEquipment(10);

		assertTrue(result);

		verify(equipmentRepository).deleteById(10);
	}

	@Test
	void deleteEquipment_whenEquipmentDoesNotExist_shouldReturnFalse() {

		when(equipmentRepository.existsById(99)).thenReturn(false);

		boolean result = equipmentService.deleteEquipment(99);

		assertFalse(result);

		verify(equipmentRepository, never()).deleteById(anyInt());
	}
}
