package com.alienworkspace.cdr.demographic.model;

import com.alienworkspace.cdr.demographic.model.audit.AuditTrail;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the address of a person.
 *
 * <p>This is the entity that
 * represents the address of a person in the CDR system.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Builder
@Entity(name = "person_address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class PersonAddress extends AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "person_address_id")
    private long personAddressId;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    private boolean preferred;

    @Column(name = "country_id")
    private int country;

    @Column(name = "state_id")
    private int state;

    @Column(name = "county_id")
    private int county;

    @Column(name = "city_id")
    private int city;

    @Column(name = "community_id")
    private int community;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "address_line_3")
    private String addressLine3;

    private String landmark;

    private long longitude;

    private long latitude;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
}
