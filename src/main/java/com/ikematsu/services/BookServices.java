package com.ikematsu.services;

import com.ikematsu.controllers.BookController;
import com.ikematsu.data.dto.v1.BookDTO;
import com.ikematsu.exceptions.RequiredObjectIsNullException;
import com.ikematsu.exceptions.ResourceNotFoundException;
import com.ikematsu.mapper.DozerMapper;
import com.ikematsu.model.Book;
import com.ikematsu.repositories.BookRepository;
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
public class BookServices {
    private final Logger logger = Logger.getLogger(BookServices.class.getName());

    @Autowired
    BookRepository repository;

    @Autowired
    PagedResourcesAssembler<BookDTO> assembler;

    public PagedModel<EntityModel<BookDTO>> findAll(Pageable pageable) {

        logger.info("Finding all books!");

        var bookPage = repository.findAll(pageable);

        var bookDtosPage = bookPage.map(p -> DozerMapper.parseObject(p, BookDTO.class));
        bookDtosPage.map(
                p -> p.add(
                        linkTo(methodOn(BookController.class)
                                .findById(p.getKey())).withSelfRel()));

        Link link = linkTo(
                methodOn(BookController.class)
                        .findAll(pageable.getPageNumber(),
                                pageable.getPageSize(),
                                "asc")).withSelfRel();

        return assembler.toModel(bookDtosPage, link);
    }

    public BookDTO findById(Long id) {

        logger.info("Finding one book!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var dto = DozerMapper.parseObject(entity, BookDTO.class);
        dto.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return dto;
    }

    public BookDTO create(BookDTO book) {

        if (book == null ) throw new RequiredObjectIsNullException();
        logger.info("Creating one book!");
        var entity = DozerMapper.parseObject(book, Book.class);
        var dto =  DozerMapper.parseObject(repository.save(entity), BookDTO.class);
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getKey())).withSelfRel());
        return dto;
    }

    public BookDTO update(BookDTO book) {

        if (book == null ) throw new RequiredObjectIsNullException();
        logger.info("Updating one book!");
        var entity = repository.findById(book.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());

        var dto =  DozerMapper.parseObject(repository.save(entity), BookDTO.class);
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getKey())).withSelfRel());
        return dto;
    }

    public void delete(Long id) {

        logger.info("Deleting one person!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);
    }
}