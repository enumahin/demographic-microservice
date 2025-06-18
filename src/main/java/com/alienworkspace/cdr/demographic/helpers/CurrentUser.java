package com.alienworkspace.cdr.demographic.helpers;

import com.alienworkspace.cdr.model.dto.person.PersonDto;
import org.springframework.stereotype.Component;

/**
 * Class to get the current user from the application context.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Component
public final class CurrentUser {

    private CurrentUser() {}

    /**
     * Get the current user from the application context.
     *
     * @return the current user
     */
    public static PersonDto getCurrentUser() {
        return PersonDto.builder()
                .personId(1L)
                .build();
    }
}
