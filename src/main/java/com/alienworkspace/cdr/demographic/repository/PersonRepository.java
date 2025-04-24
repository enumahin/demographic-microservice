package com.alienworkspace.cdr.demographic.repository;

import com.alienworkspace.cdr.demographic.model.Person;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository interface that provides the data access methods for the
 * {@link Person} entity.
 *
 * <p>This interface extends the {@link JpaRepository} interface and provides all the
 * methods that are available in the {@link JpaRepository} interface. In addition to
 * the methods provided by the {@link JpaRepository} interface, this interface also
 * provides a {@link #findByPersonId(long)} method that can be used to retrieve a person
 * by their ID.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    /**
     * Retrieves a person by their ID.
     *
     * @param personId the ID of the person to retrieve
     * @return an {@link Optional} containing the person if found, or an empty {@link Optional} if not found
     */
    Optional<Person> findByPersonId(long personId);
}
