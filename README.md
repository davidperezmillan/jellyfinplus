# JellyfinPlus

A Spring Boot application for Jellyfin instance management, built with hexagonal architecture.

## Features

- Retrieve series from Jellyfin
- Retrieve episodes of a specific series
- Retrieve downloaded series and episodes

## Architecture

- **Domain**: Business logic and entities
- **Application**: Use cases and services
- **Infrastructure**: Adapters for web and external APIs

## Technologies

- Java 21
- Spring Boot 3.3.0
- Docker

## Configuration

Create a `.env` file in the root directory with the following variables:

```
JELLYFIN_BASE_URL=http://your-jellyfin-instance.com/
JELLYFIN_TOKEN=your-api-token
JELLYFIN_USER_NAME=your-username
```

Or set the following environment variables:

- `JELLYFIN_BASE_URL`: Base URL of your Jellyfin instance (default: http://localhost:8096)
- `JELLYFIN_TOKEN`: Your Jellyfin API token
- `JELLYFIN_USER_ID`: Your Jellyfin user ID (optional, will be fetched if not provided)
- `JELLYFIN_USER_NAME`: Your Jellyfin user name (optional, used to find user ID)

## API Endpoints

- `GET /api/series`: Get all series
- `GET /api/series/downloaded`: Get downloaded series
- `GET /api/episodes/series/{seriesId}`: Get episodes of a series
- `GET /api/episodes/downloaded`: Get downloaded episodes

## Running

1. Build: `mvn clean package`
2. Run: `java -jar target/jellyfinplus-1.0-SNAPSHOT.jar`

Or with Docker: `docker-compose up --build`

## Running Tests

- Unit tests: `mvn test`
- Integration tests (with real Jellyfin instance): `mvn test -Dtest=JellyfinIntegrationTest`
