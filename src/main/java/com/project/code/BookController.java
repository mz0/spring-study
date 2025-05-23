package com.project.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    private final BookValidator bookValidator = new BookValidator();

    // Create a new book
    @PostMapping("/addBook")
    public String addBook(@Valid @RequestBody Book book, BindingResult result) {
        bookValidator.validate(book, result);

        if (result.hasErrors()) {
            return "Validation failed: " + result.getAllErrors();
        }
        bookRepository.save(book);
        return "Book added successfully";
    }
}
