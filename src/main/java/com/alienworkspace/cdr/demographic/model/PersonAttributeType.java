package com.alienworkspace.cdr.demographic.model;

import com.alienworkspace.cdr.demographic.model.audit.AuditTrail;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a person attribute type in the CDR system. This is the entity
 * that represents a person attribute type in the CDR system.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "person_attribute_type")
public class PersonAttributeType extends AuditTrail {

    @Getter
    @Setter
    @Id
    @NotNull
    @Column(name = "person_attribute_type_id", nullable = false)
    private int personAttributeTypeId;
    private String name;
    private String description;
    private String format;
}
