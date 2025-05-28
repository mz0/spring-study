package com.project.code;

import org.springframework.data.neo4j.core.schema.*;

@Node
public class Book {
    @Id @GeneratedValue
    private Long elementId;

    private String title;

    @Relationship(type = "WRITTEN_BY", direction = Relationship.Direction.OUTGOING)
    private Author author;

    private double price;

    public Book(String title, double price, Author author) {
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public Book() {}

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }
    public void setAuthor(Author author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}
