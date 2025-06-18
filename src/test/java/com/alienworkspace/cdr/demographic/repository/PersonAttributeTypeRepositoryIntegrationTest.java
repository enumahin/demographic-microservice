package com.alienworkspace.cdr.demographic.repository;

import com.alienworkspace.cdr.demographic.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.demographic.model.PersonAttributeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PersonAttributeTypeRepositoryIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private PersonAttributeTypeRepository personAttributeTypeRepository;

    @BeforeEach
    void setup() {
        personAttributeTypeRepository.deleteAll();
    }

    @Test
    @DisplayName("Test save person attribute type")
    void testSave() {
        // given
        PersonAttributeType attributeType = PersonAttributeType.builder()
                .name("Test Type")
                .description("Test Description")
                .format("java.lang.String")
                .build();

        // when
        PersonAttributeType saved = personAttributeTypeRepository.save(attributeType);

        // then
        assertTrue(saved.getPersonAttributeTypeId() > 0);
        assertEquals("Test Type", saved.getName());
        assertEquals("Test Description", saved.getDescription());
        assertEquals("java.lang.String", saved.getFormat());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    @DisplayName("Test update person attribute type")
    void testUpdate() {
        // given
        PersonAttributeType attributeType = PersonAttributeType.builder()
                .name("Test Type")
                .description("Test Description")
                .format("java.lang.String")
                .build();

        // when
        PersonAttributeType saved = personAttributeTypeRepository.save(attributeType);
        saved.setName("Updated Name");
        saved.setDescription("Updated Description");
        saved.setFormat("java.lang.Integer");
        saved.setLastModifiedAt(LocalDateTime.now());
        saved.setLastModifiedBy(1L);
        PersonAttributeType updated = personAttributeTypeRepository.save(saved);

        // then
        assertEquals(saved.getPersonAttributeTypeId(), updated.getPersonAttributeTypeId());
        assertEquals("Updated Name", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
        assertEquals("java.lang.Integer", updated.getFormat());
        assertNotNull(updated.getLastModifiedAt());
        assertEquals(1L, updated.getLastModifiedBy());
    }

    @Test
    @DisplayName("Test find person attribute type by ID")
    void testFindById() {
        // given
        PersonAttributeType attributeType = PersonAttributeType.builder()
                .name("Test Type")
                .description("Test Description")
                .format("java.lang.String")
                .build();

        // when
        PersonAttributeType saved = personAttributeTypeRepository.save(attributeType);
        Optional<PersonAttributeType> found = personAttributeTypeRepository.findById(saved.getPersonAttributeTypeId());

        // then
        assertTrue(found.isPresent());
        assertEquals(saved.getPersonAttributeTypeId(), found.get().getPersonAttributeTypeId());
        assertEquals(saved.getName(), found.get().getName());
        assertEquals(saved.getDescription(), found.get().getDescription());
        assertEquals(saved.getFormat(), found.get().getFormat());
    }

    @Test
    @DisplayName("Test find all person attribute types")
    void testFindAll() {
        // given
        PersonAttributeType type1 = PersonAttributeType.builder()
                .name("Type 1")
                .description("Description 1")
                .format("java.lang.String")
                .build();

        PersonAttributeType type2 = PersonAttributeType.builder()
                .name("Type 2")
                .description("Description 2")
                .format("java.lang.Integer")
                .build();

        // when
        personAttributeTypeRepository.save(type1);
        personAttributeTypeRepository.save(type2);
        List<PersonAttributeType> all = personAttributeTypeRepository.findAll();

        // then
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(type -> type.getName().equals("Type 1")));
        assertTrue(all.stream().anyMatch(type -> type.getName().equals("Type 2")));
    }

    @Test
    @DisplayName("Test delete person attribute type")
    void testDelete() {
        // given
        PersonAttributeType attributeType = PersonAttributeType.builder()
                .name("Test Type")
                .description("Test Description")
                .format("java.lang.String")
                .build();

        // when
        PersonAttributeType saved = personAttributeTypeRepository.save(attributeType);
        personAttributeTypeRepository.delete(saved);

        // then
        Optional<PersonAttributeType> found = personAttributeTypeRepository.findById(saved.getPersonAttributeTypeId());
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Test soft delete person attribute type")
    void testSoftDelete() {
        // given
        PersonAttributeType attributeType = PersonAttributeType.builder()
                .name("Test Type")
                .description("Test Description")
                .format("java.lang.String")
                .build();

        // when
        PersonAttributeType saved = personAttributeTypeRepository.save(attributeType);
        saved.setVoided(true);
        saved.setVoidedBy(1L);
        saved.setVoidReason("Test void reason");
        saved.setVoidedAt(LocalDateTime.now());
        PersonAttributeType voided = personAttributeTypeRepository.save(saved);

        // then
        Optional<PersonAttributeType> found = personAttributeTypeRepository.findById(voided.getPersonAttributeTypeId());
        assertTrue(found.isPresent());
        assertTrue(found.get().isVoided());
        assertEquals(1L, found.get().getVoidedBy());
        assertEquals("Test void reason", found.get().getVoidReason());
        assertNotNull(found.get().getVoidedAt());
    }
} 