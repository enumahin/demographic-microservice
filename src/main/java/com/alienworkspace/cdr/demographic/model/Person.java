package com.alienworkspace.cdr.demographic.model;

import com.alienworkspace.cdr.demographic.model.audit.AuditTrail;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents a person in the CDR system. This is the entity that
 * represents a person in the CDR system.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Person extends AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "person_id")
    private long personId;

    @Column
    private Character gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private boolean dead = false;

    @Column(name = "death_date")
    private LocalDate deathDate;

    @Column(name = "cause_of_death")
    private String causeOfDeath;
}
