```
ERROR o.a.c.c.C.[.[.[/].[dispatcherServlet] : Servlet.service() for servlet [dispatcherServlet]
  in context with path [] threw exception
  [Request processing failed: org.hibernate.HibernateException: No CurrentSessionContext configured] with root cause

org.hibernate.HibernateException: No CurrentSessionContext configured
	at org.hibernate.internal.SessionFactoryImpl.getCurrentSession(SessionFactoryImpl.java:660)
  ~[hibernate-core-6.6.15.Final.jar:6.6.15.Final]
```
The error `org.hibernate.HibernateException: No CurrentSessionContext configured` indicates that when you call
`sessionFactory.getCurrentSession()`, Hibernate doesn't have a defined strategy for managing what the "current" session is.

In a Spring Boot application, especially one like yours using `spring-boot-starter-data-jpa`,
session and transaction management are typically handled by Spring itself. Your `HibernateUtil` class is correctly set up
as a Spring component and successfully initializes the `SessionFactory`.

The issue arises from how and where sessionFactory.getCurrentSession() is subsequently called.

Here's a breakdown of the cause and the recommended solution:
1. getCurrentSession() Behavior: The sessionFactory.getCurrentSession() method relies on a configured
"current session context." Hibernate needs to know how to scope these sessions
(e.g., one session per transaction, one per thread).
2. Spring's Role: Spring Boot, with its JPA and transaction auto-configuration,
integrates its transaction management with Hibernate's session management.
When you use Spring's `@Transactional` annotation, Spring ensures that a Hibernate Session is created
(or retrieved) and bound to the current transaction. Within this Spring-managed transactional context,
`getCurrentSession()` works because Spring provides the necessary context.
3. Likely Cause: You are most likely calling `HibernateUtil.getSessionFactory().getCurrentSession()` in a part
of your code that is not currently executing within an active Spring-managed transaction.

## Recommended Solution: Embrace Spring's Transaction Management (Idiomatic Approach)
This is the standard and most robust way to handle database interactions in Spring Boot.
1. Use `@Transactional`: Annotate your service methods (or any methods performing database operations
that need to be atomic) with `@org.springframework.transaction.annotation.Transactional`.
2. Inject `EntityManager` or `SessionFactory`: Instead of relying on your static `HibernateUtil.getSessionFactory()`,
inject the `jakarta.persistence.EntityManager` (JPA standard and generally preferred) or `org.hibernate.SessionFactory`
(if you need Hibernate-specific features) directly into your Spring-managed beans (e.g., services, repositories).

Here's an example of how you would typically structure your service layer:
```java
package com.project.code.service;

import com.project.code.entity.Book;

@Service
public class BookService {

    private final EntityManager entityManager;

    @Autowired
    public BookService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional // Spring will manage the transaction and session lifecycle
    public void performDatabaseOperation(/* parameters */) {
        // To get the Hibernate Session from EntityManager:
        Session session = entityManager.unwrap(Session.class);

        Book bki = session.get(Book.class, someId);
        if (bki != null) {
             bki.setGenre(newValue);
             session.persist(bki); // or session.merge(bki)
        }
        // session.persist(newEntityObject);

        // If you had injected SessionFactory directly (less common with JPA focus):
        // Session session = sessionFactory.getCurrentSession();
        // session.persist(newEntityObject);
    }

    @Transactional(readOnly = true) // Optimize for read-only operations
    public Book findEntityById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        // return session.get(YourEntity.class, id);
        System.out.println("Finding entity by ID: " + id);
        return null; // Replace with actual find logic
    }
}
```

Why this works: When you invoke a method annotated with `@Transactional` (like `performDatabaseOperation`),
Spring AOP intercepts the call.

It starts a database transaction and makes a Hibernate Session available and bound to that transaction.

Any call to `entityManager.unwrap(Session.class)` or `sessionFactory.getCurrentSession()` within this method
will then correctly retrieve the active session.
Upon method completion, Spring will commit the transaction (if successful) or roll it back (if an exception occurs).

Regarding [HibernateUtil.java](src/main/java/com/project/code/HibernateUtil.java):
The class itself is correctly initializing the `SessionFactory` because it's a Spring `@Component`
and its constructor is invoked with the `EntityManagerFactory`.
No changes are strictly required in `HibernateUtil.java` to fix the `NoCurrentSessionContext` error,
as the fix lies in how and where `sessionFactory.getCurrentSession()` is invoked.

However, by adopting the dependency injection pattern shown above, you often reduce or eliminate the need
for a static utility like `HibernateUtil` to obtain the `SessionFactory` for typical business logic,
leading to more testable and idiomatic Spring code.

Alternative (Not Recommended for typical *Spring Boot* applications): If you were to use
`sessionFactory.getCurrentSession()` outside Spring's transaction management,
you would need to explicitly configure the `hibernate.current_session_context_class` property in your
`application.properties`.

For example: `spring.jpa.properties.hibernate.current_session_context_class=thread`
This would make Hibernate manage sessions per thread, and you'd be responsible for their lifecycle.

This approach generally conflicts with Spring's more comprehensive transaction management and is not advised.
Spring Boot typically auto-configures an appropriate `SpringSessionContext`
that integrates with its transaction manager, so the issue is almost always about
calling `getCurrentSession()` outside an active Spring-managed transaction.

In summary: The most effective way to resolve the `NoCurrentSessionContext` error is to ensure
your Hibernate session operations occur within methods managed by Spring's declarative transaction mechanism
(`@Transactional`) and to use dependency injection for `EntityManager` or `SessionFactory`.
