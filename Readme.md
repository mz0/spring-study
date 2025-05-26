## WARNING: see [BROKEN](BROKEN.md)

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
```bash
BOOK='{ "title": "Spring Boot Essentials", "author": "Jane Doe", "price": 21.95 }'

curl -s http://localhost:8080/saveBook \
  --header 'Content-Type: application/json' \
  --data "$BOOK"

BOOK='{"title": "Spring Boot 2Ed", "author": "Jane & John Doe", "price": 28.99}'
curl -s http://localhost:8080/saveBook \
  --header 'Content-Type: application/json' \
  --data "$BOOK"

curl -s 'http://localhost:8080/getBook/1' | jq
curl -s 'http://localhost:8080/getBooks' | jq
```
The last two calls results:
```json
{
  "id": 1,
  "title": "Spring Boot Essentials",
  "author": "Jane Doe",
  "price": 21.95,
  "genre": null
}
```
and
```json
[
  {
    "id": 1,
    "title": "Spring Boot Essentials",
    "author": "Jane Doe",
    "price": 21.95,
    "genre": null
  },
  {
    "id": 2,
    "title": "Spring Boot 2Ed",
    "author": "Jane & John Doe",
    "price": 28.99,
    "genre": null
  }
]
```

Health-check
```bash
curl -s http://localhost:8080/actuator/health | jq
```
produces
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "MySQL",
        "validationQuery": "isValid()"
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
