package com.alienworkspace.cdr.demographic.model;

import com.alienworkspace.cdr.demographic.model.audit.AuditTrail;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a person attribute in the CDR system. This is the entity that
 * represents a person attribute in the CDR system.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "person_attribute")
public class PersonAttribute extends AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "person_attribute_id")
    private long personAttributeId;

    @Column(name = "person_id")
    private long personId;

    @Column(name = "person_attribute_type_id")
    private int personAttributeTypeId;

    @Column(name = "attribute_value")
    private String attributeValue;
}
