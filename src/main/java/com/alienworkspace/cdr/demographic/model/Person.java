package com.alienworkspace.cdr.demographic.model;

import com.alienworkspace.cdr.demographic.model.audit.AuditTrail;
import com.google.common.base.MoreObjects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



/**
 * Represents a person in the CDR system. This is the entity that
 * represents a person in the CDR system.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Getter
@Setter
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Person person)) {
            return false;
        }
        return personId == person.personId && dead == person.dead && Objects.equals(gender, person.gender)
                && Objects.equals(birthDate, person.birthDate) && Objects.equals(deathDate, person.deathDate)
                && Objects.equals(causeOfDeath, person.causeOfDeath)
                && Objects.equals(this.getCreatedAt(), person.getCreatedAt())
                && Objects.equals(this.getCreatedBy(), person.getCreatedBy())
                && Objects.equals(this.getLastModifiedBy(), person.getLastModifiedBy())
                && Objects.equals(this.getLastModifiedAt(), person.getLastModifiedAt())
                && this.isVoided() == person.isVoided()
                && Objects.equals(this.getVoidedAt(), person.getVoidedAt())
                && Objects.equals(this.getVoidReason(), person.getVoidReason())
                && Objects.equals(this.getVoidedBy(), person.getVoidedBy())
                && Objects.equals(this.getUuid(), person.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, gender, birthDate, dead, deathDate, causeOfDeath,
                getCreatedAt(), getCreatedBy(), getLastModifiedBy(), getLastModifiedAt(), isVoided(), getVoidedAt(),
                getVoidReason(), getVoidedBy(), getUuid());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("personId", personId)
                .add("gender", gender)
                .add("birthDate", birthDate)
                .add("dead", dead)
                .add("deathDate", deathDate)
                .add("causeOfDeath", causeOfDeath)
                .add("CreatedAt", getCreatedAt())
                .add("CreatedBy", getCreatedBy())
                .add("LastModifiedBy", getLastModifiedBy())
                .add("LastModifiedAt", getLastModifiedAt())
                .add("Voided", isVoided())
                .add("VoidedAt", getVoidedAt())
                .add("VoidReason", getVoidReason())
                .add("VoidedBy", getVoidedBy())
                .add("Uuid", getUuid())
                .toString();
    }
}
