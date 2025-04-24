package com.alienworkspace.cdr.demographic.model;

import com.alienworkspace.cdr.demographic.model.audit.AuditTrail;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
}
