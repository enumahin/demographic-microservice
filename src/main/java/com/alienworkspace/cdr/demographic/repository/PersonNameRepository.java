package com.alienworkspace.cdr.demographic.repository;

import com.alienworkspace.cdr.demographic.model.PersonName;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository interface that provides the data access methods for the
 * {@link PersonName} entity.
 *
 * <p>This interface extends the {@link JpaRepository} interface and provides all the
 * methods that are available in the {@link JpaRepository} interface. In addition to
 * the methods provided by the {@link JpaRepository} interface, this interface also
 * provides a {@link #findAllByPersonIdAndVoided(long, boolean)} method that can be used to retrieve a person
 * by their ID.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Repository
public interface PersonNameRepository extends JpaRepository<PersonName, Long> {

    /**
     * Retrieves a person by their ID.
     *
     * @param personId the ID of the person to retrieve
     * @return an {@link Optional} containing the person if found, or an empty {@link Optional} if not found
     */
    List<PersonName> findAllByPersonIdAndVoided(long personId, boolean voided);

    /**
     * Retrieves a person name by record's ID.
     *
     * @param id the ID of the person name to retrieve
     * @return an {@link Optional} containing the person name if found, or an empty
     *         {@link Optional} if not found
     */
    Optional<PersonName> findByPersonNameId(long id);

    /**
     * Retrieves a person name by person ID and preferred flag.
     *
     * @param personId the ID of the person
     * @param preferred the preferred flag
     * @return an {@link Optional} containing the person name if found, or an empty
     *         {@link Optional} if not found
     */
    Optional<PersonName> findByPersonIdAndPreferred(long personId, boolean preferred);

    /**
     * Sets the preferred flag to false for all person names belonging to the given person.
     *
     * @param personId the ID of the person
     */
    @Transactional
    @Modifying
    @Query("UPDATE person_name p set p.preferred = false where p.personId = :personId")
    void unsetPreferred(long personId);

    /**
     * Retrieves all person names belonging to the given person.
     *
     * @param personId the ID of the person
     * @return a list of person names
     */
    List<PersonName> findAllByPersonId(long personId);
}
