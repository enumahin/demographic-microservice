package com.alienworkspace.cdr.demographic.service;

import com.alienworkspace.cdr.model.dto.person.PersonDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@AllArgsConstructor
@Service
public class DataIngestionService {

    private static final Logger log = LoggerFactory.getLogger(DataIngestionService.class);

    private final ObjectMapper objectMapper;

    private final PersonService personService;

    @Bean
    public Consumer<PersonDto> personConsumer() {
        return personDto -> {
            log.info("Consuming personDto: {}", personDto);
            personService.addPerson(personDto);
        };
    }
}
