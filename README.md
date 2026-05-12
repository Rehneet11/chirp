# Chirp

Chirp is a reactive social-posting API built with Spring Boot, WebFlux, MongoDB, Elasticsearch, and Kafka. It handles the core moves of a microblogging app: posting chirps, replying, rechirping, tagging, searching, and serving global or trending feeds.

## Why this project is fun

- Reactive API surface with Spring WebFlux and Reactor
- MongoDB for chirp persistence
- Elasticsearch-backed search for fast keyword discovery
- Kafka event flow for view-count and engagement-style processing
- Cursor pagination for feed-style reads

## Stack

- Java 25
- Spring Boot 4
- Spring WebFlux
- Reactive MongoDB
- Spring Data Elasticsearch
- Apache Kafka
- Lombok
- SpringDoc OpenAPI / Swagger UI

## API docs

Once the app is running, open:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Core endpoints

### Chirps

- `POST /api/chirps/create_chirp` - create a chirp
- `GET /api/chirps/{id}` - fetch a chirp by id
- `GET /api/chirps` - list chirps with cursor pagination
- `DELETE /api/chirps/{id}` - delete a chirp
- `GET /api/chirps/search` - search chirps by keyword
- `GET /api/chirps/elasticsearch` - search chirps in Elasticsearch
- `GET /api/chirps/tag/{tagId}` - list chirps by tag
- `PATCH /api/chirps/likes/{id}` - like a chirp
- `POST /api/chirps/rechirp/{id}` - rechirp an existing chirp
- `POST /api/chirps/reply` - reply to a chirp

### Feed

- `GET /api/feed/global` - global feed
- `GET /api/feed/trending` - trending feed

### Tags

- `POST /api/tags/create_tag` - create a tag

## Example payloads

### Create a chirp

```json
{
  "content": "Chirp is alive and very opinionated about clean APIs.",
  "chirperId": "user_123",
  "chirpType": "POST",
  "tagIds": ["tag_001", "tag_002"]
}
```

### Reply to a chirp

```json
{
  "content": "Replying in real time with WebFlux.",
  "parentChirpId": "681f6ab8f0f3e278cb41f001",
  "chirperId": "user_456",
  "chirpType": "REPLY",
  "tagIds": ["tag_001"]
}
```

### Create a tag

```json
{
  "tag": "springboot"
}
```

## Local setup

### Prerequisites

- Java 25
- MongoDB running on `localhost:27017`
- Elasticsearch running on `localhost:9200`
- Kafka running on `localhost:9092`

### Configuration

Default config lives in [application.yaml](/Users/rehneetsingh/Development/Chirp/src/main/resources/application.yaml).

### Run the app

```bash
./gradlew bootRun
```

### Run tests

```bash
./gradlew test
```

## Project structure

```text
src/main/java/com/example/Chirp
├── config
├── controllers
├── consumers
├── dto
├── enums
├── events
├── mapper
├── models
├── producers
├── repository
├── services
└── utils
```

## What’s next

- Add auth and ownership checks
- Add richer error responses
- Expand test coverage around feeds, search, and messaging flows
- Split API versions cleanly as the surface grows
