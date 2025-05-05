package com.alienworkspace.cdr.demographic.model;

import com.alienworkspace.cdr.demographic.model.audit.AuditTrail;
import com.google.common.base.MoreObjects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a name for a person.
 *
 * <p>This is a separate entity from <code>Person</code> in order to allow a person to have multiple names (e.g.
 * maiden name, married name, etc.), and to allow each name to have a start and end date.</p>
 *
 * <p>Each name is associated with a <code>Person</code> by its <code>personId</code> field.</p>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "person_name")
public class PersonName extends AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "person_name_id")
    private long personNameId;

    @Column(name = "person_id")
    private long personId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "other_name")
    private String otherName;

    private boolean preferred;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PersonName that)) {
            return false;
        }
        return personNameId == that.personNameId && personId == that.personId
                && preferred == that.preferred && Objects.equals(firstName, that.firstName)
                && Objects.equals(middleName, that.middleName)
                && Objects.equals(lastName, that.lastName)
                && Objects.equals(otherName, that.otherName)
                && Objects.equals(this.getCreatedAt(), that.getCreatedAt())
                && Objects.equals(this.getCreatedBy(), that.getCreatedBy())
                && Objects.equals(this.getLastModifiedBy(), that.getLastModifiedBy())
                && Objects.equals(this.getLastModifiedAt(), that.getLastModifiedAt())
                && this.isVoided() == that.isVoided()
                && Objects.equals(this.getVoidedAt(), that.getVoidedAt())
                && Objects.equals(this.getVoidReason(), that.getVoidReason())
                && Objects.equals(this.getVoidedBy(), that.getVoidedBy())
                && Objects.equals(this.getUuid(), that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(personNameId, personId, firstName, middleName,
                lastName, otherName, preferred, getCreatedAt(), getCreatedBy(), getLastModifiedBy(),
                getLastModifiedAt(), isVoided(), getVoidedAt(), getVoidReason(), getVoidedBy(), getUuid());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("PersonId", personId)
                .add("PersonNameId", personNameId)
                .add("FirstName", firstName)
                .add("MiddleName", middleName)
                .add("LastName", lastName)
                .add("OtherName", otherName)
                .add("Preferred", preferred)
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
