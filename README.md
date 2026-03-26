# Matrimony App (Spring Boot Scaffold)

This is a ready-to-run Spring Boot scaffold matching your requested package & folder layout.
It compiles, starts, and exposes a few sample endpoints plus Swagger UI.

## Quick Start

```bash
mvn clean package
java -jar target/matrimony-app-0.0.1-SNAPSHOT.jar
```

Open: http://localhost:8080 and Swagger at http://localhost:8080/swagger-ui.html

## Profiles

- `dev`: H2 in-memory, auto schema
- `prod`: Postgres (see `docker-compose.yml`)

## Import in Eclipse

1. File → Import → Existing Maven Projects.
2. Select the project root and finish.
3. Run `MatrimonyApplication` as Spring Boot app.
