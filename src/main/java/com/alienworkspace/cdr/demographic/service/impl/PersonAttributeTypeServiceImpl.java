package com.alienworkspace.cdr.demographic.service.impl;

import com.alienworkspace.cdr.demographic.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.demographic.helpers.CurrentUser;
import com.alienworkspace.cdr.demographic.model.mapper.PersonAttributeTypeMapper;
import com.alienworkspace.cdr.demographic.repository.PersonAttributeTypeRepository;
import com.alienworkspace.cdr.demographic.service.PersonAttributeTypeService;
import com.alienworkspace.cdr.model.dto.person.PersonAttributeTypeDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link PersonAttributeTypeService} interface.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Service
@AllArgsConstructor
public class PersonAttributeTypeServiceImpl implements PersonAttributeTypeService {

    private final PersonAttributeTypeRepository personAttributeTypeRepository;
    private final PersonAttributeTypeMapper personAttributeTypeMapper;

    @Override
    public PersonAttributeTypeDto savePersonAttributeType(PersonAttributeTypeDto personAttributeTypeDto) {
        return personAttributeTypeMapper.toDto(
                personAttributeTypeRepository.save(personAttributeTypeMapper.toEntity(personAttributeTypeDto)));
    }

    @Override
    public PersonAttributeTypeDto updatePersonAttributeType(int id, PersonAttributeTypeDto personAttributeTypeDto) {
        return personAttributeTypeRepository.findById(id)
                .map(personAttributeType -> {
                    personAttributeType.setFormat(personAttributeTypeDto.getFormat());
                    personAttributeType.setLastModifiedAt(LocalDateTime.now());
                    personAttributeType.setLastModifiedBy(CurrentUser.getCurrentUser().getPersonId());
                    return personAttributeTypeRepository.save(personAttributeType);                })
                .map(personAttributeTypeMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Person attribute type not found"));
    }

    @Override
    public void deletePersonAttributeType(int id, RecordVoidRequest resourceVoidRequest) {
        personAttributeTypeRepository.findById(id)
                .map(personAttributeType -> {
                    personAttributeType.setVoided(true);
                    personAttributeType.setVoidedAt(LocalDateTime.now());
                    personAttributeType.setVoidedBy(CurrentUser.getCurrentUser().getPersonId());
                    personAttributeType.setVoidReason(resourceVoidRequest.getVoidReason());
                    return personAttributeTypeRepository.save(personAttributeType);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Person attribute type not found"));
    }

    @Override
    public PersonAttributeTypeDto getPersonAttributeTypeById(int personAttributeTypeId) {
        return personAttributeTypeRepository.findById(personAttributeTypeId)
                .map(personAttributeTypeMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Person attribute type not found"));
    }

    @Override
    public List<PersonAttributeTypeDto> getAllPersonAttributeTypes() {
        return personAttributeTypeRepository.findAll().stream()
                .filter(personAttributeType -> !personAttributeType.isVoided())
                .map(personAttributeTypeMapper::toDto).toList();
    }
}
