# Hibernate ORM

## Key components and concepts

* Session - Short-lived..
* SessionFactory - Creates Hibernate sessions and manages configurations.
* Transaction -	Ensure atomicity of database changes
* Query - *HQL* (Hibernate Query Language) or *SQL*
* Entity Class - *POJO* (Plain Old Java Object) mapped to a specific database table.
* Configuration - Hibernate settings from `hibernate.cfg.xml`

ORM main advantages:
* trivial ResultSet to (one or more) *POJO*(s) mapping
* simpler relationship management between Entities (database tables)

## Basic Hibernate Annotations
* @Transient - not persisted
* @Enumerated - `@Enumerated(ProductStatus.String) private ProductStatus status;`
* @Lob - Large Object, e.g. `@Lob private String productDescription; // may be well over default 255 VARCHAR limit`
* @Temporal e.g. `@Temporal(TemporalType.TIMESTAMP) private java.util.Date eventDate;`

* @OneToOne
  ```java
  @Entity
  public class User {
    @Id .. private Long id;

    @OneToOne(cascade = CascadeType.ALL) @JoinColumn(name = "profile_id", referecedColumn = "id")
    private Profile profile;
  }
  ```
* @OneToMany
  ```java
  @Entity
  public class Customer {
    @Id .. private Long id;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;
    ...
  }

  @Entity
  public class Order {
    @Id .. private Long id;

    @ManyToOne @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    ...
  }
  ```
* @ManyToMany
  ```java
  @Entity
  public class Student {
    @Id .. private Long id;

    @ManyToMany
    @JoinTable(name = "student_course",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Customer customer;
    ...
  }

  @Entity
  public class Course {
    @Id .. private Long id;

    @ManyToMany(mappedBy = "courses")
    private List<Student> students;
  }
  ```

## SQL and HQL
* SQL
  ```java
  @PersistenceContext
  private EntityManager em;

  public List<Object[]> getBooksByGenre(String genre) {
    String sql = "SELECT id, title, price FROM books WHERE genre = ?";
    Query query = em.createNativeQuery(sql);
    query.setParameter(1, genre);
    return query.getResultList();
  }
  ```
* HQL
  ```java
  @PersistenceContext
  private EntityManager em;

  public List<Book> getBooksByGenre(String genre) {
    String hql = "FROM Book b WHERE b.genre = :genre";
    Query query = em.createNativeQuery(hql, Book.class);
    query.setParameter("genre", genre);
    return query.getResultList();
  }
  ```
  also
  ```java
  @Repository
  public interface BookRepository extends JpaRepository<Book, Long> {
    @Qurey("FROM Book b WHERE b.price > :price")
    List<Book> findExpensiveBooks(@Param("price") double price);
  }
  ```

### HQL Exceptions
* QuerySyntaxException
  * using table names - use only Entities; using column names - use property names
  * using SQL constructs, like `*`
  * missing Entity alias
* LazyInitializationException - avoiding
  * access within an open Transaction
  * Or use `JOIN FETCH`
