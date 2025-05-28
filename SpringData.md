# Spring Data

Spring Data `@Repository` lets us declaratively define most common CRUD operations
by writing only a Java interface, for which Spring Data generates an implementation.
```java
import org.springframework.data.influxdb.repository.InfluxDBRepository;

public interface MyMeasurementRepository extends InfluxDBRepository<MyMeasurement, String> {
    List<MyMeasurement> findByTagsHost(String host);
}
```
Spring Data creates a proxy instance that implements this repository interface.
That proxy handles the interaction with the database, translating your method calls
like `findByFoo("bar")` into actual database queries.

Spring Data may use
* SQL (Relational) Databases:
  * (low-level Spring Data JDBC) - you have to extend `PagingAndSortingRepository` > `CrudRepository` ...
  * (Spring JPA) `JpaRepository` extends `PagingAndSortingRepository` > `CrudRepository` > `QueryByExampleExecutor`
    Implements JPA-specific methods: flush(), saveAndFlush(), getOne(),
    findAll() with Sort or Pageable, and batch operations.
* NoSQL - Document Databases:
  * MongoDB: `MongoRepository`
  * Elasticsearch: `ElasticsearchRepository`
  * Apache Solr: `SolrCrudRepository`
  * Azure Cosmos DB: `CosmosRepository`
* NoSQL - Key-Value Stores:
  * Redis: `CrudRepository` (used with Spring Data Redis, though *direct template* usage is also common)
  * Hazelcast: `HazelcastRepository`
  * GemFire/Geode: `GemfireRepository`
* NoSQL - Graph Databases: Neo4j: `Neo4jRepository`
* NoSQL - Column-Family Databases:
  * Apache Cassandra: `CassandraRepository`
* NoSQL - Time Series DB: InfluxDB `InfluxDBRepository`
* LDAP: `LdapRepository`

## Common Spring Data keywords/prefixes and patterns
* `find...By` / `read...By` / `get...By` / `query...By` / `stream...By`: retrieve entities.
  Example: findByTitle(String title), findDistinctByAuthor(String author)
* count...By: count the number of entities matching the criteria.
  `countByAuthor(String author)`
* `exists...By`: check if an entity matching the criteria exists. Return `boolean`. `existsByIsbn(String isbn)`
* `delete...By` / `remove...By`: delete entities matching the criteria.
    * Example: deleteByTitle(String title); return the number of deleted entities or a list of deleted entities
      depending on the specified return type.

Property Expressions
* You can chain property names: `findByAuthorLastName(String lastName)` assuming Author has a lastName property.
* You can traverse nested properties: `findByAuthorAddressZipCode(String zipCode)`
  assuming Book has an Author object, which has an `Address` object, which has a `zipCode` property.

Conditional Keywords
* And: findByAuthorAndTitle(String author, String title)
* Or: findByAuthorOrTitle(String author, String title)
* Is, Equals: findByTitleIs(String title) (often implicit)
* Not: findByTitleNot(String title)
* IsNull, IsNotNull (or NotNull): findByPublicationDateIsNull()
* Like, NotLike, StartingWith, EndingWith, Containing: findByTitleContaining(String keyword)
* IgnoreCase: findByTitleIgnoreCase(String title)
* LessThan, LessThanEqual, GreaterThan, GreaterThanEqual: findByPageCountGreaterThan(int count)
* Between: findByPublicationDateBetween(Date startDate, Date endDate)
* In, NotIn: findByGenreIn(Collection<String> genres)
* True, False: findByIsPublishedTrue()

Ordering
* OrderBy...Asc / OrderBy...Desc: findByAuthorOrderByTitleAsc(String author)

Limiting Results
* findFirst...By or findTop...By: findFirstByOrderByPublicationDateDesc() - get the most recently published book
* findFirstN...By or findTopN...By: findTop5ByOrderByPageCountDesc() - get the top 5 longest books
