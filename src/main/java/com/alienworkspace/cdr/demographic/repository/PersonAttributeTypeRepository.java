package com.alienworkspace.cdr.demographic.repository;

import com.alienworkspace.cdr.demographic.model.PersonAttributeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository interface that provides the data access methods for the
 * {@link PersonAttributeType} entity.
 *
 * <p>This interface extends the {@link JpaRepository} interface and provides all the
 * methods that are available in the {@link JpaRepository} interface.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Repository
public interface PersonAttributeTypeRepository extends JpaRepository<PersonAttributeType, Integer> {
}
