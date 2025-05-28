package com.project.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book createBook(String title, double price, String authorName) {
        Author author = new Author(authorName);
        Book book = new Book(title, price, author);
        return bookRepository.save(book);
    }

    public Book getBookByTitle(String title) {
        return bookRepository.findByTitle(title);
    }
}
