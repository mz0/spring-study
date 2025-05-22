Run using 'prod' profile:
```bash
export DB_USERNAME=shsha
export DB_PASSWORD=999

mvn spring-boot:run # -Dspring-boot.run.profiles=prod
```

Open in browser:
```
http://localhost:8080/api/databases
http://localhost:8080/api/databases/bookstore/tables
```

Or with *curl*
```
curl -s http://localhost:8080/api/databases/bookstore/tables | jq
```

Health-check
```
curl -s http://localhost:8080/actuator/health | jq
```
