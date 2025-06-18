package com.alienworkspace.cdr.demographic.model;

import com.alienworkspace.cdr.demographic.model.audit.AuditTrail;
import com.google.common.base.MoreObjects;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
@Table(name = "person")
public class Person extends AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "person_id")
    @Getter
    private long personId;

    @Getter
    @Column(name = "gender", length = 1, nullable = false)
    private Character gender;

    @Getter
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Getter
    @Builder.Default
    private boolean dead = false;

    @Getter
    @Column(name = "death_date")
    private LocalDate deathDate;

    @Getter
    @Column(name = "cause_of_death")
    private String causeOfDeath;

    @Setter
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", nullable = false)
    @Builder.Default
    private Set<PersonName> names = new HashSet<>();

    @Setter
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    @Builder.Default
    private Set<PersonAddress> addresses = new HashSet<>();

    @Setter
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    @Builder.Default
    private Set<PersonAttribute> attributes = new HashSet<>();

    /**
     * The names of the person.
     *
     * @return the preferred name
     */
    public PersonName getPreferredName() {
        return names.stream()
                .filter(PersonName::isPreferred).findFirst().orElse(null);
    }

    /**
     * The addresses of the person.
     *
     * @return the preferred address
     */
    public PersonAddress getPreferredAddress() {
        return addresses.stream()
                .filter(PersonAddress::isPreferred).findFirst().orElse(null);
    }

    /**
     * The attributes of the person.
     *
     * @return the preferred attribute
     */
    public PersonAttribute getPreferredAttribute(int personAttributeTypeId) {
        return attributes.stream()
                .filter(attribute ->
                        attribute.getPersonAttributeType().getPersonAttributeTypeId() == personAttributeTypeId)
                .filter(PersonAttribute::isPreferred).findFirst().orElse(null);
    }

    /**
     * Adds a name to the person.
     *
     * @param name the name to add
     */
    public void addName(PersonName name) {
        PersonName preferredName = getPreferredName();
        if (preferredName != null) {
            if (name.isPreferred()) {
                preferredName.setPreferred(false);
                name.setPreferred(true);
            }
        } else {
            name.setPreferred(true);
        }
        name.setPerson(this);
        names.add(name);
    }

    /**
     * Adds an address to the person.
     *
     * @param address the address to add
     */
    public void addAddress(PersonAddress address) {
        PersonAddress preferredAddress = getPreferredAddress();
        if (preferredAddress != null) {
            if (address.isPreferred()) {
                preferredAddress.setPreferred(false);
                address.setPreferred(true);
            }
        } else {
            address.setPreferred(true);
        }
        address.setPerson(this);
        addresses.add(address);
    }

    /**
     * Adds an attribute to the person.
     *
     * @param attribute the attribute to add
     */
    public void addAttribute(PersonAttribute attribute) {
        PersonAttribute preferredAttribute = getPreferredAttribute(
                attribute.getPersonAttributeType().getPersonAttributeTypeId());
        if (preferredAttribute != null) {
            if (attribute.isPreferred()) {
                preferredAttribute.setPreferred(false);
                attribute.setPreferred(true);
            }
        } else {
            attribute.setPreferred(true);
        }
        attribute.setPerson(this);
        attributes.add(attribute);
    }

    /**
     * Returns an unmodifiable set of names for the person.
     *
     * @return an unmodifiable set of names for the person
     */
    public Set<PersonName> getNames() {
        return Collections.unmodifiableSet(names);
    }

    /**
     * Returns an unmodifiable set of addresses for the person.
     *
     * @return an unmodifiable set of addresses for the person
     */
    public Set<PersonAddress> getAddresses() {
        return Collections.unmodifiableSet(addresses);
    }

    /**
     * Returns an unmodifiable set of attributes for the person.
     *
     * @return an unmodifiable set of attributes for the person
     */
    public Set<PersonAttribute> getAttributes() {
        return Collections.unmodifiableSet(attributes);
    }

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
