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
JDATA='{"username": "john_doe", "email": "john.doe@example.com", "profileBio": "Software Developer"}'
curl -s http://localhost:8080/createUser \
  --header 'Content-Type: application/json' \
  --data "$JDATA"

curl -s 'http://localhost:8080/getBook/1' | jq

JDATA='{"username": "jane_doe", "email": "jane@example.org", "profileBio": "UX Expert"}'
curl -s http://localhost:8080/createUser \
  --header 'Content-Type: application/json' \
  --data "$JDATA"

User and profile created OK

JDATA='{"content": "This is a new post!", "userId": 1}'
curl -s http://localhost:8080/createPost \
  --header 'Content-Type: application/json' \
  --data "$JDATA"

Post created OK

curl -s 'http://localhost:8080/getPosts/1' | jq
```
```json
[
  {
    "id": 1,
    "content": "This is a new post!",
    "user": {
      "id": 1,
      "username": "john_doe",
      "email": "john.doe@example.com",
      "profile": {
        "id": 1,
        "bio": "Software Developer",
        "user": 1
      },
      "posts": [
        {
          "id": 1,
          "content": "This is a new post!",
          "user": 1
        }
      ]
    }
  }
]
```
