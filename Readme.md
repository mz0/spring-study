Run using 'dev' profile (set in `application.properties`):
```bash
export DB_USERNAME=shsha
export DB_PASSWORD=999
export DB_HOST=localhost

mvn spring-boot:run # -Dspring-boot.run.profiles=prod
```
When you try to start the spring-boot app using the command above,
if you get an error stating that "Port 8080 is already in use",
run the following command in the terminal to kill the process on port 8080.
```bash
fuser -k 8080/tcp
```

With *curl*
```
BOOK='{ "title": "Spring Boot Essentials", "author": "Jane Doe", "price": 21.95 }'

curl -s http://localhost:8080/book \
  --header 'Content-Type: application/json' \
  --data "$BOOK"

BOOK='{"title": "Spring Boot 2Ed", "author": "Jane & John Doe", "price": 28.99}'
curl -s http://localhost:8080/book \
  --header 'Content-Type: application/json' \
  --data "$BOOK"

curl -s 'http://localhost:8080/book' | jq
curl -s 'http://localhost:8080/book/1' | jq

curl -s --request PUT 'http://localhost:8080/book/2' \
  --header 'Content-Type: application/json' \
  --data '{"title":"Spring Boot 3", "author":"John \"Boots\" Doe", "price":10.99, "genre":"Java"}' | jq

curl --request DELETE 'http://localhost:8080/book/1'
```

Health-check
```
curl -s http://localhost:8080/actuator/health | jq
```
