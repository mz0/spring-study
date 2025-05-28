package com.project.code;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface BookRepository extends Neo4jRepository<Book, Long> {
    Book findByTitle(String title);
}
