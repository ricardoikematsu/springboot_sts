package com.ikematsu.mapper.custom;

import java.util.Date;

import com.ikematsu.data.dto.v2.PersonDTOV2;
import com.ikematsu.model.Person;
import org.springframework.stereotype.Service;

@Service
public class PersonMapper {

    public PersonDTOV2 convertEntityToDto(Person person) {
        PersonDTOV2 dto = new PersonDTOV2();
        dto.setId(person.getId());
        dto.setAddress(person.getAddress());
        dto.setBirthDay(new Date());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setGender(person.getGender());
        return dto;
    }


    public Person convertDtoTOEntity(PersonDTOV2 person) {
        Person entity = new Person();
        entity.setId(person.getId());
        entity.setAddress(person.getAddress());
        //vo.setBirthDay(new Date());
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setGender(person.getGender());
        return entity;
    }

}

