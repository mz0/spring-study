package com.project.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.*;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    private final BookValidator bookValidator = new BookValidator();

    // Create a new book
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createBook(@Valid @RequestBody Book book, BindingResult result) {
        bookValidator.validate(book, result);

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            ApiResponse<List<String>> invalid = new ApiResponse<>("error", "Validation failed", errors);
            return new ResponseEntity<>(invalid, HttpStatus.BAD_REQUEST);
        }
        var sb = bookRepository.save(book);
        ApiResponse<Book> response = new ApiResponse<>("success", "Book created successfully", sb);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book update) {
        Book book = bookRepository.findById(id).orElseThrow();
        book.setTitle(update.getTitle());
        book.setAuthor(update.getAuthor());
        book.setPrice(update.getPrice());
        book.setGenre(update.getGenre());
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }
}
