package com.ikematsu.services;

import com.ikematsu.controllers.PersonController;
import com.ikematsu.data.dto.v1.PersonDTO;
import com.ikematsu.exceptions.RequiredObjectIsNullException;
import com.ikematsu.exceptions.ResourceNotFoundException;
import com.ikematsu.mapper.DozerMapper;
import com.ikematsu.model.Person;
import com.ikematsu.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    PagedResourcesAssembler<PersonDTO> assembler;

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {

        logger.info("Finding all people!");

        var personPage = repository.findAll(pageable);

        var personDtosPage = personPage.map(p -> DozerMapper.parseObject(p, PersonDTO.class));
        personDtosPage.map(
                p -> p.add(
                        linkTo(methodOn(PersonController.class)
                                .findById(p.getKey())).withSelfRel()));

        Link link = linkTo(
                methodOn(PersonController.class)
                        .findAll(pageable.getPageNumber(),
                                pageable.getPageSize(),
                                "asc")).withSelfRel();

        return assembler.toModel(personDtosPage, link);
    }

    public PagedModel<EntityModel<PersonDTO>> findPersonByName(String firstname, Pageable pageable) {

        logger.info("Finding people by id!");

        var personPage = repository.findPersonByName(firstname, pageable);

        var personDtosPage = personPage.map(p -> DozerMapper.parseObject(p, PersonDTO.class));
        personDtosPage.map(
                p -> p.add(
                        linkTo(methodOn(PersonController.class)
                                .findById(p.getKey())).withSelfRel()));

        Link link = linkTo(
                methodOn(PersonController.class)
                        .findAll(pageable.getPageNumber(),
                                pageable.getPageSize(),
                                "asc")).withSelfRel();

        return assembler.toModel(personDtosPage, link);
    }

    public PersonDTO findById(Long id) {

        logger.info("Finding one person!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var dto = DozerMapper.parseObject(entity, PersonDTO.class);
        dto.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return dto;
    }

    public PersonDTO create(PersonDTO person) {

        if (person == null ) throw new RequiredObjectIsNullException();
        logger.info("Creating one person!");
        var entity = DozerMapper.parseObject(person, Person.class);
        var dto =  DozerMapper.parseObject(repository.save(entity), PersonDTO.class);
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getKey())).withSelfRel());
        return dto;
    }

//    public PersonDTOV2 createV2(PersonDTOV2 person) {
//
//        if (person == null ) throw new RequiredObjectIsNullException();
//        logger.info("Creating one person with V2!");
//        var entity = mapper.convertDtoTOEntity(person);
//        var dto =  mapper.convertEntityToDto(repository.save(entity));
//        return dto;
//    }

    public PersonDTO update(PersonDTO person) {

        if (person == null ) throw new RequiredObjectIsNullException();
        logger.info("Updating one person!");
        var entity = repository.findById(person.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var dto =  DozerMapper.parseObject(repository.save(entity), PersonDTO.class);
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getKey())).withSelfRel());
        return dto;
    }

    @Transactional
    public PersonDTO disblePerson(Long id) {

        logger.info("Disabling one person!");

        repository.disablePerson(id);
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var dto = DozerMapper.parseObject(entity, PersonDTO.class);
        dto.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return dto;
    }

    public void delete(Long id) {

        logger.info("Deleting one person!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);
    }
}