# Movie Database Management REST API

Provides a **RESTful API** for managing a movie database. Includes endpoints for creating, retrieving, updating, and deleting **movies**, **genres** and **actors** with many-to-many relationships between them.

## Project Overview

The project is built using **Spring Boot 3.5.6** with **Java 21**. It uses **SQLite** database for data persistence. The API follows **CRUD** principles and provides pagination capabilities for all list endpoints.

### Libraries Used

- [Spring Boot](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot)
    - [Data JPA](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa)
    - [Web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web)
    - [Validation](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation)
    - [Security](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security)
- [SQLite JDBC](https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc)
- [Hibernate Community Dialects](https://mvnrepository.com/artifact/org.hibernate.orm/hibernate-community-dialects)

### Features

- **Basic Authentication** with role-based access control (USER and ADMIN roles)
- **Pagination** support for all list endpoints
- **Filtering and Search** capabilities for movies and actors
- **Many-to-Many Relationships** between movies, genres, and actors
- **Validation** with clear error messages (400 Bad Request for validation errors)
- **Global Exception Handling** with appropriate HTTP status codes
- **Database Management** endpoints for filling and clearing the database

### Structure

```
src/main/java/com/example/movie_api
   ├── configuration          -- configuration classes
   │   └── auth               -- basic authentication configuration
   │       └── BasicAuthConfig.java
   ├── controller             -- REST controllers handling HTTP requests
   │   ├── ActorController.java
   │   ├── ClearController.java
   │   ├── FillController.java
   │   ├── GenreController.java
   │   └── MovieController.java
   ├── dto                    -- data transfer objects
   │   ├── ActorDTO.java / ActorResponseDTO.java
   │   ├── GenreDTO.java / GenreResponseDTO.java
   │   └── MovieDTO.java / MovieResponseDTO.java
   ├── entities               -- JPA entities representing database schema
   │   ├── Actor.java
   │   ├── Genre.java
   │   └── Movie.java
   ├── exception              -- custom exceptions and global exception handler
   │   ├── DatabaseNotEmptyException.java
   │   ├── DeletionConflictException.java
   │   ├── GlobalExceptionHandler.java
   │   └── ResourceNotFoundException.java
   ├── repositories           -- Spring Data JPA repositories
   │   ├── ActorRepository.java
   │   ├── GenreRepository.java
   │   └── MovieRepository.java
   └── service                -- service layer providing business logic
       ├── ActorService.java
       ├── GenreService.java
       └── MovieService.java
src/test/java/com/example/movie_api
   └── Postman.json           -- Postman collection for API testing
```

## Requirements

- **Java 21+**
- **Maven** (for building and running)

## Build and Run

### Using Maven

Requires **Maven** to be installed in your system. Grab it [here](https://maven.apache.org/install.html).

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080` by default.

## Endpoints

### Common CRUD Operations

All entities (Genres, Actors, Movies) support the following operations:

| Method | Endpoint | Description |
|--------|----------|-------------|
| **POST**   | `/api/{entity}`      | Creates a new entity (requires USER or ADMIN role) |
| **GET**    | `/api/{entity}`      | Retrieves all entities with pagination (public) |
| **GET**    | `/api/{entity}/{id}` | Retrieves an entity by ID (public) |
| **PATCH**  | `/api/{entity}/{id}` | Partially updates an entity (requires USER or ADMIN role) |
| **DELETE** | `/api/{entity}/{id}` | Deletes an entity (requires USER or ADMIN role) |

**Pagination Parameters:**
- `page` (default: 0) - Page number (0-indexed)
- `size` (default: 10) - Number of items per page

**Delete Parameters:**
- `force` (default: false) - If `true`, removes relationships before deletion

### Genre Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| **GET** | `/api/genres/{id}/movies` | Retrieves all movies for a specific genre |

### Actor Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| **GET** | `/api/actors?name={name}` | Filters actors by name (partial match, case insensitive) |
| **GET** | `/api/actors/{id}/movies` | Retrieves all movies for a specific actor |

### Movie Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| **GET** | `/api/movies?genre={genreId}` | Filters movies by genre ID |
| **GET** | `/api/movies?year={releaseYear}` | Filters movies by release year |
| **GET** | `/api/movies?actor={actorId}` | Filters movies by actor ID |
| **GET** | `/api/movies/search?title={title}` | Searches movies by title (partial match, case insensitive) |
| **GET** | `/api/movies/{id}/actors` | Retrieves all actors for a specific movie |

### Database Management Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| **POST** | `/api/fill` | Fills the database with sample data (requires ADMIN role, database must be empty) |
| **DELETE** | `/api/clear` | Clears all data from the database (requires ADMIN role) |

**Note:** The `/api/fill` endpoint will return **409 Conflict** if the database is not empty. Use `/api/clear` first to empty the database.

## Permissions

| Role | Access |
| ---- | ------ |
| **Unauthenticated** | `GET` requests only (all endpoints) |
| **USER** | All `GET` requests + `POST`, `PATCH`, `DELETE` for `/api/genres`, `/api/actors`, `/api/movies` |
| **ADMIN** | All endpoints including `/api/fill` and `/api/clear` |

### Default Credentials

- **Username:** `user` / **Password:** `password` (USER role)
- **Username:** `admin` / **Password:** `password` (ADMIN role)

## Payload Examples

### Genre

**Create Genre:**
```json
{
    "name": "Fantasy"
}
```

**Update Genre (PATCH):**
```json
{
    "name": "Science Fiction"
}
```

### Actor

**Create Actor:**
```json
{
    "name": "Ryan Gosling",
    "birthDate": "1980-11-12",
    "movieIds": [1, 2, 3]
}
```

**Update Actor (PATCH):**
```json
{
    "name": "Ryan Gosling Updated",
    "birthDate": "1980-11-12",
    "movieIds": [1, 4, 5]
}
```

**Note:** `birthDate` must be in `YYYY-MM-DD` format and must be in the past.

### Movie

**Create Movie:**
```json
{
    "title": "Drive",
    "releaseYear": 2011,
    "duration": 100,
    "genreIds": [1, 3, 5],
    "actorIds": [2, 4, 6]
}
```

**Update Movie (PATCH):**
```json
{
    "title": "Drive Updated",
    "releaseYear": 2011,
    "duration": 120,
    "genreIds": [1, 2],
    "actorIds": [2, 3]
}
```

**Note:** 
- `releaseYear` must be >= 1900
- `duration` must be positive (in minutes)
- All fields are optional in PATCH requests (partial updates)

## Error Handling

The API provides clear error messages with appropriate HTTP status codes:

- **400 Bad Request** - Validation errors (invalid input data, date format, etc.)
- **404 Not Found** - Resource not found
- **409 Conflict** - Database not empty when trying to fill
- **500 Internal Server Error** - Unexpected server errors

### Example Error Responses

**Validation Error (400):**
```json
{
    "birthDate": "Birth date must be in the past"
}
```

**Resource Not Found (404):**
```
Actor not found with id 999
```

**Database Not Empty (409):**
```
Database is not empty. Please clear the database first using /api/clear
```

## Testing

A Postman collection is included in `src/test/java/com/example/movie_api/Postman.json` with examples of all endpoints, including authentication headers for ADMIN endpoints.
