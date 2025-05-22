Run using 'prod' profile:
```bash
export DB_USERNAME=shsha
export DB_PASSWORD=999

mvn spring-boot:run # -Dspring-boot.run.profiles=prod
```

With *curl*
```
curl -s http://localhost:8080/books/addBook \
  --header 'Content-Type: application/json' \
  --data '{
        "title": "Spring Boot Essentials",
        "author": "Jane Doe",
        "price": 19.99
    }
    ' | jq

BOOK2='{"title": "Spring Boot 2ed", "author": "Jane & John Doe", "price": 28.99}'
curl -s http://localhost:8080/books/addBook \
  --header 'Content-Type: application/json' \
  --data "$BOOK2" | jq

curl -s 'http://localhost:8080/books/getBooks' | jq
curl -s 'http://localhost:8080/books/getBook/1' | jq

curl -s --request PUT 'http://localhost:8080/books/updateBook/2' \
  --header 'Content-Type: application/json' \
  --data '{"title":"Spring Boot 3","author": "John", "price":"10.99"}' | jq

curl --request DELETE 'http://localhost:8080/books/deleteBook/1'
```

Health-check
```
curl -s http://localhost:8080/actuator/health | jq
```
