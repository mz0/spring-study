**NB:** Use `mvn dependency:tree`, `mvn dependency:list`, `mvn dependency:analyze` to check dependencies and versions

## Start all DB servers: MySQL, MongoDB, Neo4j.
```shell
docker compose up -d
```

Run using 'dev' profile (set in `application.properties`):
```bash
mvn clean pring-boot:run # -Dspring-boot.run.profiles=prod
```
When you try to start the spring-boot app using the command above,
if you get an error stating that "Port 8080 is already in use",
run the following command in the terminal to kill the process on port 8080.
```bash
fuser -k 8080/tcp
```

With *curl*
```bash
curl 'http://localhost:8080/createbook?title=Pride%20and%20Prejudice&authorName=Jane%20Austen&price=19.99' \
--header 'Content-Type: application/json' \
--data '{
  "id": "1",
  "bookId": "book-123",
  "copiesSold": 10,
  "timestamp": "2025-03-13T12:34:56Z"
}
' | jq

# Get Author name
curl http://localhost:8080/getBookAuthor/Pride%20and%20Prejudice
```
```json
{
  "title": "Pride and Prejudice",
  "author": {
    "name": "Jane Austen"
  },
  "price": 19.99,
  "genre": "n/a"
}
```
Using id(s) above you can also
* update using PUT `/updateBook/{id}`
* DELETE `/deleteBook/{id}`

## Neo4j CLI tool `cypher-shell`
[Cypher Manual](https://neo4j.com/docs/cypher-manual/current/)
```bash
docker exec -it neo4j cypher-shell -u neo4j -p securepassword112 --uri bolt://localhost:7687

Connected to Neo4j using Bolt protocol version 5.8 at bolt://localhost:7687 as user neo4j.
Note that Cypher queries must end with a semicolon.

neo4j@neo4j> show databases;
+------------------------------------------------------------------------+
| name     | type       | access       | currentStatus | default | home  |
+------------------------------------------------------------------------+
| "neo4j"  | "standard" | "read-write" | "online"      | TRUE    | TRUE  |
| "system" | "system"   | "read-write" | "online"      | FALSE   | FALSE |
+------------------------------------------------------------------------+

neo4j@neo4j> :use neo4j
neo4j@neo4j> CALL db.labels();
+----------+
| label    |
+----------+
| "Book"   |
| "Author" |
+----------+

neo4j@neo4j> CALL db.relationshipTypes();
+------------------+
| relationshipType |
+------------------+
| "WRITTEN_BY"     |
+------------------+

neo4j@neo4j> CALL db.propertyKeys();
+-------------+
| propertyKey |
+-------------+
| "title"     |
| "price"     |
| "name"      |
+-------------+

neo4j@neo4j> MATCH (b:Book) RETURN count(b);
...
neo4j@neo4j> MATCH ()-[r:WRITTEN_BY]->() RETURN count(r);
...
neo4j@neo4j>:exit
```

## Health-check
```bash
curl -s http://localhost:8080/actuator/health | jq
```
produces
```json
{
  "status": "UP",
  "components": {
    "neo4j": {
      "status": "UP",
      "details": {
        "server": "2025.04.0@localhost:7687",
        "edition": "community",
        "database": "neo4j"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 999888777000,
        "free": 789888777000,
        "threshold": 10485760,
        "path": "~/com.project/code/.",
        "exists": true
      }
    },
    "ping": {
      "status": "UP"
    },
    "ssl": {
      "status": "UP",
      "details": {
        "validChains": [],
        "invalidChains": []
      }
    }
  }
}
```
* Exit with Ctrl-D
* Destroy containers and their volumes
  ```
  docker compose down -v
  ```
