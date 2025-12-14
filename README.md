# MovieLibrary-Management

**MovieLibrary-Management** is a small Spring Boot application that provides a REST API to manage a simple movie library. It stores movies in a relational database and asynchronously enriches movie records with IMDb ratings using the OMDB API.

---

## Overview

The application exposes CRUD endpoints for movies and a background enrichment service that fetches IMDb ratings (via OMDB). When a movie is created or its title changes, an asynchronous job is triggered to fetch and persist the movie rating.

Swagger / OpenAPI documentation is available when the application is running locally.

---

## Swagger / OpenAPI

* **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

The root path `/` redirects:

* ADMIN users → Swagger UI
* Non-admin users → `/api/movies`

---

## Tech Stack

* **Java 17**
* **Spring Boot 4**
* Spring Web, Spring Data JPA, Spring Security, Validation
* **MariaDB** (JDBC driver included)
* **SpringDoc OpenAPI** (Swagger UI)
* **Gradle** (with wrapper)
* **Lombok** (compileOnly)

---

## Local Setup & Run

### Prerequisites

* JDK 17
* (Optional) MariaDB running for persistent storage

    * Alternatively, configure an in-memory database in `application.properties`

### Build & Run

From the project root:

Build and run tests:

```bash
./gradlew clean build
```

Run the application:

```bash
./gradlew bootRun
```

Or run the generated JAR (located in `build/libs/`):

```bash
java -jar build/libs/<your-artifact>.jar
```

Default server port: **8080**

---

## Configuration

Configuration is located in:

```
src/main/resources/application.properties
```

Key properties:

* `spring.datasource.url` (default: `jdbc:mariadb://localhost:3306/movie_library`)
* `spring.datasource.username`
* `spring.datasource.password`
* `spring.jpa.hibernate.ddl-auto` (default: `update`)
* `server.port` (default: `8080`)
* `omdb.api.key`
* `springdoc.paths-to-match`

> ⚠️ The repository contains a **sample OMDB API key**:
> `omdb.api.key=1ee4a302`
>
> Replace this with your own key for production use.

All properties can be overridden via environment variables or profile-specific configuration files.

---

## Security & Authentication

Spring Security is enabled.

* **ADMIN role** is required for create, update, and delete operations.
* Read-only endpoints are accessible to non-admin users.

Refer to:

```
src/main/java/com/example/movielibrarymanagement/security/
```

for details about authentication mechanism, roles, and credentials.

---
