## Pre-create a Database
```shell
mysql --user=root -e 'create database userData'
mysql --user=root -e "GRANT ALL ON userData.* TO shsha@'%'"
```

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
JDATA='{"title": "Spring Boot Guide", "author": "John Doe", "price": 29.90, "genre": "Programming"}'
curl -s http://localhost:8080/createBook \
  --header 'Content-Type: application/json' \
  --data "$JDATA" |jq

JDATA='{"title": "Spring Boot 3.5", "author": "Jack Doe", "price": 19.49, "genre": "Programming"}'
# repeat curl
JDATA='{"title": "Essential Spring Boot", "author": "Jane Doe", "price": 39.99, "genre": "Spring Boot"}'
# repeat curl

curl -s http://localhost:8080/genre/Programming   | jq
curl -s http://localhost:8080/genre/Spring%20Boot | jq
```
```json
{
  "title": "Spring Boot Guide",
  "author": "John Doe",
  "price": 29.9,
  "genre": "Programming",
  "id": "6835fd96c82825fe0bab3540"
}
---
[
  {
    "title": "Spring Boot Guide",
    "author": "John Doe",
    "price": 29.9,
    "genre": "Programming",
    "id": "6835fd41c82825fe0bab3540"
  },
  {
    "title": "Spring Boot 3.5",
    "author": "Jack Doe",
    "price": 19.49,
    "genre": "Programming",
    "id": "6835fd61c82825fe0bab3541"
  }
]
---
[
  {
    "title": "Essential Spring Boot",
    "author": "Jane Doe",
    "price": 39.99,
    "genre": "Programming",
    "id": "6835fdedc82825fe0bab3542"
  }
]
```
Using id(s) above you can also
* update using PUT `/updateBook/{id}`
* DELETE `/deleteBook/{id}`

## Use `mongosh`
* Provided you started container `mongo` using `docker compose up -d`,
you can run `mongosh` using this very container interactively:
```
$ docker exec -it mongo mongosh -u root -p simple-1

test> show databases
admin    100.00 KiB
config   108.00 KiB
library   72.00 KiB
local     72.00 KiB

test> use library
switched to db library

library> show collections
books

library> db.books.find()
```
* Output:
```json
[
  {
    _id: ObjectId('6835fd41c82825fe0bab3540'),
    title: 'Spring Boot Guide',
    author: 'John Doe',
    price: 29.99,
    genre: 'Programming',
    _class: 'com.project.code.Book'
  },
  {
    _id: ObjectId('6835fd61c82825fe0bab3541'),
    title: 'Spring Boot 3.5',
    author: 'Jack Doe',
    price: 19.49,
    genre: 'Programming',
    _class: 'com.project.code.Book'
  },
  {
    _id: ObjectId('6835fd96c82825fe0bab3542'),
    title: 'Spring Boot Guide',
    author: 'John Doe',
    price: 29.9,
    genre: 'Programming',
    _class: 'com.project.code.Book'
  }
]
```
* Exit with Ctrl-D
* Destroy containers and their volumes
  ```
  docker compose down -v
  ```
